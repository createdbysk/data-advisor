package com.data_advisor.local.application.entry_point.impl;

import com.data_advisor.local.application.ApplicationConfig;
import com.data_advisor.local.application.FileSystemAbstractFactory;
import com.data_advisor.local.service.file_system.FileSystemService;
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

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test the Application class.
 * Use the SpringJUnit4ClassRunner to auto-wire injected dependencies. This ensures that the dependencies don't miss
 * expected annotations. This satisfies Design Principle #2.
 *  2. Verify that the injected entities are configured correctly for dependency injection.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Verify that the ApplicationConfig injects the required objects and dependencies.
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ApplicationConfig.class})
public class LocalFileSystemTest {
    private static final String ABSOLUTE_PATH = "/absolute_path";

    // Use this instance to verify that the LocalFileSystem has the expected annotation to be able to
    // Autowire an instance of LocalFileSystem.
    // NOTE: Intellij does not detect that this class is Auto-wired.
    @Autowired
    private LocalFileSystem localFileSystemAutowired;

    @InjectMocks
    private LocalFileSystem localFileSystem;

    @Mock
    private FileSystemAbstractFactory fileSystemAbstractFactory;

    @Mock
    private FileVisitor<Path> addHierarchyVisitor;

    @Mock
    private FileSystemService fileSystemService;

    @Mock
    private Path path;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        given(fileSystemAbstractFactory.getFileSystemService()).willReturn(fileSystemService);
        given(fileSystemAbstractFactory.createFileVisitor()).willReturn(addHierarchyVisitor);
        given(fileSystemAbstractFactory.getPath(ABSOLUTE_PATH)).willReturn(path);
    }

    @Test
    public void testLocalFileSystem_CanAutowire() {
        assertNotNull(localFileSystemAutowired);
        assertNotNull(localFileSystemAutowired.fileSystemAbstractFactory);
    }

    @Test
    public void testAddHierarchy() {
        // GIVEN
        final String absolutePath = ABSOLUTE_PATH;
        final FileVisitor<Path> addHierarchyVisitor = this.addHierarchyVisitor;
        final FileSystemService fileSystemService = this.fileSystemService;
        final Path path = this.path;

        // WHEN
        localFileSystem.addHierarchy(absolutePath);

        // THEN
        verify(fileSystemService, times(1)).visitPath(path, addHierarchyVisitor);
    }
}
