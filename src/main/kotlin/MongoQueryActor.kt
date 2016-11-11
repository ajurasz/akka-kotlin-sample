import akka.actor.Props
import akka.actor.UntypedActor
import akka.event.Logging
import java.util.concurrent.ThreadLocalRandom

class MongoQueryActor() : UntypedActor() {
    val log = Logging.getLogger(context.system(), this)

    override fun onReceive(message: Any?) {
        when (message) {
            is Protocols.DataMessage -> {
                Thread.sleep(ThreadLocalRandom.current().nextInt(50, 70 + 1).toLong())
                sender.tell(message.copy(marketIds = randomMarketIds()), self)
            }
        }
    }

    private fun randomMarketIds() =
        (1..ThreadLocalRandom.current().nextInt(10 + 1))
                .map { ThreadLocalRandom.current().nextInt(100 + 1) }

    companion object {
        fun props() = Props.create(MongoQueryActor::class.java)
    }
}