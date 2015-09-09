package com.data_advisor.local.application;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

/**
 * Test class Main
 */
public class MainTest {
    private Main main;

    @Mock
    private AnnotationConfigApplicationContext annotationConfigApplicationContext;

    @Mock
    private Application application;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        main = new Main(annotationConfigApplicationContext);
    }

    @Test
    public void testInitialize() {
        // GIVEN
        final String[] args = new String[]{};
        given(annotationConfigApplicationContext.getBean(Application.class)).willReturn(application);

        // WHEN
        main.loadAndRun(args);

        // THEN
        InOrder inOrder = inOrder(annotationConfigApplicationContext, application);
        inOrder.verify(annotationConfigApplicationContext, times(1)).register(ApplicationConfig.class);
        inOrder.verify(annotationConfigApplicationContext, times(1)).refresh();
        inOrder.verify(annotationConfigApplicationContext, times(1)).getBean(Application.class);
        inOrder.verify(application, times(1)).run(args);
    }
}
