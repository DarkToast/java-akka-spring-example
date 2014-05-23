package example.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

import static example.akka.spring.AkkaSpringExtension.SpringExtProvider;

@Configuration
class AppConfiguration {

    @Inject
    private ApplicationContext applicationContext;

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("SchedulerTest");
        SpringExtProvider.get(system).initialize(applicationContext);
        return system;
    }

    @Bean
    public ActorRef controlActorRef() {
        return actorSystem().actorOf(
            SpringExtProvider.get(actorSystem()).props("ControlActor"), "controller");
    }

    @Bean
    public ActorRef databaseActorRef() {
        return actorSystem().actorOf(
            SpringExtProvider.get(actorSystem()).props("DatabaseActor"), "database");
    }

    @Bean
    public ActorRef workerActorRef() {
        return actorSystem().actorOf(
            SpringExtProvider.get(actorSystem()).props("WorkerActor"), "worker");
    }

    @Bean
    public Cancellable mainScheduler() {
        return actorSystem().scheduler().schedule(
            Duration.create(0, TimeUnit.SECONDS),
            Duration.create(SchedulerConfig.controllerInterval, TimeUnit.SECONDS),
            controlActorRef(),
            "Update",
            actorSystem().dispatcher(),
            null
        );
    }
}
