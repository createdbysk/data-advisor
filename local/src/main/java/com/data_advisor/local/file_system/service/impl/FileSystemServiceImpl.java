package com.data_advisor.local.file_system.service.impl;

import com.data_advisor.local.file_system.service.FileSystemService;
import org.apache.storm.guava.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Set;

/**
 * Implements FileSystemService
 */
@Service
public class FileSystemServiceImpl implements FileSystemService {
    @Override
    public void visitPath(Path path, FileVisitor<Path> fileVisitor) {
        try {
            walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), 1, fileVisitor);
        } catch (IOException e) {
            // TODO: Log exception to eliminate warning.
        }
    }

    /** Convenience method to be able to stub Files.walkFileTree() without Powermockito or similar library */
    @VisibleForTesting
    void walkFileTree(Path path, Set<FileVisitOption> options, int maxDepth, FileVisitor<Path> fileVisitor) throws IOException {
        Files.walkFileTree(path, options, maxDepth, fileVisitor);
    }
}
