package com.data_advisor.local.application.entry_point.impl;

import com.data_advisor.local.application.entry_point.FileSystemAbstractFactory;
import com.data_advisor.local.service.file_system.FileSystemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.nio.file.FileVisitor;
import java.nio.file.Path;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;

/**
 * Test for the LocalFileSystem class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Convention over configuration, this will automatically look for the nested @Configuration class.
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class LocalFileSystemTest {
    private static final String ABSOLUTE_PATH = "/absolute_path";

    // Use this instance to verify that the LocalFileSystem has the expected annotation to be able to
    // Autowire an instance of LocalFileSystem.
    // NOTE: Intellij does not detect that this class is Auto-wired.
    @Autowired
    private LocalFileSystem localFileSystemAutowired;

    // To use a spy in order to mock the createPath implementation in localFileSystem,
    // create an instance with @InjectMocks, which will inject the mocks. Then construct the spy in the
    // @Before method.
    @InjectMocks
    private LocalFileSystem localFileSystemWithInjectedMocks;
    private LocalFileSystem localFileSystem;

    @Mock
    private FileSystemAbstractFactory addHierarchyAbstractFactory;

    @Mock
    private FileVisitor<Path> addHierarchyVisitor;

    @Mock
    private FileSystemService fileSystemService;

    @Mock
    private Path path;

    @Configuration
    @ComponentScan("com.data_advisor.local.application.entry_point")
    static class TestConfiguration {
        // NOTE: Intellij Community Edition does not detect that the @Bean annotation causes
        // the spring framework to call these functions.
        // Create mocks to inject beans into the LocalFileSystem constructor.

        @Bean
        public FileSystemService fileSystemService() {
            return mock(FileSystemService.class);
        }

        @Bean
        public FileSystemAbstractFactory fileSystemAbstractFactory() {
            return mock(FileSystemAbstractFactory.class);
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        localFileSystem = spy(localFileSystemWithInjectedMocks);
        given(addHierarchyAbstractFactory.getFileVisitor()).willReturn(addHierarchyVisitor);
    }

    @Test
    public void testLocalFileSystem_CanAutowire() {
        assertNotNull(localFileSystemAutowired);
    }

    @Test
    public void testAddHierarchy() {
        // GIVEN
        final String absolutePath = ABSOLUTE_PATH;
        final FileVisitor<Path> addHierarchyVisitor = this.addHierarchyVisitor;
        final FileSystemService fileSystemService = this.fileSystemService;
        final Path path = this.path;

        willReturn(path).given(localFileSystem).createPath(absolutePath);

        // WHEN
        localFileSystem.addHierarchy(absolutePath);

        // THEN
        verify(fileSystemService, times(1)).visitPath(path, addHierarchyVisitor);
    }
}
