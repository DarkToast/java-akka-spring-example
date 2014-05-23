package example.akka.actors;

import akka.actor.UntypedActor;
import example.akka.model.RegisterData;
import example.akka.model.WorkerResult;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import java.text.SimpleDateFormat;
import java.util.Date;

@Named("WorkerActor")
@Scope("prototype")
public class WorkerActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RegisterData) {
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            long id = Thread.currentThread().getId();

            RegisterData data = (RegisterData) message;
            System.out.println(String.format("[WorkerActor, %d, %s] - Munching some data andget some work done: id: %d, name: %s, ekp: %s", id, time, data.getId(), data.getName(), data.getStreet()));
            Thread.sleep(2000);

            getSender().tell(WorkerResult.of(data.getId(), Math.random() < 0.7), getSelf());
        } else {
            unhandled(message);
        }

    }
}
