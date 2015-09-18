package com.data_advisor.local.event.file_system;

/**
 * Publisher for PathEvent instances.
 */
public interface PathEventPublisher {
    /**
     * Publish the given file system event
     * @param pathEvent   The event to publish
     */
    void publish(PathEvent pathEvent);
}
