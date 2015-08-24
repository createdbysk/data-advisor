package com.data_advisor.local.file_system.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Set;

import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

/**
 * Test for class RotaryFileSystemServiceImpl
 * Use the SpringJUnit4ClassRunner to auto-wire injected dependencies. This ensures that the dependencies don't miss
 * expected annotations. This satisfies Design Principle #2.
 *  2. Verify that the injected entities are configured correctly for dependency injection.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Convention over configuration, this will automatically look for the nested @Configuration class.
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class FileSystemServiceTest {
    // To use a spy in order to mock the walkFileTree implementation in fileSystemService,
    // create an instance with @Autowired, which will inject the mocks. Then construct the spy in the constructor.
    // NOTE: Intellij does not detect that this class is Auto-wired.
    @Autowired
    private FileSystemServiceImpl fileSystemServiceAutoWired;
    private FileSystemServiceImpl fileSystemService;

    @Mock
    private FileVisitor<Path> fileVisitor;

    @Mock
    private Path path;

    private Set<FileVisitOption> options;

    @Configuration
    @ComponentScan(basePackages = {"com.data_advisor.local.file_system.service.impl"})
    static class TestConfiguration {
    }

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        fileSystemService = spy(fileSystemServiceAutoWired);
        options = EnumSet.noneOf(FileVisitOption.class);

        willDoNothing().given(fileSystemService).walkFileTree(path, options, 1, fileVisitor);
    }

    @Test
    public void testListDirectoriesAndFiles() throws IOException {
        // WHEN
        fileSystemService.visitPath(path, fileVisitor);

        // THEN
        verify(fileSystemService, times(1)).walkFileTree(path, options, 1, fileVisitor);
    }
}
