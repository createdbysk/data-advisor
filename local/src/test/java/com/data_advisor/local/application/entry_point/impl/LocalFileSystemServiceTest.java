package com.data_advisor.local.application.entry_point.impl;

import com.data_advisor.infrastructure.UniqueIdGenerator;
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
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.*;

/**
 * Test for class {@link LocalFileSystemServiceImpl}
 * Use the SpringJUnit4ClassRunner to auto-wire injected dependencies. This ensures that the dependencies don't miss
 * expected annotations. This satisfies Design Principle #2.
 *  2. Verify that the injected entities are configured correctly for dependency injection.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Verify that the ApplicationConfig injects the required objects and dependencies.
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ApplicationConfig.class})
public class LocalFileSystemServiceTest {
    private static final int MAX_DEPTH = Integer.MAX_VALUE;
    private static final String UUID_FOR_FAILED_MD5 = "de305d54-75b4-431b-adb2-eb6b9e546014";

    // Use this instance to verify that the LocalFileSystemServiceImpl has the expected annotation to be able to
    // Autowire an instance of FileSystemService.
    // NOTE: Intellij does not detect that this class is Auto-wired.
    @Autowired
    private FileSystemService fileSystemServiceAutoWired;

    // To use a spy in order to mock the walkFileTree implementation in fileSystemService,
    // create an instance with @InjectMocks, which will inject the mocks. Then construct the spy in the constructor.
    @InjectMocks
    private LocalFileSystemServiceImpl fileSystemServiceInjectMocks;
    private LocalFileSystemServiceImpl fileSystemService;

    @Mock
    private Logger logger;

    @Mock
    private Path path;

    private Set<FileVisitOption> options;

    @Mock
    private FileVisitor<Path> fileVisitor;

    @Mock
    private BasicFileAttributes basicFileAttributes;

    @Mock
    private UniqueIdGenerator uniqueIdGenerator;

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
        willDoNothing().given(fileSystemService).walkFileTree(path, options, MAX_DEPTH, fileVisitor);

        // WHEN
        fileSystemService.visitPath(path, fileVisitor);

        // THEN
        verify(fileSystemService, times(1)).walkFileTree(path, options, MAX_DEPTH, fileVisitor);
        verify(logger, times(1)).trace(anyString(), eq(path), eq(fileVisitor));
    }

    @Test
    public void testListDirectory_WhenWalkFileTreeGeneratesUnexpectedException_LogsAnError() throws IOException {
        // GIVEN
        // ... see common declarations.
        IOException expectedException = new IOException();
        willThrow(expectedException).given(fileSystemService).walkFileTree(path, options, MAX_DEPTH, fileVisitor);

        // WHEN
        fileSystemService.visitPath(path, fileVisitor);

        // THEN
        verify(fileSystemService, times(1)).walkFileTree(path, options, MAX_DEPTH, fileVisitor);
        verify(logger, times(1)).warn(anyString(), eq(expectedException));
    }

    @Test
    public void testFileSystemService_GivenValidFilePath_WhencomputeMd5Hash_ThenReturnMd5HashOfFile() throws FileNotFoundException {
        // GIVEN
        // A neutral app computed this value.
        final String expectedMd5Hash = "3E0782C5C22A2AC3FA160C93FD130AC2";
        final File file = ResourceUtils.getFile("classpath:com/data_advisor/local/application/entry_point/impl/test_input_file.txt");
        final Path path = file.toPath();
        final FileSystemService fileSystemService = this.fileSystemService;

        // WHEN
        final String computedMd5Hash = fileSystemService.computeMd5Hash(path);

        // THEN
        // Change both values to lower case
        assertEquals(expectedMd5Hash.toLowerCase(), computedMd5Hash.toLowerCase());
    }

    /** Test for the case where the computeMd5Hash generates an IOException. In most cases,
     * this will happen because the file system denied access to the file. In those cases,
     * set the md5 hash to be an invalid value. To make this value unique, use a guid.
     */
    @Test
    public void testFileSystemService_GivenAccessToFileThrowsIOException_WhencomputeMd5Hash_ThenReturnGeneratedGUID() {
        // GIVEN
        final Path path = Paths.get("ThisFile.DoesNotExist");
        final String nonExistentFilePath = path.toAbsolutePath().normalize().toString();
        final FileSystemService fileSystemService = this.fileSystemService;
        final String uuidString = UUID_FOR_FAILED_MD5;
        final UniqueIdGenerator uniqueIdGenerator = this.uniqueIdGenerator;
        given(uniqueIdGenerator.generate()).willReturn(uuidString);

        // WHEN
        final String computedMd5Hash = fileSystemService.computeMd5Hash(path);

        // THEN
        final String message = String.format("computeMd5Hash(%s) threw an exception - ", nonExistentFilePath);
        verify(logger, times(1)).warn(eq(message), any(IOException.class));
        assertEquals(uuidString, computedMd5Hash);
    }
}
