package com.data_advisor.local.application;

import com.data_advisor.local.application.entry_point.impl.LocalFileSystem;
import org.apache.storm.guava.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality used by the Main application entry point.
 */
@Component
public class Application {
    @VisibleForTesting
    LocalFileSystem localFileSystem;

    @Autowired
    public Application(LocalFileSystem localFileSystem) {
        this.localFileSystem = localFileSystem;
    }
    /**
     * Run the application with the given arguments.
     * @param args  The command line arguments to the application.
     */
    public void run(String[] args) {
        final String absolutePath = args[0];
        localFileSystem.addHierarchy(absolutePath);
    }

}
