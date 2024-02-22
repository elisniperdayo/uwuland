package me.aehz.uwuland.API

import io.ktor.server.application.*
import kotlinx.coroutines.channels.ReceiveChannel
import io.ktor.http.*
import io.ktor.server.response.*
import java.io.Writer

data class SseEvent(val data: String, val event: String? = null, val id: String? = null)

suspend fun ApplicationCall.respondSse(events: ReceiveChannel<SseEvent>, init: SseEvent? = null) {
    respondTextWriter(contentType = ContentType.Text.EventStream) {
        if (init != null) sendSse(this, init)
        for (event in events) {
            sendSse(this, event)
        }
    }
}

fun sendSse(writer: Writer, event: SseEvent) {
    if (event.id != null) {
        writer.write("id: ${event.id}\n")
    }
    if (event.event != null) {
        writer.write("event: ${event.event}\n")
    }
    for (dataLine in event.data.lines()) {
        writer.write("data: $dataLine\n")
    }
    writer.write("\n")
    writer.flush()
}