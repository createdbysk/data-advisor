package com.data_advisor.local.service.file_system;

import java.nio.file.FileVisitor;
import java.nio.file.Path;

/**
 * Provides file system related operations.
 */
public interface FileSystemService {
    /**
     * Visit the path and any sub-paths and invoke the appropriate methods on the fileVisitor.
     * @param path              File or directory path to visit.
     * @param fileVisitor       The visitor.
     */
    void visitPath(Path path, FileVisitor<Path> fileVisitor);

    // TODO: Implement the test for this method.
    /**
     * Compute the MD5 hash of the given file.
     * @param filePath  The path of the file.
     * @return  The MD5 hash of the file contents.
     */
    String computeMd5Hash(Path filePath);
}
