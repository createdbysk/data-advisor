package com.data_advisor.local.application.entry_point.impl;

/**
 * Test for the LocalFileSystemFactory.
 */

import com.data_advisor.local.application.ApplicationConfig;
import com.data_advisor.local.application.FileSystemAbstractFactory;
import com.data_advisor.local.service.impl.file_system.FileSystemServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.Paths;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

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

    @Test
    public void testLocalFileSystemFactory_CanAutowire() {
        assertNotNull(localFileSystemFactoryAutowired);
    }

    @Test
    public void test_getFileSystemService() {
        assertTrue(localFileSystemFactoryAutowired.getFileSystemService() instanceof FileSystemServiceImpl);
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
}
