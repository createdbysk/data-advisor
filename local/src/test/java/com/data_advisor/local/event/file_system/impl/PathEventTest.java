package com.data_advisor.local.event.file_system.impl;

import com.data_advisor.local.application.ApplicationConfig;
import com.data_advisor.local.event.file_system.PathEvent;
import com.data_advisor.local.event.file_system.PathEventPublisher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test for all {@link PathEvent} and derived events.
 * Use the {@link SpringJUnit4ClassRunner} to auto-wire injected dependencies. This ensures that the dependencies don't miss
 * expected annotations. This satisfies Design Principle #2.
 *  2. Verify that the injected entities are configured correctly for dependency injection.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Verify that the ApplicationConfig injects the required objects and dependencies.
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ApplicationConfig.class})
public class PathEventTest {
    @Mock
    private BasicFileAttributes basicFileAttributes;

    // Autowired instance to test correct annotation.
    // Intellij may be unable to detect this instance is autowired.
    @Autowired
    private PathEventPublisher pathEventPublisherAutowired;

    @InjectMocks
    private PathEventPublisherImpl pathEventPublisher;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    Path path;

    @Mock
    private PathEvent pathEvent;

    @Mock
    private Logger logger;

    private class TestPathEvent extends PathEvent {
        public TestPathEvent(Path path, BasicFileAttributes basicFileAttributes, Object source) {
            super(path, basicFileAttributes, source);
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPathEvent_Exists() {
        // GIVEN
        final Path path = this.path;
        final BasicFileAttributes basicFileAttributes = this.basicFileAttributes;

        // WHEN
        PathEvent pathEvent = new TestPathEvent(path, basicFileAttributes, this);

        // THEN
        assertSame(path, pathEvent.getPath());
        assertSame(basicFileAttributes, pathEvent.getBasicFileAttributes());
        assertSame(this, pathEvent.getSource());
    }

    @Test
    public void testFileSystemEventPublisher_CanAutowire() {
        assertTrue(pathEventPublisherAutowired instanceof PathEventPublisherImpl);
        assertNotNull(((PathEventPublisherImpl) pathEventPublisherAutowired).applicationEventPublisher);
    }

    @Test
    public void testFileSystemEventPublisher_WhenPublish_ThenPublishesFileSystemEventAndLogs() {
        // GIVEN
        final PathEvent pathEvent = this.pathEvent;

        // WHEN
        pathEventPublisher.publish(pathEvent);

        // THEN
        verify(applicationEventPublisher, times(1)).publishEvent(pathEvent);
        verify(logger, times(1)).trace("publishEvent(pathEvent={})", pathEvent);
    }
}
