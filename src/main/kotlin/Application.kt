import akka.actor.ActorRef
import akka.actor.ActorSystem

class Application {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val _system = ActorSystem.create("ApplicationActorSystem")
            val outputActor = _system.actorOf(OutputActor.props(), "output")
            val readerActor = _system.actorOf(FileReaderActor.props(outputActor), "reader")

            val path = javaClass.getResource("sample.csv").path
            readerActor.tell(Protocols.FilePathMessage(path), ActorRef.noSender())

        }
    }
}
