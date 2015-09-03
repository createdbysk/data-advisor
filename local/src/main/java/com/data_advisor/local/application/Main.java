package com.data_advisor.local.application;

import org.apache.storm.guava.annotations.VisibleForTesting;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * The main application entry point.
 */
public class Main {
    private final AnnotationConfigApplicationContext annotationConfigApplicationContext;

    public static void main(String[] args) {
        // Create an instance of the Main class to be able to test the functionality to
        // bootstrap the application.
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        Main main = new Main(ctx);
        main.loadAndRun(args);
    }

    @VisibleForTesting
    Main(AnnotationConfigApplicationContext annotationConfigApplicationContext) {
        this.annotationConfigApplicationContext = annotationConfigApplicationContext;
    }

    /**
     * Load and run the application with the command-line parameters.
     * @param args The command-line parameters.
     */
    @VisibleForTesting
    void loadAndRun(String[] args) {
        annotationConfigApplicationContext.register(ApplicationConfig.class);
        annotationConfigApplicationContext.refresh();
        Application application = annotationConfigApplicationContext.getBean(Application.class);
        application.run(args);
    }
}
