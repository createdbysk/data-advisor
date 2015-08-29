package com.data_advisor.school.spring;

import com.data_advisor.school.spring.event.BaseEventPublisher;
import com.data_advisor.school.spring.event.DerivedEvent1;
import com.data_advisor.school.spring.event.DerivedEvent2;
import com.data_advisor.school.spring.hello_world.HelloWorld;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * The application entry point
 */
public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        ctx.register(AppConfig.class);

        ctx.refresh();

        // Test annotation configuration.
        TestApplicationConfig(ctx);

        // Test event mechanism.
        TestEvents(ctx);
    }

    /** How event dispatch behaves with polymorphism */
    private static void TestEvents(AnnotationConfigApplicationContext ctx) {
        // Get the publisher bean.
        BaseEventPublisher baseEventPublisher = ctx.getBean(BaseEventPublisher.class);
        // Create a DerivedEvent1. Expect the BaseEventListener and the DerivedEvent1Listener to receive this event.
        DerivedEvent1 derivedEvent1 = new DerivedEvent1(baseEventPublisher);
        baseEventPublisher.publish(derivedEvent1);

        // Create a DerivedEvent2. Expect the BaseEventListener and the DerivedEvent1Listener to receive this event.
        DerivedEvent2 derivedEvent2 = new DerivedEvent2(baseEventPublisher);
        baseEventPublisher.publish(derivedEvent2);
    }

    private static void TestApplicationConfig(AnnotationConfigApplicationContext ctx) {
        HelloWorld helloWorld = ctx.getBean(HelloWorld.class);

        helloWorld.say();
    }
}
