package com.data_advisor.local.service.impl.file_system;

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
public class FileSystemServiceImpl implements FileSystemService {
    private static final int MAX_DEPTH = 1;
    private Logger logger = LoggerFactory.getLogger(FileSystemServiceImpl.class);

    @Override
    public void visitPath(Path path, FileVisitor<Path> fileVisitor) {
        logger.trace("visitPath({}, {})", path, fileVisitor);
        try {
            walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), MAX_DEPTH, fileVisitor);
        } catch (IOException e) {
           logger.warn("walkFileTree threw an unexpected exception", e);
        }
    }

    /** Convenience method to be able to stub Files.walkFileTree() without Powermockito or similar library */
    @VisibleForTesting
    void walkFileTree(Path path, Set<FileVisitOption> options, int maxDepth, FileVisitor<Path> fileVisitor) throws IOException {
        Files.walkFileTree(path, options, maxDepth, fileVisitor);
    }
}
