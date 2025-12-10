package ru.mint.mobile.store.parser.service

import ru.mint.mobile.store.parser.repository.entity.Application

interface TopPingerService {
    fun pingApps()
    fun prepareRemovedApp(application: Application)
}