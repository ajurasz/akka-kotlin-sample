import akka.actor.Props
import akka.actor.UntypedActor
import akka.event.Logging

class OutputActor() : UntypedActor() {
    val log = Logging.getLogger(context.system(), this)

    var counter = 0

    override fun onReceive(message: Any?) {
        when (message) {
            is Protocols.DataMessage -> {
                log.debug("Processed ${++counter}")
                log.debug(message.toString())
            }
        }
    }

    companion object {
        @JvmStatic
        fun props() = Props.create(OutputActor::class.java)
    }
}