package com.data_advisor.school.spring.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Listener for all BaseEvent
 */
@Component
public class BaseEventListener implements ApplicationListener<BaseEvent> {
    private Logger logger = LoggerFactory.getLogger(BaseEventListener.class);

    @Override
    public void onApplicationEvent(BaseEvent event) {
        logger.trace("BaseEventListener.onApplicationEvent with {} {}", event, event.getClass());
    }
}
