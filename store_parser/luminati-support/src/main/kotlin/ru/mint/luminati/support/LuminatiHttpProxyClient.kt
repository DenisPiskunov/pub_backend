package ru.mint.luminati.support

import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import java.io.IOException
import java.net.InetAddress
import java.util.*


/**
 * http client using https://luminati.io/ proxy.
 * After every http request client will switch ip randomly.
 */
class LuminatiHttpProxyClient<out T>(private val username: String,
                                     private val password: String,
                                     private val country: String? = null,
                                     private val maxFailures: Int,
                                     port: Int,
                                     private val reqTimeout: Int = 60 * 1000,
                                     private val responseConverter: HttpResponseConverter<T>) {

    private var superProxy: HttpHost? = null
    private var client: CloseableHttpClient? = null
    private val random = Random()
    private var failureCount = 0
    private var proxyPort = 0


    init {
        proxyPort = port
        initProxy()
    }



    fun resetFailureCountAndReinitProxy() {
        failureCount = 0
        initProxy()
    }

    fun get(url: String): HttpResponseEntity<T> {
        if (failureCount == maxFailures) {
            throw ProxyIpsBannedException("Probably all IP addresses banned.")
        }
        return executeGet(url)
    }

    private fun initProxy() {
        val proxySessionId = random.nextInt(Integer.MAX_VALUE).toString()
        val address = InetAddress.getByName("session-$proxySessionId.zproxy.luminati.io")
        superProxy = HttpHost(address.hostAddress, proxyPort)
        client = newHttpClient()
    }

    private fun executeGet(url: String): HttpResponseEntity<T> {
        val request = HttpGet(url)
        do {
            try {
                val response = client!!.execute(request)
                if (isStatusCodeRequiresNodeSwitch(response.statusLine.statusCode)) {
                    switchHttpClient()
                    failureCount++
                } else {
                    val responseEntity = responseConverter.convert(response)
                    response.close()
                    switchHttpClient()
                    return responseEntity
                }
            } catch (e: IOException) {
                failureCount++
                throw e
            }
        } while (failureCount < maxFailures)
        throw ProxyIpsBannedException("Probably all IP addresses banned.")
    }

    private fun switchHttpClient() {
        client!!.close()
        client = newHttpClient()
    }

    private fun newHttpClient(): CloseableHttpClient {
        val login = newSessionLogin()
        val credProvider = BasicCredentialsProvider()
        credProvider.setCredentials(AuthScope(superProxy), UsernamePasswordCredentials(login, password))
        val config = RequestConfig.custom().setConnectTimeout(reqTimeout).setConnectionRequestTimeout(reqTimeout).build()
        val connMgr = PoolingHttpClientConnectionManager()
        connMgr.defaultMaxPerRoute = Integer.MAX_VALUE
        connMgr.maxTotal = Integer.MAX_VALUE
        return HttpClients.custom()
                .setConnectionManager(connMgr)
                .setProxy(superProxy)
                .setDefaultCredentialsProvider(credProvider)
                .setDefaultRequestConfig(config)
                .build()
    }

    private fun newSessionLogin(): String {
        val sessionId = Integer.toString(random.nextInt(Integer.MAX_VALUE))
        val countryPart = if (country != null) "-country-$country" else ""
        return "$username$countryPart-session-$sessionId"
    }

    private fun isStatusCodeRequiresNodeSwitch(statusCode: Int): Boolean {
        return statusCode == 403 || statusCode == 429 || statusCode == 502 || statusCode == 503
    }

}