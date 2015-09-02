package com.data_advisor.local.application;

import com.data_advisor.local.service.file_system.FileSystemService;

import java.nio.file.FileVisitor;
import java.nio.file.Path;

/**
 * Abstract factory for AddHierarchy.
 */
public interface FileSystemAbstractFactory {
    FileSystemService getFileSystemService();
    FileVisitor<Path> getFileVisitor();
    Path createPath(String absolutePath);
}
