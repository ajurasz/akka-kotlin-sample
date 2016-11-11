import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import akka.testkit.TestProbe;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JavaTest extends JavaTestKit {
    public JavaTest() {
        super(ActorSystem.create("TestActorSystem"));
    }

    private TestActorRef<OutputActor> outputTestActorRef;
    private TestActorRef<FileReaderActor> fileReaderTestActorRef;

    @Before
    public void before() {
        outputTestActorRef = TestActorRef.create(getSystem(), OutputActor.props());
        fileReaderTestActorRef = TestActorRef.create(getSystem(), FileReaderActor.props(outputTestActorRef));
    }

    @Test
    public void shouldReceiveMessageByFileReaderActor() {
        // When
        fileReaderTestActorRef.tell(new Protocols.FilePathMessage(getClass().getResource("sample.csv").getPath()), ActorRef.noSender());

        // Then
        new Within(duration("10 seconds")) {
            @Override
            protected void run() {
                assertEquals(1, fileReaderTestActorRef.underlyingActor().getCounter());
            }
        };
    }

    @Test
    public void shouldReceiveMessageByOutputActor() {
        // Given
        TestProbe testProbe = new TestProbe(getSystem());
        ActorRef outputActorRef = getSystem().actorOf(OutputActor.props(), "outputTest");
        ActorRef fileReaderActorRef = getSystem().actorOf(FileReaderActor.props(testProbe.testActor()), "fileReaderTest");

        // When
        fileReaderActorRef.tell(new Protocols.FilePathMessage(getClass().getResource("sample.csv").getPath()), ActorRef.noSender());

        // Then
        testProbe.expectMsgClass(Protocols.DataMessage.class);
    }
}
