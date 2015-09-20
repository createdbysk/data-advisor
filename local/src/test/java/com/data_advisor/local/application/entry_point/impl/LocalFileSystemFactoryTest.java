package com.data_advisor.local.application.entry_point.impl;

/**
 * Test for the LocalFileSystemFactory.
 */

import com.data_advisor.local.application.ApplicationConfig;
import com.data_advisor.local.application.FileSystemAbstractFactory;
import com.data_advisor.local.event.file_system.PathEvent;
import com.data_advisor.local.event.file_system.impl.DirectoryPathEvent;
import com.data_advisor.local.event.file_system.impl.FilePathEvent;
import com.data_advisor.local.event.file_system.impl.PathEventPublisherImpl;
import com.data_advisor.local.event.file_system.impl.UnhandledPathTypeEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test for the {@link LocalFileSystem} class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Convention over configuration, this will automatically look for the nested @Configuration class.
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ApplicationConfig.class})
public class LocalFileSystemFactoryTest {
    private static final String ABSOLUTE_PATH = "/path";

    // Use this instance to verify that the LocalFileSystemFactory has the expected annotation to be able to
    // Autowire an instance of FileSystemAbstractFactory.
    // NOTE: Intellij does not detect that this class is Auto-wired.
    @Autowired
    private FileSystemAbstractFactory localFileSystemFactoryAutowired;

    @InjectMocks
    private LocalFileSystemFactory localFileSystemFactory;

    @Mock
    private BasicFileAttributes fileAttributes;

    @Mock
    private BasicFileAttributes directoryAttributes;

    @Mock
    private BasicFileAttributes unknownPathTypeAttributes;

    @Mock
    private Path path;

    @Mock
    private Logger logger;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // LocalFileSystemFactory does not have a default constructor. This test will use constructor
        // injection to inject the dependencies of LocalFileSystemFactory. Explicitly set the logger
        // so that the logger does not have to be a constructor parameter.
        localFileSystemFactory.logger = this.logger;

        given(fileAttributes.isRegularFile()).willReturn(true);
        given(fileAttributes.isDirectory()).willReturn(false);
        given(fileAttributes.isSymbolicLink()).willReturn(false);

        given(directoryAttributes.isRegularFile()).willReturn(false);
        given(directoryAttributes.isDirectory()).willReturn(true);
        given(directoryAttributes.isSymbolicLink()).willReturn(false);
    }

    @Test
    public void testLocalFileSystemFactory_CanAutowire() {
        assertNotNull(localFileSystemFactoryAutowired);
        assertTrue(localFileSystemFactoryAutowired.getFileSystemService() instanceof LocalFileSystemServiceImpl);
        assertTrue(localFileSystemFactoryAutowired.getPathEventPublisher() instanceof PathEventPublisherImpl);
        assertTrue(localFileSystemFactoryAutowired.getFilesGroupedBySize() instanceof HashMap);
    }

    @Test
    public void test_getFileVisitor() {
        // GIVEN
        final LocalFileSystemFactory localFileSystemFactory = this.localFileSystemFactory;
        final Logger logger = this.logger;

        // WHEN
        FileVisitor<Path> fileVisitorA = localFileSystemFactory.createFileVisitor();
        FileVisitor<Path> fileVisitorB = localFileSystemFactory.createFileVisitor();

        // THEN
        assertTrue(fileVisitorA instanceof LocalFileSystemVisitor);
        // Verify that createFileVisitor() returns a different instance for each call.
        assertNotSame(fileVisitorA, fileVisitorB);

        verify(logger, times(1)).trace("createFileVisitor() returned {}", fileVisitorA);
        verify(logger, times(1)).trace("createFileVisitor() returned {}", fileVisitorB);
    }

    @Test
    public void test_getPath() {
        // GIVEN
        final LocalFileSystemFactory localFileSystemFactory = this.localFileSystemFactory;
        final String pathString = ABSOLUTE_PATH;
        final Path expectedPath = Paths.get(pathString);
        final Logger logger = this.logger;

        // WHEN
        Path actualPath = localFileSystemFactory.getPath(pathString);

        // THEN
        assertEquals(expectedPath, actualPath);
        verify(logger, times(1)).trace("getPath({}) returned {}", pathString, actualPath);
    }

    @Test
    public void test_GivenFile_WhenCreatePathEvent_WillReturnFilePathEvent() {
        // GIVEN
        final BasicFileAttributes attrs = this.fileAttributes;
        final Path path = this.path;
        final FileSystemAbstractFactory fileSystemAbstractFactory = this.localFileSystemFactory;
        final Logger logger = this.logger;

        // WHEN
        PathEvent pathEvent = fileSystemAbstractFactory.createPathEvent(path, attrs, this);

        // THEN
        assertTrue(pathEvent instanceof FilePathEvent);
        verify(logger, times(1)).trace("createPathEvent({}, {}) - {} is a file path. Will create a {}.", path, attrs, path, FilePathEvent.class);
        verify(logger, times(1)).trace("createPathEvent({}, {}) returned {}", path, attrs, pathEvent);
    }

    @Test
    public void test_GivenDirectory_WhenCreatePathEvent_WillReturnDirectoryPathEvent() {
        // GIVEN
        final BasicFileAttributes attrs = this.directoryAttributes;
        final Path path = this.path;
        final FileSystemAbstractFactory fileSystemAbstractFactory = this.localFileSystemFactory;
        final Logger logger = this.logger;

        // WHEN
        PathEvent pathEvent = fileSystemAbstractFactory.createPathEvent(path, attrs, this);

        // THEN
        assertTrue(pathEvent instanceof DirectoryPathEvent);
        verify(logger, times(1)).trace("createPathEvent({}, {}) - {} is a directory path. Will create a {}.", path, attrs, path, DirectoryPathEvent.class);
        verify(logger, times(1)).trace("createPathEvent({}, {}) returned {}", path, attrs, pathEvent);
    }

    @Test
    public void test_GivenUnknownPathType_WhenCreatePathEvent_WillCreateUnknownPathTypeEvent() {
        // GIVEN
        final BasicFileAttributes attrs = this.unknownPathTypeAttributes;
        final Path path = this.path;
        final FileSystemAbstractFactory fileSystemAbstractFactory = this.localFileSystemFactory;
        final Logger logger = this.logger;

        // WHEN
        PathEvent pathEvent = fileSystemAbstractFactory.createPathEvent(path, attrs, this);

        // THEN
        assertTrue(pathEvent instanceof UnhandledPathTypeEvent);
        verify(logger, times(1)).trace("createPathEvent({}, {}) - {} is a path type the application will not handle. Will create a {}.", path, attrs, path, UnhandledPathTypeEvent.class);
        verify(logger, times(1)).trace("createPathEvent({}, {}) returned {}", path, attrs, pathEvent);
    }

    @Test
    public void testCreateFileGroup() {
        // GIVEN
        final Logger logger = this.logger;
        final FileSystemAbstractFactory fileSystemAbstractFactory = this.localFileSystemFactory;

        // WHEN
        List<FilePathEvent> fileGroup = fileSystemAbstractFactory.createFileGroup();

        // THEN
        assertTrue(fileGroup instanceof ArrayList);
        verify(logger, times(1)).trace("createFileGroup() - return {}", fileGroup);
     }
}
