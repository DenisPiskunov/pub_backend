package ru.mint.mobile.store.parser.rest

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import javax.validation.Validator
import org.springframework.web.servlet.config.annotation.CorsRegistry

@EnableWebMvc
@ComponentScan
@Configuration
class WebConfiguration : WebMvcConfigurerAdapter() {

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>?) {
        val builder = Jackson2ObjectMapperBuilder().apply { serializationInclusion(JsonInclude.Include.NON_NULL) }
        converters!!.add(MappingJackson2HttpMessageConverter(builder.build()))
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry?) {
        registry?.addResourceHandler("/**")?.addResourceLocations("/WEB-INF/resources/static/")
    }

    override fun addCorsMappings(registry: CorsRegistry?) {
        registry!!.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST")
                .allowedHeaders("Access-Control-Allow-Origin", "Content-Type")
                .allowCredentials(true)
                .maxAge(6000)
    }

    @Bean
    fun validator() = LocalValidatorFactoryBean()

    @Bean
    fun methodValidationPostProcessor(validator: Validator) = MethodValidationPostProcessor().apply { setValidator(validator) }
}