package com.data_advisor.school.spring;

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

        HelloWorld helloWorld = ctx.getBean(HelloWorld.class);

        helloWorld.say();
    }
}
