package com.data_advisor.local.application.entry_point.impl;

import com.data_advisor.local.application.FileSystemAbstractFactory;
import com.data_advisor.local.service.file_system.FileSystemService;
import org.springframework.stereotype.Component;

import java.nio.file.FileVisitor;
import java.nio.file.Path;

/**
 * FileSystemAbstractFactory implementation for local file system.
 */
@Component
public class LocalFileSystemFactory implements FileSystemAbstractFactory {
    @Override
    public FileSystemService getFileSystemService() {
        return null;
    }

    @Override
    public FileVisitor<Path> getFileVisitor() {
        return null;
    }

    @Override
    public Path createPath(String absolutePath) {
        return null;
    }
}
