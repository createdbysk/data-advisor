package com.data_advisor.local.application.entry_point.impl;

import com.data_advisor.local.application.FileSystemAbstractFactory;
import com.data_advisor.local.service.file_system.FileSystemService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * FileSystemAbstractFactory implementation for local file system.
 */
@Component
public class LocalFileSystemFactory implements FileSystemAbstractFactory, ApplicationContextAware {
    private final FileSystemService fileSystemService;
    private ApplicationContext applicationContext;
    /**
     * Private constructor to force use of this object through Dependency injection.
     * @param fileSystemService The file system service instance.
     *
     */
    @Autowired
    private LocalFileSystemFactory(FileSystemService fileSystemService) {
        this.fileSystemService = fileSystemService;
    }

    @Override
    public FileSystemService getFileSystemService() {
        return fileSystemService;
    }

    @Override
    public FileVisitor<Path> createFileVisitor() {
        // LocalFileSystemVisitor has prototype scope. So every call to getBean() returns
        // a new instance of that class.
        // Define a local variable to ease debugging.
        @SuppressWarnings("redundant")
        final FileVisitor<Path> fileVisitor = applicationContext.getBean(LocalFileSystemVisitor.class);
        return fileVisitor;
    }

    @Override
    public Path getPath(String absolutePath) {
        // Define local variable to aid debugging.
        @SuppressWarnings("redundant")
        Path path = Paths.get(absolutePath);
        return path;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
