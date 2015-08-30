package com.data_advisor.local.application.entry_point;

import com.data_advisor.local.service.file_system.FileSystemService;
import org.apache.storm.guava.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides the entry points for the local file system support.
 */
@Component
public class LocalFileSystem {
    private final AddHierarchyVisitor addHierarchyVisitor;
    private final FileSystemService fileSystemService;

    @Autowired
    public LocalFileSystem(AddHierarchyVisitor addHierarchyVisitor, FileSystemService fileSystemService) {
        this.addHierarchyVisitor = addHierarchyVisitor;
        this.fileSystemService = fileSystemService;
    }
    /**
     * Add the contents of the hierarchy rooted at the given absolutePath to the
     * file collection.
     * NOTE: If absolutePath is a file, then just adds the file to the
     * file collection.
     *
     * @param absolutePath  The absolute path to start at.
     */
    public void addHierarchy(String absolutePath) {
        Path path = createPath(absolutePath);
        fileSystemService.visitPath(path, addHierarchyVisitor);
    }

    /** Wrapper to provide test a stud to convert a absolutePath string to a Path object */
    @VisibleForTesting
    Path createPath(String absolutePath) {
        return Paths.get(absolutePath);
    }
}
