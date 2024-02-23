package me.aehz.uwuland.managers

import com.google.common.collect.EvictingQueue
import com.google.common.collect.Queues
import me.aehz.uwuland.API.Data.ApiEvent
import me.aehz.uwuland.enums.ApiEventType
import java.util.Queue

object ApiEventManager {
    val events: MutableMap<ApiEventType, Queue<ApiEvent>> = mutableMapOf()
    val maxSize = 5

    fun add(e: ApiEvent) {
        if (!events.containsKey(e.type)) {
            val q = EvictingQueue.create<ApiEvent>(maxSize)
            val syncQ = Queues.synchronizedQueue(q)
            events[e.type] = syncQ
        }
        events[e.type]!!.add(e)
    }
}