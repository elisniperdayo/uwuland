package me.aehz.uwuland.abstracts

import com.google.gson.Gson
import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import me.aehz.uwuland.API.SseEvent
import me.aehz.uwuland.API.respondSse
import org.bukkit.Bukkit

@OptIn(kotlinx.coroutines.ObsoleteCoroutinesApi::class, ExperimentalCoroutinesApi::class)
abstract class SharedSseController {
    var previousSseEvent: SseEvent? = null
    val scope = CoroutineScope(Dispatchers.Default)
    private val channel = scope.produce {
        while (true) {
            val data = getSseData()
            Bukkit.getLogger().info("SENDING")
            handleSend(this, data)
            delay(1000)
        }
    }.broadcast()

    abstract fun getSseData(): Any

    suspend fun handleSend(producer: ProducerScope<SseEvent>, data: Any, forceSend: Boolean = false) {
        val event = SseEvent(Gson().toJson(data))
        if (shouldSend(event) || forceSend) {
            producer.send(event)
            previousSseEvent = event
        }
    }

    fun shouldSend(newEvent: SseEvent): Boolean {
        val prev = previousSseEvent ?: return true
        if (prev.data != newEvent.data) return true
        return false
    }

    suspend fun sse(call: ApplicationCall) {
        val sub = channel.openSubscription()
        call.respondSse(sub, previousSseEvent)
    }
}