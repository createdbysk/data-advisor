package com.data_advisor.infrastructure;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotEquals;

/**
 * Test for UniqueIdGenerator
 */
public class UniqueIdGeneratorTest {
    @InjectMocks
    private UniqueIdGenerator uniqueIdGenerator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUniqueIdGenerator_WhenCalledMultipleTimes_ThenReturnDifferentValuesEachTime() {
        // GIVEN
        final UniqueIdGenerator uniqueIdGenerator = this.uniqueIdGenerator;

        // WHEN
        final String firstId = uniqueIdGenerator.generate();
        final String secondId = uniqueIdGenerator.generate();

        // THEN
        assertNotEquals(firstId, secondId);
    }
}