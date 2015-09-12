package com.data_advisor.local.application.entry_point.impl;

import com.data_advisor.local.application.FileSystemAbstractFactory;
import com.data_advisor.local.event.file_system.DirectoryPathEvent;
import com.data_advisor.local.event.file_system.FilePathEvent;
import com.data_advisor.local.event.file_system.PathEvent;
import com.data_advisor.local.event.file_system.PathEventPublisher;
import com.data_advisor.local.service.file_system.FileSystemService;
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
    public PathEvent createPathEvent(Path path, BasicFileAttributes attrs) {
        if (attrs.isRegularFile()) {
            return new FilePathEvent(path, attrs);
        } else if (attrs.isDirectory()) {
            return new DirectoryPathEvent(path, attrs);
        }
        // TODO: Log an warning that the path was to an unknown type.
        return null;
    }

    public PathEventPublisher getPathEventPublisher() {
        return null;
    }
}
