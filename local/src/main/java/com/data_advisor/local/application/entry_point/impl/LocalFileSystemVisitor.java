package com.data_advisor.local.application.entry_point.impl;

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
        return null;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        return null;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return null;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return null;
    }
}
