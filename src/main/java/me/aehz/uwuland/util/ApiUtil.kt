package me.aehz.uwuland.util

import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.io.Writer

object ApiUtil {
    suspend fun asJsonSSE(writer: Writer, delay: Long, data: Any) {
        val json = Gson().toJson(data)
        writer.write("data: $json\n\n")
        writer.flush()
        delay(delay)
    }
}