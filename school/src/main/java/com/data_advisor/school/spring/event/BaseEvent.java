package com.data_advisor.school.spring.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

/**
 * Event base class
 */
public abstract class BaseEvent extends ApplicationEvent {
    private static final Logger logger = LoggerFactory.getLogger(BaseEvent.class);
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public BaseEvent(Object source) {
        super(source);
        logger.trace("Create {}", this);
    }
}
