package ru.mint.plugins

import io.ktor.application.*
import io.ktor.config.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import ru.mint.dao.Databases
import ru.mint.service.*
import ru.mint.service.converter.*
import ru.mint.service.mail.MailService
import ru.mint.service.mail.MailServiceImpl

fun Application.configureDI(databases: Databases) {
    di {
        importAll(serviceModule(environment.config, databases))
    }
}

private fun serviceModule(appConfig: ApplicationConfig, databases: Databases) = DI.Module("service") {

    bind<Databases>() with singleton { databases }

    bind<AuthorityConverter>() with singleton { AuthorityConverterImpl() }
    bind<RoleConverter>() with singleton { RoleConverterImpl(instance()) }
    bind<MasterAccountConverter>() with singleton { MasterAccountConverterImpl() }
    bind<AccountConverter>() with singleton { AccountConverterImpl(instance()) }

    val mailConfig = MailingConfigurationFactory().newConfig(appConfig)
    bind<MailService>() with singleton { MailServiceImpl(mailConfig) }

    bind<PasswordEncoder>() with singleton { BCryptPasswordEncoder() }
    bind<PasswordGenerator>() with singleton { PasswordGeneratorImpl() }

    bind<AccountService>() with singleton { AccountServiceImpl(instance(), instance(), instance()) }
    bind<MasterAccountService>() with singleton { MasterAccountServiceImpl(instance(), instance(), instance(), instance(), instance()) }
    bind<RoleService>() with singleton { RoleServiceImpl(instance()) }
    bind<AuthorityService>() with singleton { AuthorityServiceImpl(instance()) }
}