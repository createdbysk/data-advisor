package com.data_advisor.local.event.file_system;

import com.data_advisor.local.application.ApplicationConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test for all PathEvent and derived events.
 * Use the SpringJUnit4ClassRunner to auto-wire injected dependencies. This ensures that the dependencies don't miss
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
    private PathEventPublisher pathEventPublisher;

    @Mock
    private ApplicationEventPublisher applicationEventPulisher;

    @Mock
    Path path;

    @Mock
    private PathEvent pathEvent;

    /** Use this class to test the abstract PathEvent */
    private class TestPathEvent extends PathEvent {
        public TestPathEvent(Path path, BasicFileAttributes basicFileAttributes) {
            super(path, basicFileAttributes);
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFileSystemEvent_Exists() {
        // GIVEN
        final Path path = this.path;
        final BasicFileAttributes basicFileAttributes = this.basicFileAttributes;

        // WHEN
        PathEvent pathEvent = new TestPathEvent(path, basicFileAttributes);

        // THEN
        assertEquals(path, pathEvent.getPath());
        assertEquals(basicFileAttributes, pathEvent.getBasicFileAttributes());
    }

    @Test
    public void testFileSystemEventPublisher_CanAutowire() {
        assertNotNull(pathEventPublisherAutowired);
        assertNotNull(pathEventPublisherAutowired.applicationEventPublisher);
    }

    @Test
    public void testFileSystemEventPublisher_WhenPublish_ThenPublishesFileSystemEvent() {
        // GIVEN
        final PathEvent pathEvent = this.pathEvent;

        // WHEN
        pathEventPublisher.publish(pathEvent);

        // THEN
        verify(applicationEventPulisher, times(1)).publishEvent(pathEvent);
    }
}
