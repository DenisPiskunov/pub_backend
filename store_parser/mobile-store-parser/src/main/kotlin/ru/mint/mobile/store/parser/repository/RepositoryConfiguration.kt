package ru.mint.mobile.store.parser.repository

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.Database
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource


@EnableJpaRepositories
@EnableTransactionManagement
@Configuration
class RepositoryConfiguration {

    @Bean
    fun dataSource(): DataSource = JndiDataSourceLookup().getDataSource("jdbc/mobile_store_parser")

    @Bean
    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val emFactory = LocalContainerEntityManagerFactoryBean()
        emFactory.dataSource = dataSource
        emFactory.jpaVendorAdapter = HibernateJpaVendorAdapter().apply { setDatabase(Database.POSTGRESQL) }
        emFactory.setPackagesToScan("ru.mint.mobile.store.parser.repository.entity")
        emFactory.setJpaProperties(jpaProperties())
        return emFactory
    }

    @Bean
    fun transactionManager(emf: EntityManagerFactory) = JpaTransactionManager(emf)

    @Bean
    fun exceptionTranslation() = PersistenceExceptionTranslationPostProcessor()

    private fun jpaProperties() = Properties().apply {
        put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect")
        put("hibernate.show_sql", "true")
        put("hibernate.format_sql", "true")
    }
}