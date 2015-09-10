package com.data_advisor.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Use this class to compose FileVisitor functionality.
 *
 * This class uses the builder pattern to create a file visitor chain, which uses the composite pattern
 * to compose the constituent FileVisitor implementations.
 */
@Component
public class FileVisitorChainBuilder {
    Logger logger = LoggerFactory.getLogger(FileVisitorChainBuilder.class);

    public FileVisitor<Path> create() {
        return new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                logger.trace("preVisitDirectory({}, {})", dir, attrs);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                logger.trace("visitFile({}, {})", file, attrs);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                logger.trace("visitFileFailed({}, {})", file, exc);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                logger.trace("postVisitDirectory({}, {})", dir, exc);
                return FileVisitResult.CONTINUE;
            }
        };
    }
}
