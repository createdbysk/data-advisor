package com.data_advisor.local.application;

import com.data_advisor.local.event.file_system.PathEvent;
import com.data_advisor.local.service.file_system.FileSystemService;

import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Abstract factory for AddHierarchy.
 */
public interface FileSystemAbstractFactory {
    FileSystemService getFileSystemService();
    FileVisitor<Path> createFileVisitor();
    Path getPath(String absolutePath);

    PathEvent createPathEvent(Path file, BasicFileAttributes attrs);
}
