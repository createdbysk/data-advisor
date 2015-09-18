package com.data_advisor.local.event.file_system.impl;

import com.data_advisor.local.event.file_system.PathEvent;
import com.data_advisor.local.event.file_system.PathEventPublisher;
import org.apache.storm.guava.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * ApplicationEventPublisherAware Publisher for file system events
 */
@Component
public class PathEventPublisherImpl implements ApplicationEventPublisherAware, PathEventPublisher {
    @VisibleForTesting
    private Logger logger = LoggerFactory.getLogger(PathEventPublisher.class);

    @VisibleForTesting
    ApplicationEventPublisher applicationEventPublisher;

    /** {@inheritDoc} */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Publish the given file system event
     * @param pathEvent   The event to publish
     */
    @Override
    public void publish(PathEvent pathEvent) {
        logger.trace("publishEvent(pathEvent={})", pathEvent);
        applicationEventPublisher.publishEvent(pathEvent);
    }
}
