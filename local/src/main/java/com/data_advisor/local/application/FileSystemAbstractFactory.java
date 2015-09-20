package com.data_advisor.local.application;

import com.data_advisor.local.event.file_system.PathEvent;
import com.data_advisor.local.event.file_system.PathEventPublisher;
import com.data_advisor.local.event.file_system.impl.FilePathEvent;
import com.data_advisor.local.service.file_system.FileSystemService;

import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;

/**
 * Abstract factory for AddHierarchy.
 */
public interface FileSystemAbstractFactory {
    /** Gets an instance of FileSystemService */
    FileSystemService getFileSystemService();

    /** Create an instance of a FileVisitor */
    FileVisitor<Path> createFileVisitor();

    /** Get the Path object corresponding to the given absolutePath */
    Path getPath(String absolutePath);

    /**
     * Create the path event given the path and the attributes.
     *
     * @param path      The path.
     * @param attrs     The attributes of path.
     * @param source    The object that made this call.
     * @return          The path event instance based on the path and attributes.
     */
    PathEvent createPathEvent(Path path, BasicFileAttributes attrs, Object source);

    /** Get an instance of PathEventPublisher */
    PathEventPublisher getPathEventPublisher();

    /** Get a view of the files mapped by their size. Use a {@link Map} to represent this view. */
    Map<Long, List<FilePathEvent>> getFilesGroupedBySize();

    /** Create the container for a group of files */
    List<FilePathEvent> createFileGroup();
}
