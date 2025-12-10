package ru.mint.plugins

import io.ktor.application.*
import io.ktor.util.*
import org.slf4j.*

class ApplicationEventsMonitor(private val monitor: ApplicationEvents) {
    class Configuration { }

    private val logger = LoggerFactory.getLogger(javaClass)
    private val starting: (Application) -> Unit = { logger.info("Application starting: $it") }
    private val started: (Application) -> Unit = { logger.info("Application started: $it") }
    private val stopping: (Application) -> Unit = { logger.info("Application stopping: $it") }
    private var stopped: (Application) -> Unit = {}

    init {
        stopped = {
            logger.info("Application stopped: $it")
            monitor.unsubscribe(ApplicationStarting, starting)
            monitor.unsubscribe(ApplicationStarted, started)
            monitor.unsubscribe(ApplicationStopping, stopping)
            monitor.unsubscribe(ApplicationStopped, stopped)
        }

        monitor.subscribe(ApplicationStarting, starting)
        monitor.subscribe(ApplicationStarted, started)
        monitor.subscribe(ApplicationStopping, stopping)
        monitor.subscribe(ApplicationStopped, stopped)
    }

    companion object Feature : ApplicationFeature<Application, Configuration, ApplicationEventsMonitor> {
        override val key: AttributeKey<ApplicationEventsMonitor> = AttributeKey("Call Monitor")
        override fun install(pipeline: Application, configure: Configuration.() -> Unit): ApplicationEventsMonitor {
            return ApplicationEventsMonitor(pipeline.environment.monitor)
        }
    }
}