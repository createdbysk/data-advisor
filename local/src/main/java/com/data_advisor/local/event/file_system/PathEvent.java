package com.data_advisor.local.event.file_system;

import org.springframework.context.ApplicationEvent;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Base class for a path event.
 *
 * This event participates in the Spring event handling system.
 * 1. Avoid the need for Spring to wrap and unwrap this object in a PayloadApplicationEvent.
 *     Spring wraps any object that does not extend ApplicationEvent in a PayloadApplicationEvent
 *     and unwraps this when it passes it to the EventListener. Extend PathEvent from ApplicationEvent to
 *     avoid this behavior.
 * 2. Store the source of the event in the source parameter.
 *      This allows in tracing of data and execution flow in the system.
 */
public abstract class PathEvent extends ApplicationEvent {
    private final Path path;
    private final BasicFileAttributes basicFileAttributes;

    public PathEvent(Path path, BasicFileAttributes basicFileAttributes, Object source) {
        super(source);
        this.path = path;
        this.basicFileAttributes = basicFileAttributes;
    }

    public Path getPath() {
        return path;
    }

    public BasicFileAttributes getBasicFileAttributes() {
        return basicFileAttributes;
    }

    @Override
    public String toString() {
        return path.toAbsolutePath().normalize().toString();
    }
}
