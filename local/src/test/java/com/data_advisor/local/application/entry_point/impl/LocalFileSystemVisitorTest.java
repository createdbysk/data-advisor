package com.data_advisor.local.application.entry_point.impl;

import com.data_advisor.local.application.FileSystemAbstractFactory;
import com.data_advisor.local.event.file_system.PathEvent;
import com.data_advisor.local.event.file_system.PathEventPublisher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test class LocalSystemVisitor
 */
public class LocalFileSystemVisitorTest {
    private LocalFileSystemVisitor localFileSystemVisitor;

    @Mock
    private FileSystemAbstractFactory fileSystemAbstractFactory;

    @Mock
    private Path path;

    @Mock
    private BasicFileAttributes attrs;

    @Mock
    private Logger logger;

    @Mock
    private PathEvent pathEvent;

    @Mock
    private PathEventPublisher pathEventPublisher;

    private IOException ioException;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        localFileSystemVisitor = new LocalFileSystemVisitor(fileSystemAbstractFactory);
        localFileSystemVisitor.logger = logger;
        ioException = new IOException();
    }

    @Test
    public void test_WhenPreVisitDirectory_ThenLogAndContinue() throws IOException {
        // GIVEN
        final FileVisitor<Path> fileVisitor = localFileSystemVisitor;
        final Path dir = this.path;
        final BasicFileAttributes attrs = this.attrs;
        final FileVisitResult expectedFileVisitResult = FileVisitResult.CONTINUE;
        given(attrs.isDirectory()).willReturn(true);

        // WHEN
        FileVisitResult fileVisitResult = fileVisitor.preVisitDirectory(dir, attrs);

        // THEN
        assertEquals(expectedFileVisitResult, fileVisitResult);
        verify(logger, times(1)).trace("preVisitDirectory({}, {})", dir, attrs);
    }

    @Test
    public void test_WhenVisitFile_ThenPublishEventAndLogAndContinue() throws IOException {
        // GIVEN
        final FileVisitor<Path> fileVisitor = localFileSystemVisitor;
        final Path file = this.path;
        final BasicFileAttributes attrs = this.attrs;
        final FileVisitResult expectedFileVisitResult = FileVisitResult.CONTINUE;
        given(fileSystemAbstractFactory.createPathEvent(file, attrs)).willReturn(pathEvent);
        given(fileSystemAbstractFactory.getPathEventPublisher()).willReturn(pathEventPublisher);

        // WHEN
        FileVisitResult fileVisitResult = fileVisitor.visitFile(file, attrs);

        // THEN
        assertEquals(expectedFileVisitResult, fileVisitResult);
        verify(logger, times(1)).trace("visitFile({}, {})", file, attrs);
        verify(pathEventPublisher, times(1)).publish(pathEvent);
    }

    @Test
    public void test_WhenVisitFileFailed_ThenLogAndContinue() throws IOException {
        // GIVEN
        final FileVisitor<Path> fileVisitor = localFileSystemVisitor;
        final Path file = this.path;
        final IOException ioException = this.ioException;
        final FileVisitResult expectedFileVisitResult = FileVisitResult.CONTINUE;

        // WHEN
        FileVisitResult fileVisitResult = fileVisitor.visitFileFailed(file, ioException);

        // THEN
        assertEquals(expectedFileVisitResult, fileVisitResult);
        verify(logger, times(1)).trace("visitFileFailed({}, {})", file, ioException);
    }

    @Test
    public void test_WhenPostVisitDirectory_ThenLogAndContinue() throws IOException {
        // GIVEN
        final FileVisitor<Path> fileVisitor = localFileSystemVisitor;
        final Path dir = this.path;
        final IOException ioException = this.ioException;
        final FileVisitResult expectedFileVisitResult = FileVisitResult.CONTINUE;
        given(attrs.isDirectory()).willReturn(true);

        // WHEN
        FileVisitResult fileVisitResult = fileVisitor.postVisitDirectory(dir, ioException);

        // THEN
        assertEquals(expectedFileVisitResult, fileVisitResult);
        verify(logger, times(1)).trace("postVisitDirectory({}, {})", dir, ioException);
    }
}
