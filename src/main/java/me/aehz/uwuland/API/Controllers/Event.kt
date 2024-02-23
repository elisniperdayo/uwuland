package me.aehz.uwuland.API.Controllers

import me.aehz.uwuland.abstracts.SharedSseController
import me.aehz.uwuland.managers.ApiEventManager
import java.time.Duration

object EventController : SharedSseController() {
    override val delay = Duration.ofMillis(100)
    override fun getSseData(): Any {
        return ApiEventManager.events
    }
}
