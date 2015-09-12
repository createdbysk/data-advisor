package com.data_advisor.local.event.file_system;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * PathEvent instance for file paths.
 */
public class FilePathEvent extends PathEvent {
    public FilePathEvent(Path path, BasicFileAttributes basicFileAttributes) {
        super(path, basicFileAttributes);
    }
}
