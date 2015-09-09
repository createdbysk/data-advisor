package com.data_advisor.local.event.file_system;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Base class for a path event.
 */
public abstract class PathEvent {
    private final Path path;
    private final BasicFileAttributes basicFileAttributes;

    public PathEvent(Path path, BasicFileAttributes basicFileAttributes) {
        this.path = path;
        this.basicFileAttributes = basicFileAttributes;
    }

    public Path getPath() {
        return path;
    }

    public BasicFileAttributes getBasicFileAttributes() {
        return basicFileAttributes;
    }
}
