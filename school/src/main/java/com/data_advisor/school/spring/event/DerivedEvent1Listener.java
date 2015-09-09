package com.data_advisor.school.spring.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Listener for all DerivedEvent1
 */
@Component
public class DerivedEvent1Listener implements ApplicationListener<DerivedEvent1> {
    private Logger logger = LoggerFactory.getLogger(DerivedEvent1Listener.class);

    @Override
    public void onApplicationEvent(DerivedEvent1 event) {
        logger.trace("DerivedEvent1Listener.onApplicationEvent with {} {}", event, event.getClass());
    }
}
