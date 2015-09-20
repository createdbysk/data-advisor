/**
 * Test application to determine the behavior of nio.
 */
package com.data_advisor.school.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

/**
 * The application entry point
 *
 * Test the behavior of {@link Files}.walkFileTree()
 */
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static class FileVisitor implements java.nio.file.FileVisitor<Path> {

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            logger.trace("preVisitDirectory: dir: {}, isDir: {}", dir, attrs.isDirectory());
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            logger.trace("visitFile: file: {}, isFile: {}, size: {}", file, attrs.isRegularFile(), attrs.size());
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            logger.trace("visitFileFailed: file: {}, exception: {}", file, exc);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            logger.trace("postVisitDirectory: dir: {}, exception: {}", dir, exc);
            return FileVisitResult.CONTINUE;
        }
    }

    public static void main(String[] args) {
        if (args == null || args.length <= 0) {
            System.out.println("Walks the tree rooted at the given root directory.");
            System.out.println("Usage: <executable> <root directory>");
            System.exit(1);
        }
        Path path = Paths.get(args[0]);
        FileVisitor fileVisitor = new FileVisitor();
        try {
            Files.walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), 1, fileVisitor);
        } catch(IOException ex) {
            logger.error("main: Exception occurred", ex);
        }
    }
}
