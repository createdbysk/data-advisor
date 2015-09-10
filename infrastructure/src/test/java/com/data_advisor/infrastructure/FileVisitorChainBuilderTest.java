package com.data_advisor.infrastructure;

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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test FileVisitorChainBuilder
 * Use the SpringJUnit4ClassRunner to auto-wire injected dependencies. This ensures that the dependencies don't miss
 * expected annotations. This satisfies Design Principle #2.
 *  2. Verify that the injected entities are configured correctly for dependency injection.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Verify that the ApplicationConfig injects the required objects and dependencies.
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {InfrastructureConfig.class})
public class FileVisitorChainBuilderTest {
    @Autowired
    private FileVisitorChainBuilder fileVisitorChainBuilderAutowired;

    @InjectMocks
    private FileVisitorChainBuilder fileVisitorChainBuilder;

    @Mock
    private Path path;

    @Mock
    private BasicFileAttributes attrs;

    @Mock
    private Logger logger;

    private IOException ioException;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ioException = new IOException();
    }

    @Test
    public void testFileVisitorChainBuilder_CanAutowire() {
        assertNotNull(fileVisitorChainBuilderAutowired);
    }

    @Test
    public void test_WhenPreVisitDirectory_ThenLogAndContinue() throws IOException {
        // GIVEN
        final FileVisitorChainBuilder fileVisitorChainBuilder = this.fileVisitorChainBuilder;
        final Path dir = this.path;
        final BasicFileAttributes attrs = this.attrs;
        final FileVisitResult expectedFileVisitResult = FileVisitResult.CONTINUE;
        given(this.attrs.isDirectory()).willReturn(true);

        // WHEN
        FileVisitor<Path> fileVisitor = fileVisitorChainBuilder.create();
        FileVisitResult fileVisitResult = fileVisitor.preVisitDirectory(dir, attrs);

        // THEN
        assertEquals(expectedFileVisitResult, fileVisitResult);
        verify(logger, times(1)).trace("preVisitDirectory({}, {})", dir, attrs);
    }

    @Test
    public void test_WhenVisitFile_ThenLogAndContinue() throws IOException {
        // GIVEN
        final FileVisitorChainBuilder fileVisitorChainBuilder = this.fileVisitorChainBuilder;
        final Path file = this.path;
        final BasicFileAttributes attrs = this.attrs;
        final FileVisitResult expectedFileVisitResult = FileVisitResult.CONTINUE;
        given(this.attrs.isRegularFile()).willReturn(true);

        // WHEN
        FileVisitor<Path> fileVisitor = fileVisitorChainBuilder.create();
        FileVisitResult fileVisitResult = fileVisitor.visitFile(file, attrs);

        // THEN
        assertEquals(expectedFileVisitResult, fileVisitResult);
        verify(logger, times(1)).trace("visitFile({}, {})", file, attrs);
    }

    @Test
    public void test_WhenVisitFileFailed_ThenLogAndContinue() throws IOException {
        // GIVEN
        final FileVisitorChainBuilder fileVisitorChainBuilder = this.fileVisitorChainBuilder;
        final Path file = this.path;
        final IOException ioException = this.ioException;
        final FileVisitResult expectedFileVisitResult = FileVisitResult.CONTINUE;

        // WHEN
        FileVisitor<Path> fileVisitor = fileVisitorChainBuilder.create();
        FileVisitResult fileVisitResult = fileVisitor.visitFileFailed(file, ioException);

        // THEN
        assertEquals(expectedFileVisitResult, fileVisitResult);
        verify(logger, times(1)).trace("visitFileFailed({}, {})", file, ioException);
    }

    @Test
    public void test_WhenPostVisitDirectory_ThenLogAndContinue() throws IOException {
        // GIVEN
        final FileVisitorChainBuilder fileVisitorChainBuilder = this.fileVisitorChainBuilder;
        final Path dir = this.path;
        final IOException ioException = this.ioException;
        final FileVisitResult expectedFileVisitResult = FileVisitResult.CONTINUE;
        given(this.attrs.isDirectory()).willReturn(true);

        // WHEN
        FileVisitor<Path> fileVisitor = fileVisitorChainBuilder.create();
        FileVisitResult fileVisitResult = fileVisitor.postVisitDirectory(dir, ioException);

        // THEN
        assertEquals(expectedFileVisitResult, fileVisitResult);
        verify(logger, times(1)).trace("postVisitDirectory({}, {})", dir, ioException);
    }
}
