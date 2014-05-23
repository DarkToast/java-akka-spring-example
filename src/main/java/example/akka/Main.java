package example.akka;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext ctx =  new AnnotationConfigApplicationContext();
        ctx.scan("example.akka");
        ctx.refresh();
    }
}
