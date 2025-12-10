package ru.mint.mobile.store.parser.service

import ru.mint.mobile.store.parser.repository.entity.Application

class ApplicationQueue private constructor() {

    companion object {
       private var mInstance: ApplicationQueue = ApplicationQueue()

        @Synchronized
        fun getInstance(): ApplicationQueue {
            return mInstance
        }
    }

    private var items: MutableList<Application> = mutableListOf()

    private fun isEmpty(): Boolean = this.items.isEmpty()

    fun count(): Int = this.items.count()

    override fun toString() = this.items.toString()

    fun enqueue(element: Application) {
        this.items.add(element)
    }

    fun dequeue(): Application? {
        return if (this.isEmpty()) {
            null
        } else {
            this.items.removeAt(0)
        }
    }
}
