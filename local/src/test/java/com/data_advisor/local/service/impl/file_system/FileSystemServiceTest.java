package com.data_advisor.local.service.impl.file_system;

import com.data_advisor.local.application.ApplicationConfig;
import com.data_advisor.local.service.file_system.FileSystemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.*;

/**
 * Test for class RotaryFileSystemServiceImpl
 * Use the SpringJUnit4ClassRunner to auto-wire injected dependencies. This ensures that the dependencies don't miss
 * expected annotations. This satisfies Design Principle #2.
 *  2. Verify that the injected entities are configured correctly for dependency injection.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Verify that the ApplicationConfig injects the required objects and dependencies.
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ApplicationConfig.class})
public class FileSystemServiceTest {
    // Use this instance to verify that the FileSystemServiceImpl has the expected annotation to be able to
    // Autowire an instance of FileSystemService.
    // NOTE: Intellij does not detect that this class is Auto-wired.
    @Autowired
    private FileSystemService fileSystemServiceAutoWired;

    // To use a spy in order to mock the walkFileTree implementation in fileSystemService,
    // create an instance with @InjectMocks, which will inject the mocks. Then construct the spy in the constructor.
    @InjectMocks
    private FileSystemServiceImpl fileSystemServiceInjectMocks;
    private FileSystemServiceImpl fileSystemService;

    @Mock
    private Logger logger;

    @Mock
    private Path path;

    private Set<FileVisitOption> options;

    @Mock
    private FileVisitor<Path> fileVisitor;

    @Mock
    private BasicFileAttributes basicFileAttributes;

    @Configuration
    @ComponentScan(basePackages = {"com.data_advisor.local.service.impl.file_system"})
    static class TestConfiguration {
    }

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        fileSystemService = spy(fileSystemServiceInjectMocks);
        options = EnumSet.noneOf(FileVisitOption.class);
    }

    @Test
    public void testFileSystemService_CanAutowire() {
        assertNotNull(fileSystemServiceAutoWired);
    }

    @Test
    public void testListDirectory_WhenNoErrors_CompletesSuccessfully() throws IOException {
        // GIVEN
        // ... see common declarations.
        willDoNothing().given(fileSystemService).walkFileTree(path, options, 1, fileVisitor);

        // WHEN
        fileSystemService.visitPath(path, fileVisitor);

        // THEN
        verify(fileSystemService, times(1)).walkFileTree(path, options, 1, fileVisitor);
        verify(logger, times(1)).trace(anyString(), eq(path), eq(fileVisitor));
    }

    @Test
    public void testListDirectory_WhenWalkFileTreeGeneratesUnexpectedException_LogsAnError() throws IOException {
        // GIVEN
        // ... see common declarations.
        IOException expectedException = new IOException();
        willThrow(expectedException).given(fileSystemService).walkFileTree(path, options, 1, fileVisitor);

        // WHEN
        fileSystemService.visitPath(path, fileVisitor);

        // THEN
        verify(fileSystemService, times(1)).walkFileTree(path, options, 1, fileVisitor);
        verify(logger, times(1)).warn(anyString(), eq(expectedException));
    }
}
