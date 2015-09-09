package com.data_advisor.local.application;

import com.data_advisor.local.application.entry_point.impl.LocalFileSystem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test the application
 * Use the SpringJUnit4ClassRunner to auto-wire injected dependencies. This ensures that the dependencies don't miss
 * expected annotations. This satisfies Design Principle #2.
 *  2. Verify that the injected entities are configured correctly for dependency injection.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Verify that the ApplicationConfig injects the required objects and dependencies.
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ApplicationConfig.class})
public class ApplicationTest {
    private static final String ABSOLUTE_PATH = "/absolute_path";

    @Autowired
    private Application applicationAutowired;

    @InjectMocks
    private Application application;

    @Mock
    private LocalFileSystem localFileSystem;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testApplication_CanAutowire() {
        assertNotNull(application);
        assertNotNull(application.localFileSystem);
    }

    @Test
    public void testRun() {
        // GIVEN
        final String absolutePath = ABSOLUTE_PATH;
        final String[] args = new String[] {ABSOLUTE_PATH};
        final Application application = this.application;
        final LocalFileSystem localFileSystem = this.localFileSystem;

        // WHEN
        application.run(args);

        // THEN
        verify(localFileSystem, times(1)).addHierarchy(absolutePath);
    }
}
