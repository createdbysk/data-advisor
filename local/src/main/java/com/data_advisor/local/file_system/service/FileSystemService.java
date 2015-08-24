package com.data_advisor.local.file_system.service;

import java.nio.file.FileVisitor;
import java.nio.file.Path;

/**
 * Provides file system related operations.
 */
public interface FileSystemService {
    void visitPath(Path path, FileVisitor<Path> fileVisitor);
}
