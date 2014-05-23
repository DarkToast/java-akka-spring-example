package example.akka.actors;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import com.google.common.base.Optional;
import example.akka.SchedulerConfig;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Named("ControlActor")
@Scope("prototype")
public class ControlActor extends UntypedActor {
    private Optional<Cancellable> jobScheduler = Optional.absent();

    @Inject
    private ActorRef databaseActorRef;

    @Override
    public void onReceive(Object message) throws Exception {
        if (!message.equals("Update")) {
            unhandled(message);
        }

        boolean configState = getConfigState();
        boolean schedulerState = jobScheduler.isPresent() && !jobScheduler.get().isCancelled();

        long id = Thread.currentThread().getId();
        System.out.println(String.format("[ControlActor, %d] - Reading state. State is %b.", id, configState));

        if (configState && !schedulerState) {
            System.out.println(String.format("[ControlActor, %d] - Starting the job scheduler.", id));

            jobScheduler = Optional.of(getContext().system().scheduler().schedule(
                Duration.create(50, TimeUnit.MILLISECONDS),
                Duration.create(SchedulerConfig.databaseInterval, TimeUnit.SECONDS),
                databaseActorRef,
                "Tick",
                getContext().dispatcher(),
                getSelf()
            ));
        } else if (!configState && schedulerState) {
            System.out.println(String.format("[ControlActor, %d] Stopping the job scheduler.", id));
            jobScheduler.get().cancel();
        }
    }

    private boolean getConfigState() {
        try {
            return new String(Files.readAllBytes(Paths.get("./trigger.txt"))).equals("true");
        } catch (IOException e) {
            return false;
        }
    }

}
