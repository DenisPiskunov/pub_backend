package ru.mint.mobile.store.parser.rest

import org.springframework.web.filter.CharacterEncodingFilter
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer
import ru.mint.mobile.store.parser.repository.RepositoryConfiguration
import ru.mint.mobile.store.parser.security.SecurityConfiguration
import ru.mint.mobile.store.parser.service.ServiceConfiguration
import java.nio.charset.StandardCharsets
import javax.servlet.Filter

class WebAppInitializer : AbstractAnnotationConfigDispatcherServletInitializer() {
    override fun getRootConfigClasses() =
            //TODO: Uncomment this string for remote-server deployment
//            arrayOf(RepositoryConfiguration::class.java, ServiceConfiguration::class.java , SecurityConfiguration::class.java)

            //TODO: Uncomment this stri for local-server deployment and testing client-side
            arrayOf(RepositoryConfiguration::class.java, ServiceConfiguration::class.java)

    override fun getServletMappings() = arrayOf("/")

    override fun getServletConfigClasses() = arrayOf(WebConfiguration::class.java)

    override fun getServletFilters(): Array<Filter> {
        val characterEncodingFilter = CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true)
        return arrayOf(characterEncodingFilter)
    }
}