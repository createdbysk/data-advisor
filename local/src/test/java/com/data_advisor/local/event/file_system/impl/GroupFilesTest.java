package com.data_advisor.local.event.file_system.impl;

import com.data_advisor.local.application.ApplicationConfig;
import com.data_advisor.local.application.FileSystemAbstractFactory;
import com.data_advisor.local.service.file_system.FileSystemService;
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
 * Test for {@link GroupFiles}
 * Use the {@link SpringJUnit4ClassRunner} to auto-wire injected dependencies. This ensures that the dependencies don't miss
 * expected annotations. This satisfies Design Principle #2.
 *  2. Verify that the injected entities are configured correctly for dependency injection.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Verify that the ApplicationConfig injects the required objects and dependencies.
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ApplicationConfig.class})
public class GroupFilesTest {
    private static final Long FILE_SIZE = 1L;
    private static final Long DIFFERENT_FILE_SIZE = 2L;
    private static final Long SAME_FILE_SIZE = FILE_SIZE;
    private static final String FILE_MD5_HASH = "file_md5_hash";
    private static final String DIFFERENT_FILE_MD5_HASH = "different_file_md5_hash";
    private static final String SAME_MD5_HASH = FILE_MD5_HASH;

    // Use this instance to verify that the GroupFiles has the expected annotation to be able to
    // Autowire an instance..
    // NOTE: Intellij community edition does not detect that this class is Auto-wired.
    @Autowired
    private GroupFiles groupFilesAutowired;

    @InjectMocks
    private GroupFiles groupFiles;

    @Mock
    private FilePathEvent filePathEvent;

    @Mock
    private Path filePath;

    @Mock
    private BasicFileAttributes fileAttributes;

    @Mock
    private FileSystemAbstractFactory fileSystemAbstractFactory;

    private Map<Long, List<FilePathEvent>> filesGroupedBySize;

    private Map<String, List<FilePathEvent>> filesGroupedByMd5Hash;

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

    @Mock
    private FileSystemService fileSystemService;

    @Mock
    private Path sameMd5HashFilePath;

    @Mock
    private FilePathEvent sameMd5HashFilePathEvent;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        given(fileSystemAbstractFactory.getFileSystemService()).willReturn(fileSystemService);

        setUpFileAttributes(filePath, fileAttributes, FILE_SIZE, FILE_MD5_HASH);
        filePathEvent = new FilePathEvent(filePath, fileAttributes, this);

        setUpFileAttributes(differentSizeFilePath, differentSizeFileAttributes, DIFFERENT_FILE_SIZE, DIFFERENT_FILE_MD5_HASH);
        differentSizeFilePathEvent = new FilePathEvent(differentSizeFilePath, differentSizeFileAttributes, this);

        setUpFileAttributes(sameSizeFilePath, sameSizeFileAttributes, SAME_FILE_SIZE, DIFFERENT_FILE_MD5_HASH);
        sameSizeFilePathEvent = new FilePathEvent(sameSizeFilePath, sameSizeFileAttributes, this);

        setUpFileAttributes(sameMd5HashFilePath, sameSizeFileAttributes, SAME_FILE_SIZE, SAME_MD5_HASH);
        sameMd5HashFilePathEvent = new FilePathEvent(sameMd5HashFilePath, sameSizeFileAttributes, this);

        filesGroupedBySize = new HashMap<>();
        given(fileSystemAbstractFactory.getFilesGroupedBySize()).willReturn(filesGroupedBySize);

        filesGroupedByMd5Hash = new HashMap<>();
        given(fileSystemAbstractFactory.getFilesGroupedByMd5Hash()).willReturn(filesGroupedByMd5Hash);
    }

    private void setUpFileAttributes(Path filePath, BasicFileAttributes fileAttributes, long size, String md5Hash) {
        given(fileAttributes.isDirectory()).willReturn(false);
        given(fileAttributes.isRegularFile()).willReturn(true);
        given(fileAttributes.isSymbolicLink()).willReturn(false);
        given(fileAttributes.size()).willReturn(size);
        given(fileSystemService.computeMd5Hash(filePath)).willReturn(md5Hash);
    }

    @Test
    public void testGroupByFileSize_CanAutowire() {
        assertNotNull(groupFilesAutowired);
    }

    @Test
    public void test_GivenOnlyASingleFileWithAGivenSize_WhenReceiveFilePathEvent_ThenFilesGroupedBySizeHasOnlyOneFileForThatSize() {
        // GIVEN
        final FilePathEvent filePathEvent = this.filePathEvent;
        final GroupFiles groupFiles = this.groupFiles;
        final Map<Long, List<FilePathEvent>> filesGroupedBySize = this.filesGroupedBySize;
        final FileSystemAbstractFactory fileSystemAbstractFactory = this.fileSystemAbstractFactory;
        final Long fileSize = FILE_SIZE;
        final Logger logger = this.logger;
        // Group for files of FILE_SIZE
        List<FilePathEvent> filesOfTheSameSize = new ArrayList<>();
        given(fileSystemAbstractFactory.createFilesGroup()).willReturn(filesOfTheSameSize);

        // WHEN
        groupFiles.execute(filePathEvent);

        // THEN
        List<FilePathEvent> storedFilesGroup = filesGroupedBySize.get(fileSize);
        assertEquals(storedFilesGroup.size(), 1);
        assertSame(storedFilesGroup.get(0), filePathEvent);
        assertSame(filesOfTheSameSize, storedFilesGroup);
        verify(logger, times(1)).trace("execute({})", filePathEvent);
        verify(logger, times(1)).debug("execute({}) - created a new group for files with size {}", filePathEvent, fileSize);
        // Create the files group only for the group of files of the same size and not for the md5 hash group.
        // Given there is only one file, then the createFilesGroup() should be called only once.
        verify(fileSystemAbstractFactory, times(1)).createFilesGroup();
    }

    @Test
    public void test_GivenTwoFilesOfDifferentSizes_WhenReceiveFilePathEvent_ThenFilesGroupedBySizeHasTwoGroups() {
        // GIVEN
        final FilePathEvent filePathEvent = this.filePathEvent;
        final FilePathEvent differentSizeFilePathEvent = this.differentSizeFilePathEvent;
        final GroupFiles groupFiles = this.groupFiles;
        final Map<Long, List<FilePathEvent>> filesGroupedBySize = this.filesGroupedBySize;
        final FileSystemAbstractFactory fileSystemAbstractFactory = this.fileSystemAbstractFactory;
        final Long differentFileSize = DIFFERENT_FILE_SIZE;
        final Logger logger = this.logger;
        // First group of files
        List<FilePathEvent> fileGroup1 = new ArrayList<>();
        // Second group of files
        List<FilePathEvent> fileGroup2 = new ArrayList<>();
        given(fileSystemAbstractFactory.createFilesGroup()).willReturn(fileGroup1).willReturn(fileGroup2);

        // WHENt
        groupFiles.execute(filePathEvent);
        groupFiles.execute(differentSizeFilePathEvent);

        // THEN
        List<FilePathEvent> storedFilesGroup = filesGroupedBySize.get(differentFileSize);
        assertEquals(storedFilesGroup.size(), 1);
        assertSame(storedFilesGroup.get(0), differentSizeFilePathEvent);
        // GroupFiles.execute() should call createFilesGroup() twice, once for each file size.
        verify(fileSystemAbstractFactory, times(2)).createFilesGroup();
        verify(logger, times(1)).trace("execute({})", filePathEvent);
        verify(logger, times(1)).trace("execute({})", differentSizeFilePathEvent);
        verify(logger, times(1)).debug("execute({}) - created a new group for files with size {}", differentSizeFilePathEvent, differentFileSize);
    }

    @Test
    public void test_GivenTwoFilesOfSameSizeAndDifferentMd5Hash_WhenReceiveFilePathEvent_ThenFilesGroupedBySizeHasOneGroupOfTwoAndFilesGroupedByMd5HashHasTwoGroupsOfOne() {
        // GIVEN
        final FilePathEvent filePathEvent = this.filePathEvent;
        final FilePathEvent sameSizeFilePathEvent = this.sameSizeFilePathEvent;
        final GroupFiles groupFilesPathEventListener = this.groupFiles;
        final Map<Long, List<FilePathEvent>> filesGroupedBySize = this.filesGroupedBySize;
        final FileSystemAbstractFactory fileSystemAbstractFactory = this.fileSystemAbstractFactory;
        final Long fileSize = FILE_SIZE;
        final Long sameFileSize = SAME_FILE_SIZE;
        final String fileMd5Hash = FILE_MD5_HASH;
        final String differentFileMd5Hash = DIFFERENT_FILE_MD5_HASH;
        final Logger logger = this.logger;

        // Containers for groups of files.
        // Create three containers - one for the files grouped by size and two others for the files grouped
        // by MD5 hash because there are two different MD5 hashes.
        List<FilePathEvent> fileGroup1 = new ArrayList<>();
        List<FilePathEvent> fileGroup2 = new ArrayList<>();
        List<FilePathEvent> fileGroup3 = new ArrayList<>();
        given(fileSystemAbstractFactory.createFilesGroup())
                .willReturn(fileGroup1)
                .willReturn(fileGroup2)
                .willReturn(fileGroup3);

        // WHEN
        groupFilesPathEventListener.execute(filePathEvent);
        groupFilesPathEventListener.execute(sameSizeFilePathEvent);

        // THEN
        List<FilePathEvent> storedFilesBySizeGroup = filesGroupedBySize.get(sameFileSize);
        assertEquals(storedFilesBySizeGroup.size(), 2);
        assertSame(storedFilesBySizeGroup.get(0), filePathEvent);
        assertSame(storedFilesBySizeGroup.get(1), sameSizeFilePathEvent);
        List<FilePathEvent> storedFilesByMd5HashGroup = filesGroupedByMd5Hash.get(fileMd5Hash);
        assertEquals(storedFilesByMd5HashGroup.size(), 1);
        assertSame(storedFilesByMd5HashGroup.get(0), filePathEvent);
        List<FilePathEvent> storedDifferentFilesByMd5HashGroup = filesGroupedByMd5Hash.get(differentFileMd5Hash);
        assertEquals(storedDifferentFilesByMd5HashGroup.size(), 1);
        assertSame(storedDifferentFilesByMd5HashGroup.get(0), sameSizeFilePathEvent);
        // Once for the file size and twice for the md5 hash.
        verify(fileSystemAbstractFactory, times(3)).createFilesGroup();
        verify(logger, times(1)).trace("execute({})", filePathEvent);
        verify(logger, times(1)).trace("execute({})", sameSizeFilePathEvent);
        verify(logger, times(1)).debug("execute({}) - created a new group for files with size {}", filePathEvent, fileSize);
        verify(logger, times(1)).debug("execute({}) - will add file to group with size {}", sameSizeFilePathEvent, sameFileSize);
        verify(logger, times(1)).debug("execute({}) - created a new group for files with MD5 hash {}", filePathEvent, fileMd5Hash);
        verify(logger, times(1)).debug("execute({}) - created a new group for files with MD5 hash {}", sameSizeFilePathEvent, differentFileMd5Hash);
    }

    @Test
    public void test_GivenTwoFilesOfSameSizeAndSameMd5Hash_WhenReceiveFilePathEvent_ThenFilesGroupedBySizeHasOneGroupOfTwoAndFilesGroupedByMd5HashHasOneGroupOfTwo() {
        // GIVEN
        final FilePathEvent filePathEvent = this.filePathEvent;
        final FilePathEvent sameMd5HashFilePathEvent = this.sameMd5HashFilePathEvent;
        final GroupFiles groupFilesPathEventListener = this.groupFiles;
        final Map<Long, List<FilePathEvent>> filesGroupedBySize = this.filesGroupedBySize;
        final Map<String, List<FilePathEvent>> filesGroupedByMd5Hash = this.filesGroupedByMd5Hash;
        final FileSystemAbstractFactory fileSystemAbstractFactory = this.fileSystemAbstractFactory;
        final Long fileSize = FILE_SIZE;
        final Long sameFileSize = SAME_FILE_SIZE;
        final String fileMd5Hash = FILE_MD5_HASH;
        final String sameMd5Hash = SAME_MD5_HASH;
        final Logger logger = this.logger;

        // Containers for groups of files.
        // Create two containers - one for the files grouped by size and one for the files grouped by md5 hash.
        List<FilePathEvent> fileGroup1 = new ArrayList<>();
        List<FilePathEvent> fileGroup2 = new ArrayList<>();
        given(fileSystemAbstractFactory.createFilesGroup())
                .willReturn(fileGroup1)
                .willReturn(fileGroup2);

        // WHEN
        groupFilesPathEventListener.execute(filePathEvent);
        groupFilesPathEventListener.execute(sameMd5HashFilePathEvent);

        // THEN
        List<FilePathEvent> storedFilesBySizeGroup = filesGroupedBySize.get(sameFileSize);
        assertEquals(storedFilesBySizeGroup.size(), 2);
        assertSame(storedFilesBySizeGroup.get(0), filePathEvent);
        assertSame(storedFilesBySizeGroup.get(1), sameMd5HashFilePathEvent);
        List<FilePathEvent> storedFilesByMd5HashGroup = filesGroupedByMd5Hash.get(fileMd5Hash);
        assertEquals(storedFilesByMd5HashGroup.size(), 2);
        assertSame(storedFilesByMd5HashGroup.get(0), filePathEvent);
        assertSame(storedFilesByMd5HashGroup.get(1), sameMd5HashFilePathEvent);
        // Once for the file size and twice for the md5 hash.
        verify(fileSystemAbstractFactory, times(2)).createFilesGroup();
        verify(logger, times(1)).trace("execute({})", filePathEvent);
        verify(logger, times(1)).trace("execute({})", sameMd5HashFilePathEvent);
        verify(logger, times(1)).debug("execute({}) - created a new group for files with size {}", filePathEvent, fileSize);
        verify(logger, times(1)).debug("execute({}) - will add file to group with size {}", sameMd5HashFilePathEvent, sameFileSize);
        verify(logger, times(1)).debug("execute({}) - created a new group for files with MD5 hash {}", filePathEvent, fileMd5Hash);
        verify(logger, times(1)).debug("execute({}) - will add file to group with MD5 hash {}", sameMd5HashFilePathEvent, sameMd5Hash);
    }
}
