package com.data_advisor.local.application.entry_point.impl;

import com.data_advisor.local.service.file_system.FileSystemService;
import org.apache.storm.guava.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LocalFileSystemServiceImpl implements FileSystemService {
    private static final int MAX_DEPTH = 1;
    private Logger logger = LoggerFactory.getLogger(LocalFileSystemServiceImpl.class);

    @Override
    public void visitPath(Path path, FileVisitor<Path> fileVisitor) {
        logger.trace("visitPath({}, {})", path, fileVisitor);
        try {
            walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), MAX_DEPTH, fileVisitor);
        } catch (IOException e) {
           logger.warn("walkFileTree threw an unexpected exception", e);
        }
    }

    @Override
    public String computeMd5Hash(Path filePath) {
        return null;
    }

    /**
     * Convenience method to be able to stub Files.walkFileTree() without Powermockito or similar library
     *
     * This method will NOT have a unit test. It will appear as not covered in code-coverage.
     * */
    @VisibleForTesting
    void walkFileTree(Path path, Set<FileVisitOption> options, int maxDepth, FileVisitor<Path> fileVisitor) throws IOException {
        Files.walkFileTree(path, options, maxDepth, fileVisitor);
    }
}
