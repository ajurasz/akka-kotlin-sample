import akka.actor.Props
import akka.actor.UntypedActor
import akka.event.Logging
import java.util.concurrent.TimeUnit

class TimerActor : UntypedActor() {
    val log = Logging.getLogger(context.system(), this)

    var startTime = 0L

    override fun onReceive(message: Any?) {
        when (message) {
            is Protocols.Start -> startTime = System.currentTimeMillis()
            is Protocols.Stop -> {
                val totalTime = System.currentTimeMillis() - startTime
                log.info("Processing time was ${TimeUnit.MILLISECONDS.toSeconds(totalTime)} sec")
            }
        }
    }

    companion object {
        fun props() = Props.create(TimerActor::class.java)
    }
}
