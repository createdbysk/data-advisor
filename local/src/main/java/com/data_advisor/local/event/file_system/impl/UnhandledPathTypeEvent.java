package com.data_advisor.local.event.file_system.impl;

import com.data_advisor.local.event.file_system.PathEvent;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Event for path types that the application will not handle.
 */
public class UnhandledPathTypeEvent extends PathEvent {
    public UnhandledPathTypeEvent(Path path, BasicFileAttributes basicFileAttributes, Object source) {
        super(path, basicFileAttributes, source);
    }
}
