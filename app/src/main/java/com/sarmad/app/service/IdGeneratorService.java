package com.sarmad.app.service;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorService {
    private final AtomicLong counter = new AtomicLong();

    public Integer getNextId() {
        return (int) counter.incrementAndGet();
    }
}
