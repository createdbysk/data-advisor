package com.data_advisor.local.application.entry_point.impl;

/**
 * Test for the LocalFileSystemFactory.
 */

import com.data_advisor.local.application.FileSystemAbstractFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertNotNull;

/**
 * Test for the LocalFileSystem class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Convention over configuration, this will automatically look for the nested @Configuration class.
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {LocalFileSystemFactoryTest.TestConfiguration.class})
public class LocalFileSystemFactoryTest {
    @Autowired
    private FileSystemAbstractFactory localFileSystemFactoryAutowired;

    @Configuration
    @ComponentScan("com.data_advisor.local.application.entry_point.impl")
    static class TestConfiguration {
    }

    @Test
    public void testLocalFileSystemFactory_CanAutowire() {
        assertNotNull(localFileSystemFactoryAutowired);
    }
}
