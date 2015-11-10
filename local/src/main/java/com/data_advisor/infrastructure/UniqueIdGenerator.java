package com.data_advisor.infrastructure;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Generates unique Ids.
 */
@Service
public class UniqueIdGenerator {
    public String generate() {
        final UUID uuid = UUID.randomUUID();
        final String uuidString = uuid.toString();
        return uuidString;
    }
}
