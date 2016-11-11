import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.UntypedActor
import akka.dispatch.OnComplete
import akka.event.Logging
import akka.pattern.Patterns
import akka.routing.RoundRobinPool
import akka.util.Timeout
import org.apache.commons.csv.CSVFormat
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class FileReaderActor(val outputActor: ActorRef) : UntypedActor() {
    val log = Logging.getLogger(context.system(), this)

    val t = Timeout(Duration.create(10, TimeUnit.SECONDS))

    val timerActor: ActorRef
    val mongoQueryActor: ActorRef

    var counter = 0

    init {
        this.timerActor = context.actorOf(TimerActor.props(), "timer")
        this.mongoQueryActor = context.actorOf(MongoQueryActor.props()
                .withRouter(RoundRobinPool(20))
                .withMailbox("bounded-mailbox"), "mongoQuery")
    }

    override fun onReceive(message: Any?) {
        when (message) {
            is Protocols.FilePathMessage -> {
                timerActor.tell(Protocols.Start(), ActorRef.noSender())
                val inputStream = File(message.path).inputStream()
                CSVFormat.DEFAULT.withFirstRecordAsHeader()
                        .parse(InputStreamReader(inputStream))
                        .iterator()
                        .forEach {
                            if (it.get(8) != null && it.get(9) != null) {
                                log.debug("Query #${++counter}")
                                val future: Future<Any> = Patterns.ask(mongoQueryActor, Protocols.DataMessage(it.get(0).toInt(), Models.Point(it.get(8).toDouble(), it.get(9).toDouble())), t)
                                future.onComplete(object : OnComplete<Any?>() {
                                    override fun onComplete(failure: Throwable?, success: Any?) {
                                        log.debug(success.toString())
                                        outputActor.tell(success, self)
                                    }
                                }, context.dispatcher())
                            }
                        }
                timerActor.tell(Protocols.Stop(), ActorRef.noSender())
            }
        }
    }

    companion object {
        @JvmStatic
        fun props(outputActor: ActorRef) = Props.create(FileReaderActor::class.java, outputActor)
    }
}