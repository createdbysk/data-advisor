package com.data_advisor.local.event.file_system.impl;

import com.data_advisor.local.application.FileSystemAbstractFactory;
import com.data_advisor.local.service.file_system.FileSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;

/**
 * Listener for PathEvent that groups files by size.
 */
@Component
public class GroupFiles {
    private Logger logger = LoggerFactory.getLogger(GroupFiles.class);

    /** Use this object to obtain other dependencies. */
    @Autowired
    private FileSystemAbstractFactory fileSystemAbstractFactory;

    /**
     * Listens for FilePathEvents and groups the files with the same size.
     * @param event The event that carries the information about the file.
     */
    @EventListener
    public void execute(FilePathEvent event) {
        logger.trace("execute({})", event);

        final BasicFileAttributes attributes = event.getBasicFileAttributes();
        final long size = attributes.size();
        final Map<Long, List<FilePathEvent>> filesGroupedBySize = fileSystemAbstractFactory.getFilesGroupedBySize();
        List<FilePathEvent> fileGroup;

        // Check if a group with a size of the file that generated this event already exists.
        // If a group exists then add the event to that group. Otherwise, create a new group
        // and add the event to that group.
        if (filesGroupedBySize.containsKey(size)) {
            fileGroup = filesGroupedBySize.get(size);
            logger.debug("execute({}) - will add file to group with size {}", event, size);
        } else {
            fileGroup = fileSystemAbstractFactory.createFilesGroup();
            filesGroupedBySize.put(size, fileGroup);
            logger.debug("execute({}) - created a new group for files with size {}", event, size);
        }
        // Now that this method has the group that this file belongs to,
        // add the event to that group. Use the event because the event
        // has the file path as well as the attributes of the file.
        fileGroup.add(event);
        groupFilesByMd5Hash(fileGroup);
    }

    private void groupFilesByMd5Hash(List<FilePathEvent> fileGroup) {
        // There is only one file in the group. There is no need to calculate the MD5 hash yet because
        // there is no other file with the same size. Therefore, there is no possibility of a duplicate.
        if (fileGroup.size() == 1) {
            return;
        }
        // If the group has 2 elements, then this is the first time that the files in this group need to have
        // their MD5 calculated. Calculate the MD5 hash for the first file in the group and create the group
        // to store files that have the same MD5 hash.
        if (fileGroup.size() == 2) {
            final FilePathEvent filePathEvent = fileGroup.get(0);
            addToMd5HashGroup(filePathEvent);
        }
        // The last element of the fileGroup is the new element added to the group.
        // Determine the md5 hash group this file belongs to.
        final int lastIndex = fileGroup.size() - 1;
        final FilePathEvent lastFilePathEvent = fileGroup.get(lastIndex);
        addToMd5HashGroup(lastFilePathEvent);
    }

    /** Adds the file to the group with the same MD5 hash */
    private void addToMd5HashGroup(FilePathEvent filePathEvent) {
        final Path filePath = filePathEvent.getPath();
        final FileSystemService fileSystemService = fileSystemAbstractFactory.getFileSystemService();
        final String fileMd5Hash = fileSystemService.computeMd5Hash(filePath);
        final Map<String, List<FilePathEvent>> filesGroupedByMd5Hash = fileSystemAbstractFactory.getFilesGroupedByMd5Hash();
        List<FilePathEvent> fileGroup;
        if (filesGroupedByMd5Hash.containsKey(fileMd5Hash)) {
            fileGroup = filesGroupedByMd5Hash.get(fileMd5Hash);
            logger.debug("execute({}) - will add file to group with MD5 hash {}", filePathEvent, fileMd5Hash);
        } else {
            fileGroup = fileSystemAbstractFactory.createFilesGroup();
            filesGroupedByMd5Hash.put(fileMd5Hash, fileGroup);
            logger.debug("execute({}) - created a new group for files with MD5 hash {}", filePathEvent, fileMd5Hash);
        }
        fileGroup.add(filePathEvent);
    }
}
