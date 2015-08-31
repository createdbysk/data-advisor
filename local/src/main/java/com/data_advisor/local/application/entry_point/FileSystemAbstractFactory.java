package com.data_advisor.local.application.entry_point;

import java.nio.file.FileVisitor;
import java.nio.file.Path;

/**
 * Abstract factory for AddHierarchy.
 */
public interface FileSystemAbstractFactory {
    FileVisitor<Path> getFileVisitor();
}
