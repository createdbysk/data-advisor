package com.data_advisor.school.spring.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Listener for all DerivedEvent2
 */
@Component
public class DerivedEvent2Listener implements ApplicationListener<DerivedEvent2> {
    private Logger logger = LoggerFactory.getLogger(DerivedEvent2Listener.class);

    @Override
    public void onApplicationEvent(DerivedEvent2 event) {
        logger.trace("DerivedEvent2Listener.onApplicationEvent with {} {}", event, event.getClass());
    }
}
