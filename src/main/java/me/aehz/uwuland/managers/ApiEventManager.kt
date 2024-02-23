package me.aehz.uwuland.managers

import com.google.common.collect.EvictingQueue
import com.google.common.collect.Queues
import me.aehz.uwuland.API.Data.ApiDataEvent
import me.aehz.uwuland.enums.ApiEventType
import java.util.Queue

object ApiEventManager {
    val events: MutableMap<ApiEventType, Queue<ApiDataEvent>> = mutableMapOf()
    val maxSize = 5

    fun add(e: ApiDataEvent) {
        if (!events.containsKey(e.type)) {
            val q = EvictingQueue.create<ApiDataEvent>(maxSize)
            val syncQ = Queues.synchronizedQueue(q)
            events[e.type] = syncQ
        }
        events[e.type]!!.add(e)
    }
}