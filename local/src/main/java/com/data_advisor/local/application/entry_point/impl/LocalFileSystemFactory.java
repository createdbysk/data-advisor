package com.data_advisor.local.application.entry_point.impl;

import com.data_advisor.local.application.FileSystemAbstractFactory;
import com.data_advisor.local.event.file_system.*;
import com.data_advisor.local.service.file_system.FileSystemService;
import org.apache.storm.guava.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * FileSystemAbstractFactory implementation for local file system.
 */
@Component
public class LocalFileSystemFactory implements FileSystemAbstractFactory {
    // This class does not have a default constructor.
    // The test will use constructor injection to inject mocks.
    // Therefore, allow the test to set the logger.
    @VisibleForTesting
    Logger logger = LoggerFactory.getLogger(LocalFileSystemFactory.class);

    private final FileSystemService fileSystemService;

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
        final FileVisitor<Path> fileVisitor = new LocalFileSystemVisitor(this);
        logger.trace("createFileVisitor() returned {}", fileVisitor);
        return fileVisitor;
    }

    @Override
    public Path getPath(String absolutePath) {
        // Define local variable to aid debugging.
        @SuppressWarnings("redundant")
        Path path = Paths.get(absolutePath);

        logger.trace("getPath({}) returned {}", absolutePath, path);
        return path;
    }

    @Override
    public PathEvent createPathEvent(Path path, BasicFileAttributes attrs) {
        PathEvent pathEvent;
        if (attrs.isRegularFile()) {
            logger.trace("createPathEvent({}, {}) - {} is a file path. Will create a {}.", path, attrs, path, FilePathEvent.class);
            pathEvent = new FilePathEvent(path, attrs);
        } else if (attrs.isDirectory()) {
            logger.trace("createPathEvent({}, {}) - {} is a directory path. Will create a {}.", path, attrs, path, DirectoryPathEvent.class);
            pathEvent = new DirectoryPathEvent(path, attrs);
        } else {
            logger.trace("createPathEvent({}, {}) - {} is a path type the application will not handle. Will create a {}.", path, attrs, path, UnhandledPathTypeEvent.class);
            pathEvent = new UnhandledPathTypeEvent(path, attrs);
        }
        logger.trace("createPathEvent({}, {}) returned {}", path, attrs, pathEvent);
        return pathEvent;
    }

    public PathEventPublisher getPathEventPublisher() {
        return null;
    }
}
