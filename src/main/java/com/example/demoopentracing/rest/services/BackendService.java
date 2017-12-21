package com.example.demoopentracing.rest.services;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;

import javax.inject.Inject;
import java.util.Random;

/**
 * @author Pavol Loffay
 */
public class BackendService {
    @Inject
    private io.opentracing.Tracer tracer;

    public String action() throws InterruptedException {
        int random = new Random()
                .nextInt(200);

        Tracer.SpanBuilder sb = tracer.buildSpan("blah");
        ActiveSpan span = sb.startActive();
        try {
            action2(random);
            Thread.sleep(random);
        } finally {
            span.close();
        }

        return String.valueOf(random);
    }

    private void action2(int value) {
        ActiveSpan activeSpan = tracer.activeSpan();
        activeSpan.setTag("action2", "data-" + value);
    }
}

