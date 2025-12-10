package ru.mint.mobile.store.parser.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.stereotype.Component
import ru.mint.mobile.store.parser.repository.entity.SettingName
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


@EnableScheduling
@Component
class ScheduledTasks : SchedulingConfigurer {
    @Autowired
    private lateinit var applicationParsingDataService: ApplicationParsingDataService
    @Autowired
    private lateinit var topPingerService: TopPingerService
    @Autowired
    private lateinit var appSettingsService: AppSettingsService
    @Autowired
    private lateinit var serviceConfiguration: ServiceConfiguration
    @Autowired
    private lateinit var categoriesService: CategoriesService

    private var updaterTaskInterval: Long = 0
    private var topParserTaskInterval: Long = 0
    private var pingerTaskInterval: Long = 0

    private var pingerProcess: Runnable? = null
    private var topParserProcess: Runnable? = null
    private var updaterProcess: Runnable? = null

    private var pingerTask: ScheduledFuture<*>? = null
    private var topParserTask: ScheduledFuture<*>? = null
    private var updaterDataTask: ScheduledFuture<*>? = null

    private var needRunTopParserTask: Boolean = false
    private var needRunPingerTask: Boolean = false
    private var needRunUpdaterTask: Boolean = false

    private var isPingerTaskRunning: Boolean = false
    private var isTopParserTaskRunning: Boolean = false
    private var isUpdaterTaskRunning: Boolean = false
    private var checkCategoriesOnStart: Boolean = false


    private companion object {
        val logger: Logger = LoggerFactory.getLogger(ScheduledTasks::class.java)
    }

    private fun getCtegoriesChecker() {
        checkCategoriesOnStart = appSettingsService.getCheckCategoriesOnStart()
        if (checkCategoriesOnStart) {
            categoriesService.downloadAllCategories()
        }
    }

    private fun getScheduleTasksParams() {
        updaterTaskInterval = appSettingsService.getParsingDataUpdateInterval()
        topParserTaskInterval = appSettingsService.getParseTopInterval()
        pingerTaskInterval = appSettingsService.getAppPingerInterval()

    }

    private fun createProcesses() {
        pingerProcess = Runnable {
            kotlin.run {
                if (!isUpdaterTaskRunning && !isTopParserTaskRunning) {
                    logger.debug("Pinger started")
                    needRunPingerTask = false
                    isPingerTaskRunning = true
                    topPingerService.pingApps()
                    isPingerTaskRunning = false

                    if (needRunTopParserTask) {
                        topParserProcess!!.run()
                    }
                    if (needRunUpdaterTask) {
                        updaterProcess!!.run()
                    }
                    logger.debug("Pinger finished")
                } else {
                    needRunPingerTask = true
                    logger.debug("===>>> PingerTask: Others tasks running")
                }
            }
        }

        topParserProcess = Runnable {
            kotlin.run {
                if(!isUpdaterTaskRunning && !isPingerTaskRunning) {
                    logger.debug("Start to parsing top stores.")
                    needRunTopParserTask = false
                    isTopParserTaskRunning = true
                    applicationParsingDataService.processNewTopApplications()
                    isTopParserTaskRunning = false

                    if (needRunPingerTask) {
                        pingerProcess!!.run()
                    }
                    if (needRunUpdaterTask) {
                        updaterProcess!!.run()
                    }

                    logger.debug("Parsing top stores finished.")

                } else {
                    logger.debug("===>>> TopParserTask: Others tasks running")
                    needRunTopParserTask = true
                }
            }
        }

//        updaterProcess = Runnable {
//            kotlin.run {
//                if (!isPingerTaskRunning && !isTopParserTaskRunning) {
//                    logger.debug("Updater started")
//                    needRunUpdaterTask = false
//                    isUpdaterTaskRunning = true
//                    applicationParsingDataService.updateParsingData()
//                    isUpdaterTaskRunning = false
//
//                    if (needRunPingerTask) {
//                        pingerProcess!!.run()
//                    }
//                    if (needRunTopParserTask) {
//                        topParserProcess!!.run()
//                    }
//                    logger.debug("Updater finished")
//                } else {
//                    logger.debug("===>>> UpdaterTask: Others tasks running")
//                    needRunUpdaterTask = true
//                }
//            }
//        }
    }

    private fun createPingerTask() {
        pingerTask = serviceConfiguration.taskScheduler().scheduledExecutor.scheduleWithFixedDelay(pingerProcess, 180000, pingerTaskInterval, TimeUnit.MILLISECONDS)
//        pingerTask = serviceConfiguration.taskScheduler().scheduledExecutor.scheduleWithFixedDelay(pingerProcess, pingerTaskInterval, pingerTaskInterval, TimeUnit.MILLISECONDS)
    }

    private fun createTopParserTask() {
        topParserTask = serviceConfiguration.taskScheduler().scheduledExecutor.scheduleWithFixedDelay(topParserProcess, 600000, topParserTaskInterval, TimeUnit.MILLISECONDS)
//        topParserTask = serviceConfiguration.taskScheduler().scheduledExecutor.scheduleWithFixedDelay(topParserProcess, topParserTaskInterval, topParserTaskInterval, TimeUnit.MILLISECONDS)
    }

    private fun createUpdaterDataTask() {
//        updaterDataTask = serviceConfiguration.taskScheduler().scheduledExecutor.scheduleWithFixedDelay(updaterProcess, 2700000, updaterTaskInterval, TimeUnit.MILLISECONDS)
//        updaterDataTask = serviceConfiguration.taskScheduler().scheduledExecutor.scheduleWithFixedDelay(updaterProcess, updaterTaskInterval, updaterTaskInterval, TimeUnit.MILLISECONDS)
    }

    @Synchronized
    fun recreateTasks(settingName: SettingName, hard: Boolean) {
        getScheduleTasksParams()
        when (settingName) {
            SettingName.APPS_PINGER_INTERVAL -> {
                pingerTask!!.cancel(hard)
                createPingerTask()
            }

            SettingName.PARSE_TOP_INTERVAL -> {
                topParserTask!!.cancel(hard)
                createTopParserTask()
            }

            SettingName.PARSING_DATA_UPDATE_INTERVAL -> {
                updaterDataTask!!.cancel(hard)
                createUpdaterDataTask()
            }

            else -> return
        }
    }


    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar?) {
        getCtegoriesChecker()
        getScheduleTasksParams()
        createProcesses()
        createPingerTask()
        createTopParserTask()
        createUpdaterDataTask()
    }


}