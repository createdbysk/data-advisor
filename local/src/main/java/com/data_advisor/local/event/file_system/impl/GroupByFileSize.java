package com.data_advisor.local.event.file_system.impl;

import com.data_advisor.local.application.FileSystemAbstractFactory;
import org.apache.storm.guava.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;

/**
 * Listener for PathEvent that groups files by size.
 */
@Component
public class GroupByFileSize {
    private Logger logger = LoggerFactory.getLogger(GroupByFileSize.class);

    @VisibleForTesting
    Map<Long, List<FilePathEvent>> filesGroupedBySize;

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
        List<FilePathEvent> fileGroup;

        // Check if a group with a size of the file that generated this event already exists.
        // If a group exists then add the event to that group. Otherwise, create a new group
        // and add the event to that group.
        if (filesGroupedBySize.containsKey(size)) {
            fileGroup = filesGroupedBySize.get(size);
            logger.debug("execute({}) - will add file to group with size {}", event, size);
        } else {
            fileGroup = fileSystemAbstractFactory.createFileGroup();
            filesGroupedBySize.put(size, fileGroup);
            logger.debug("execute({}) - created a new group for files with size {}", event, size);
        }
        // Now that this method has the group that this file belongs to,
        // add the event to that group. Use the event because the event
        // has the file path as well as the attributes of the file.
        fileGroup.add(event);
    }

    /** Use this method to obtain dependencies through the fileSystemAbstractFactory. */
    @PostConstruct
    public void postConstruct() {
        filesGroupedBySize = fileSystemAbstractFactory.getFilesGroupedBySize();
    }
}
