package com.data_advisor.local.application.entry_point.impl;

/**
 * Test for the LocalFileSystemFactory.
 */

import com.data_advisor.local.application.ApplicationConfig;
import com.data_advisor.local.application.FileSystemAbstractFactory;
import com.data_advisor.local.event.file_system.DirectoryPathEvent;
import com.data_advisor.local.event.file_system.FilePathEvent;
import com.data_advisor.local.event.file_system.PathEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

/**
 * Test for the LocalFileSystem class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Convention over configuration, this will automatically look for the nested @Configuration class.
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ApplicationConfig.class})
public class LocalFileSystemFactoryTest {
    private static final String ABSOLUTE_PATH = "/path";
    @Autowired
    private FileSystemAbstractFactory localFileSystemFactoryAutowired;

    @InjectMocks
    private LocalFileSystemFactory localFileSystemFactory;

    @Mock
    private BasicFileAttributes fileAttributes;

    @Mock
    private BasicFileAttributes directoryAttributes;

    @Mock
    private Path path;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

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
    }

    @Test
    public void test_getFileSystemService() {
        assertTrue(localFileSystemFactoryAutowired.getFileSystemService() instanceof LocalFileSystemServiceImpl);
    }

    @Test
    public void test_getFileVisitor() {
        FileVisitor<Path> fileVisitorA = localFileSystemFactoryAutowired.createFileVisitor();
        FileVisitor<Path> fileVisitorB = localFileSystemFactoryAutowired.createFileVisitor();
        assertTrue(fileVisitorA instanceof LocalFileSystemVisitor);
        // Verify that createFileVisitor() returns a different instance for each call.
        assertNotSame(fileVisitorA, fileVisitorB);
    }

    @Test
    public void test_getPath() {
        final Path expectedPath = Paths.get(ABSOLUTE_PATH);
        assertEquals(expectedPath, localFileSystemFactoryAutowired.getPath(ABSOLUTE_PATH));
    }

    @Test
    public void test_GivenFile_WhenCreatePathEvent_WillReturnFilePathEvent() {
        // GIVEN
        final BasicFileAttributes attrs = this.fileAttributes;
        final Path path = this.path;
        final FileSystemAbstractFactory fileSystemAbstractFactory = this.localFileSystemFactory;

        // WHEN
        PathEvent pathEvent = fileSystemAbstractFactory.createPathEvent(path, attrs);

        // THEN
        assertTrue(pathEvent instanceof FilePathEvent);
    }

    @Test
    public void test_GivenDirectory_WhenCreatePathEvent_WillReturnDirectoryPathEvent() {
        // GIVEN
        final BasicFileAttributes attrs = this.directoryAttributes;
        final Path path = this.path;
        final FileSystemAbstractFactory fileSystemAbstractFactory = this.localFileSystemFactory;

        // WHEN
        PathEvent pathEvent = fileSystemAbstractFactory.createPathEvent(path, attrs);

        // THEN
        assertTrue(pathEvent instanceof DirectoryPathEvent);
    }
}
