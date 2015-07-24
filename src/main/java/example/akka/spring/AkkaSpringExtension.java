package example.akka.spring;

import akka.actor.AbstractExtensionId;
import akka.actor.ExtendedActorSystem;
import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.context.ApplicationContext;
import scala.collection.immutable.$colon$colon;
import scala.collection.immutable.List;
import scala.collection.immutable.List$;

public class AkkaSpringExtension extends AbstractExtensionId<AkkaSpringExtension.SpringExt> {

    public static AkkaSpringExtension SpringExtProvider = new AkkaSpringExtension();

    public SpringExt createExtension(ExtendedActorSystem system) {
        return new SpringExt();
    }

    public static class SpringExt implements Extension {
        private volatile ApplicationContext applicationContext;

        public void initialize(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @SuppressWarnings("unchecked")
        public Props props(String actorBeanName) {

            // Some workaround to produce a valid scala Seq.
            // Produces a 'List(actorBeanName, applicationContext)'
            List<Object> scalaSeqConstructorParams = List$.MODULE$.empty();
            scalaSeqConstructorParams = new $colon$colon(actorBeanName, scalaSeqConstructorParams);
            scalaSeqConstructorParams = new $colon$colon(applicationContext, scalaSeqConstructorParams);

            // This also works as
            // Props.create(SpringActorProducer.class, actorBeanName, applicationContext);
            // but in some IDEs it's marked as compile error
            return Props.create(SpringActorProducer.class, scalaSeqConstructorParams);
        }
    }
}
