package com.example.demoopentracing.rest.services;

import io.opentracing.Scope;
import io.opentracing.Span;

import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Pavol Loffay
 */
public class BackendService {
    public static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private static final Logger logger = LoggerFactory.getLogger(BackendService.class.getName());

    @Inject
    private io.opentracing.Tracer tracer;

    public String action() throws InterruptedException {
        int random = RANDOM.nextInt(200);

        Span span = tracer.buildSpan("action")
                .start();
        try {
            action2(random);
            Thread.sleep(random);
        } catch (InterruptedException ie) {
            logger.warn("Got Interrupted Exception", ie);
        }
        span.finish();

        return String.valueOf(random);
    }

    private void action2(int value) {
        Scope scope = tracer.scopeManager().active();
        Span activeSpan = scope.span();
        activeSpan.setTag("action2", "data-" + value);
    }
}

