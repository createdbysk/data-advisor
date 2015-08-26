package com.data_advisor.local.service.file_system;

import java.nio.file.FileVisitor;
import java.nio.file.Path;

/**
 * Provides file system related operations.
 */
public interface FileSystemService {
    void listDirectoryContents(Path path, FileVisitor<Path> fileVisitor);
}
