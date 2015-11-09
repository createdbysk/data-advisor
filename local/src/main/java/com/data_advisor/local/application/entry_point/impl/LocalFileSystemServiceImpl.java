package com.data_advisor.local.application.entry_point.impl;

import com.data_advisor.local.service.file_system.FileSystemService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.storm.guava.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
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
    private static final int MAX_DEPTH = Integer.MAX_VALUE;
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

    /**
     * @see <a href="http://www.codejava.net/coding/how-to-calculate-md5-and-sha-hash-values-in-java">Calculate MD5</a>
     * @param filePath  The path of the file.
     * @return the md5 hash of the file.
     */
    @Override
    public String computeMd5Hash(Path filePath) {
        String filePathString = filePath.toAbsolutePath().normalize().toString();
        try {
            FileInputStream fis = new FileInputStream(filePathString);
            BufferedInputStream bis = new BufferedInputStream(fis);
            return DigestUtils.md5Hex(bis);
        } catch (IOException e) {
            logger.warn(String.format("computeMd5Hash(%s) threw an exception - ", filePathString), e);
            throw new RuntimeException(e);
        }
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
