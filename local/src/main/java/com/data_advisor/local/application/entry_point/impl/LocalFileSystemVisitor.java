package com.data_advisor.local.application.entry_point.impl;

import com.data_advisor.local.event.file_system.PathEvent;
import com.data_advisor.local.event.file_system.PathEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Implementation of {@link FileVisitor} for the LocalFileSystem.
 */
public class LocalFileSystemVisitor implements FileVisitor<Path> {
    private final LocalFileSystemFactory localFileSystemFactory;

    // Allow the test to set the logger to expect logging.
    Logger logger = LoggerFactory.getLogger(LocalFileSystemVisitor.class);

    /**
     * package scoped constructor to enforce that only
     * LocalFileSystemFactory should instantiate this object.
     *
     * @param localFileSystemFactory The factory instance that created this visitor.
     */
    LocalFileSystemVisitor(LocalFileSystemFactory localFileSystemFactory) {
        this.localFileSystemFactory = localFileSystemFactory;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        logger.trace("preVisitDirectory({}, {})", dir, attrs);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        logger.trace("visitFile({}, {})", file, attrs);
        // Publish the pathEvent to allow listeners to hook into the walk.
        PathEvent pathEvent = localFileSystemFactory.createPathEvent(file, attrs);
        PathEventPublisher pathEventPublisher = localFileSystemFactory.getPathEventPublisher();
        pathEventPublisher.publish(pathEvent);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        logger.trace("visitFileFailed({}, {})", file, exc);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        logger.trace("postVisitDirectory({}, {})", dir, exc);
        return FileVisitResult.CONTINUE;
    }
}
