package com.data_advisor.school.spring.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Derived event 2
*/
public class DerivedEvent2 extends BaseEvent {
    private static final Logger logger = LoggerFactory.getLogger(BaseEvent.class);
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DerivedEvent2(Object source) {
        super(source);
        logger.trace("Create DerivedEvent2 with {}", source);
    }
}
