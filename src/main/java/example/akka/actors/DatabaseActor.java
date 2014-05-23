package example.akka.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import example.akka.model.WorkerResult;
import org.springframework.context.annotation.Scope;
import example.akka.model.RegisterData;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.SimpleDateFormat;
import java.util.Date;

@Named("DatabaseActor")
@Scope("prototype")
public class DatabaseActor extends UntypedActor {

    @Inject
    private ActorRef workerActorRef;

    public void onReceive(Object message) throws Exception {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        long id = Thread.currentThread().getId();

        if (message.equals("Tick")) {
            Thread.sleep(500);
            int rand = (int) (Math.random() * 5);

            System.out.println(String.format("[DatabaseActor, %d, %s] - Getting a tick: Reading database and get %d registrations.", id, time, rand));
            for (int i = 0; i < rand; i++) {
                workerActorRef.tell(RegisterData.of((int) (Math.random() * 1000), "Max_" + i, "Mainstreet" + i), this.getSelf());
            }
        } else if (message instanceof WorkerResult) {
            WorkerResult result = (WorkerResult) message;
            if (result.isSuccess()) {
                System.out.println(String.format("[DatabaseActor, %d, %s] - Getting result from worker for id %d. It was a SUCCESS!", id, time, result.getId()));
            } else {
                System.out.println(String.format("[DatabaseActor, %d, %s] - Getting result from worker for id %d. It has FAILED!", id, time, result.getId()));
            }
        } else {
            unhandled(message);
        }
    }
}
