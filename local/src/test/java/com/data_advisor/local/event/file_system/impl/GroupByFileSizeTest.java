package com.data_advisor.local.event.file_system.impl;

import com.data_advisor.local.application.ApplicationConfig;
import com.data_advisor.local.application.FileSystemAbstractFactory;
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

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link GroupByFileSize}
 * Use the {@link SpringJUnit4ClassRunner} to auto-wire injected dependencies. This ensures that the dependencies don't miss
 * expected annotations. This satisfies Design Principle #2.
 *  2. Verify that the injected entities are configured correctly for dependency injection.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Verify that the ApplicationConfig injects the required objects and dependencies.
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ApplicationConfig.class})
public class GroupByFileSizeTest {
    private static final Long FILE_SIZE = 1L;
    private static final Long DIFFERENT_FILE_SIZE = 2L;
    private static final Long SAME_FILE_SIZE = FILE_SIZE;

    // Use this instance to verify that the GroupByFileSize has the expected annotation to be able to
    // Autowire an instance..
    // NOTE: Intellij community edition does not detect that this class is Auto-wired.
    @Autowired
    private GroupByFileSize groupByFileSizePathEventListenerAutowired;

    @InjectMocks
    private GroupByFileSize groupByFileSize;

    @Mock
    private FilePathEvent filePathEvent;

    @Mock
    private Path filePath;

    @Mock
    private BasicFileAttributes fileAttributes;

    @Mock
    private FileSystemAbstractFactory fileSystemAbstractFactory;

    private Map<Long, List<FilePathEvent>> filesGroupedBySize;

    @Mock
    private FilePathEvent differentSizeFilePathEvent;

    @Mock
    private FilePathEvent sameSizeFilePathEvent;

    @Mock
    private BasicFileAttributes differentSizeFileAttributes;

    @Mock
    private Path differentSizeFilePath;

    @Mock
    private BasicFileAttributes sameSizeFileAttributes;

    @Mock
    private Path sameSizeFilePath;

    @Mock
    private Logger logger;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        setUpFileAttributes(fileAttributes, FILE_SIZE);
        filePathEvent = new FilePathEvent(filePath, fileAttributes, this);

        setUpFileAttributes(differentSizeFileAttributes, DIFFERENT_FILE_SIZE);
        differentSizeFilePathEvent = new FilePathEvent(differentSizeFilePath, differentSizeFileAttributes, this);

        setUpFileAttributes(sameSizeFileAttributes, SAME_FILE_SIZE);
        sameSizeFilePathEvent = new FilePathEvent(sameSizeFilePath, sameSizeFileAttributes, this);

        filesGroupedBySize = new HashMap<>();
        given(fileSystemAbstractFactory.getFilesGroupedBySize()).willReturn(filesGroupedBySize);

        // Complete the setup of the listener.
        groupByFileSize.postConstruct();
    }

    private void setUpFileAttributes(BasicFileAttributes fileAttributes, long size) {
        given(fileAttributes.isDirectory()).willReturn(false);
        given(fileAttributes.isRegularFile()).willReturn(true);
        given(fileAttributes.isSymbolicLink()).willReturn(false);
        given(fileAttributes.size()).willReturn(size);
    }

    @Test
    public void testGroupByFileSizePathEventListener_CanAutowire() {
        assertNotNull(groupByFileSizePathEventListenerAutowired);
    }

    @Test
    public void test_WhenPostConstruct_ThenStoreFilesGroupedBySizeMap() {
        // GIVEN
        final GroupByFileSize groupByFileSizePathEventListener = this.groupByFileSize;

        // WHEN
        // postConstruct() called in setUp().

        // THEN
        assertSame(groupByFileSizePathEventListener.filesGroupedBySize, filesGroupedBySize);
    }

    @Test
    public void test_GivenOnlyASingleFileWithAGivenSize_WhenReceiveFilePathEvent_ThenFilesGroupedBySizeHasOnlyOneFileForThatSize() {
        // GIVEN
        final FilePathEvent filePathEvent = this.filePathEvent;
        final GroupByFileSize groupByFileSize = this.groupByFileSize;
        final Map<Long, List<FilePathEvent>> filesGroupedBySize = this.filesGroupedBySize;
        final Long fileSize = FILE_SIZE;
        final Logger logger = this.logger;
        // Group for files of FILE_SIZE
        List<FilePathEvent> fileGroup = new ArrayList<>();
        given(fileSystemAbstractFactory.createFileGroup()).willReturn(fileGroup);

        // WHEN
        groupByFileSize.execute(filePathEvent);

        // THEN
        List<FilePathEvent> storedFilesGroup = filesGroupedBySize.get(fileSize);
        assertEquals(storedFilesGroup.size(), 1);
        assertSame(storedFilesGroup.get(0), filePathEvent);
        assertSame(fileGroup, storedFilesGroup);
        verify(logger, times(1)).trace("execute({})", filePathEvent);
        verify(logger, times(1)).debug("execute({}) - created a new group for files with size {}", filePathEvent, fileSize);
    }

    @Test
    public void test_GivenTwoFilesOfDifferentSizes_WhenReceiveFilePathEvent_ThenFilesGroupedBySizeHasTwoGroups() {
        // GIVEN
        final FilePathEvent filePathEvent = this.filePathEvent;
        final FilePathEvent differentSizeFilePathEvent = this.differentSizeFilePathEvent;
        final GroupByFileSize groupByFileSize = this.groupByFileSize;
        final Map<Long, List<FilePathEvent>> filesGroupedBySize = this.filesGroupedBySize;
        final Long differentFileSize = DIFFERENT_FILE_SIZE;
        final Logger logger = this.logger;
        // Group for files of FILE_SIZE
        List<FilePathEvent> fileGroup = new ArrayList<>();
        // Group for files of DIFFERENT_FILE_SIZE
        List<FilePathEvent> differentFileSizeGroup = new ArrayList<>();
        given(fileSystemAbstractFactory.createFileGroup()).willReturn(fileGroup).willReturn(differentFileSizeGroup);

        // WHENt
        groupByFileSize.execute(filePathEvent);
        groupByFileSize.execute(differentSizeFilePathEvent);

        // THEN
        List<FilePathEvent> storedFilesGroup = filesGroupedBySize.get(differentFileSize);
        assertEquals(storedFilesGroup.size(), 1);
        assertSame(storedFilesGroup.get(0), differentSizeFilePathEvent);
        assertSame(differentFileSizeGroup, storedFilesGroup);
        verify(fileSystemAbstractFactory, times(2)).createFileGroup();
        verify(logger, times(1)).trace("execute({})", filePathEvent);
        verify(logger, times(1)).debug("execute({}) - created a new group for files with size {}", differentSizeFilePathEvent, differentFileSize);
    }

    @Test
    public void test_GivenTwoFilesOfSameSize_WhenReceiveFilePathEvent_ThenFilesGroupedBySizeHasOneGroupOfTwo() {
        // GIVEN
        final FilePathEvent filePathEvent = this.filePathEvent;
        final FilePathEvent sameSizeFilePathEvent = this.sameSizeFilePathEvent;
        final GroupByFileSize groupByFileSizePathEventListener = this.groupByFileSize;
        final Map<Long, List<FilePathEvent>> filesGroupedBySize = this.filesGroupedBySize;
        final Long sameFileSize = SAME_FILE_SIZE;
        final Logger logger = this.logger;

        // Group for files of FILE_SIZE and SAME_FILE_SIZE
        List<FilePathEvent> fileGroup = new ArrayList<>();
        given(fileSystemAbstractFactory.createFileGroup()).willReturn(fileGroup);

        // WHEN
        groupByFileSizePathEventListener.execute(filePathEvent);
        groupByFileSizePathEventListener.execute(sameSizeFilePathEvent);

        // THEN
        List<FilePathEvent> storedFilesGroup = filesGroupedBySize.get(sameFileSize);
        assertEquals(storedFilesGroup.size(), 2);
        assertSame(storedFilesGroup.get(0), filePathEvent);
        assertSame(storedFilesGroup.get(1), sameSizeFilePathEvent);
        assertSame(fileGroup, storedFilesGroup);
        verify(fileSystemAbstractFactory, times(1)).createFileGroup();
        verify(logger, times(1)).trace("execute({})", filePathEvent);
        verify(logger, times(1)).debug("execute({}) - will add file to group with size {}", sameSizeFilePathEvent, sameFileSize);
    }
}
