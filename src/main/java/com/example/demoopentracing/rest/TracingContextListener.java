package com.example.demoopentracing.rest;

import com.uber.jaeger.reporters.RemoteReporter;
import com.uber.jaeger.samplers.ProbabilisticSampler;
import com.uber.jaeger.samplers.Sampler;
import com.uber.jaeger.senders.Sender;
import com.uber.jaeger.senders.UdpSender;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/**
 * @author Pavol Loffay
 */
@WebListener
public class TracingContextListener implements ServletContextListener {
    private static final Map<String, String> envs = System.getenv();

    private static final String JAEGER_AGENT_HOST = envs.getOrDefault("JAEGER_AGENT_HOST", "localhost");
    private static final Integer JAEGER_FLUSH_INTERVAL = new Integer(envs.getOrDefault("JAEGER_FLUSH_INTERVAL", "100"));
    private static final Integer JAEGER_MAX_PACKET_SIZE = new Integer(envs.getOrDefault("JAEGER_MAX_PACKET_SIZE", "0"));
    private static final Integer JAEGER_MAX_QUEUE_SIZE = new Integer(envs.getOrDefault("JAEGER_MAX_QUEUE_SIZE", "50"));
    private static final Double JAEGER_SAMPLING_RATE = new Double(envs.getOrDefault("JAEGER_SAMPLING_RATE", "1.0"));
    private static final Integer JAEGER_UDP_PORT = new Integer(envs.getOrDefault("JAEGER_UDP_PORT", "5775"));
    private static final String TEST_SERVICE_NAME = envs.getOrDefault("TEST_SERVICE_NAME", "swarm-opentracing-demo");

    @Inject
    private io.opentracing.Tracer tracer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        GlobalTracer.register(tracer);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}

    @Produces
    @Singleton
    public static io.opentracing.Tracer jaegerTracer() {
        Sender sender = sender = new UdpSender(JAEGER_AGENT_HOST, JAEGER_UDP_PORT, JAEGER_MAX_PACKET_SIZE);

        RemoteReporter remoteReporter = new RemoteReporter.Builder()
                .withSender(sender)
                .withFlushInterval(JAEGER_FLUSH_INTERVAL)
                .withMaxQueueSize(JAEGER_MAX_QUEUE_SIZE)
                .build();

        Sampler sampler = new ProbabilisticSampler(JAEGER_SAMPLING_RATE);

        Tracer tracer = new com.uber.jaeger.Tracer.Builder(TEST_SERVICE_NAME)
                .withReporter(remoteReporter)
                .withSampler(sampler)
                .build();

        return tracer;
    }
}
