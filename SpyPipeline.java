package br.com.dimed.integration.lib;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

public class SpyPipeline {

    private RouteBuilder pipeline;
    private CamelContext context;
    private List<Supplier> events;

    private SpyPipeline() {
        this.events = new ArrayList<>();
    }

    public static SpyPipeline spy() {
        return new SpyPipeline();
    }

    public SpyPipeline fromPipeline(RouteBuilder pipeline) {
        this.pipeline = pipeline;
        return this;
    }

    public SpyPipeline withContext(CamelContext context) {
        this.context = context;
        return this;
    }

    public SpyPipeline addEvent(Supplier event) {
        this.events.add(event);
        return this;
    }

    private void runEvents() {
        for (Supplier s : events) {
            s.get();
        }
    }

    public void trace(Class... toSpy) throws Exception {
        if (this.pipeline == null) throw new IllegalStateException("Pipeline must be defined");
        if (this.context == null) throw new IllegalStateException("Context must be defined");

        List<Class> classesToSpy = Arrays.asList(toSpy);
        Class<? extends RouteBuilder> clazz = pipeline.getClass();
        RouteBuilder routeBuilder = clazz.newInstance();

        List<Object> spyedObjects = Stream.of(clazz.getDeclaredFields())
                .map(field -> {
                    field.setAccessible(true);
                    return field;
                })
                .map(field -> {
                    try {
                        Object spyedField = Mockito.spy(field.get(pipeline));
                        field.set(routeBuilder, spyedField);
                        return spyedField;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(object -> object != null)
                .collect(Collectors.toList());
        context.addRoutes(routeBuilder);

        runEvents();
        spyedObjects.stream()
                .filter(spyObject -> classesToSpy.contains(spyObject.getClass().getSuperclass()))
                .forEach(spyObject -> {
                    try {
                        if (spyObject instanceof Processor) {
                            verify(((Processor) spyObject), atLeastOnce()).process(Mockito.any(Exchange.class));
                        } else {
                            verify(((Predicate) spyObject), atLeastOnce()).matches(Mockito.any(Exchange.class));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void traceInOrder(Class... toSpy) throws Exception {
        if (this.pipeline == null) throw new IllegalStateException("Pipeline must be defined");
        if (this.context == null) throw new IllegalStateException("Context must be defined");

        List<Class> classesToSpy = Arrays.asList(toSpy);
        Class<? extends RouteBuilder> clazz = pipeline.getClass();
        List<Object> spyedObjects = Stream.of(classesToSpy)
                .flatMap(c -> c.stream())
                .map(c -> {
                    return Stream.of(clazz.getDeclaredFields())
                            .filter(cz -> cz.getType().equals(c)).findFirst().get();
                })
                .filter(f -> classesToSpy.contains(f.getType()))
                .map(field -> {
                    field.setAccessible(true);
                    return field;
                })
                .map(field -> {
                    try {
                        Object spyedField = Mockito.spy(field.get(pipeline));
                        field.set(pipeline, spyedField);
                        return spyedField;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(object -> object != null)
                .collect(Collectors.toList());
        context.addRoutes(pipeline);

        InOrder inOrder = Mockito.inOrder(spyedObjects.toArray());

        runEvents();
        spyedObjects.stream()
                .filter(spyObject -> classesToSpy.contains(spyObject.getClass().getSuperclass()))
                .forEachOrdered(spyObject -> {
                    try {
                        if (spyObject instanceof Processor) {
                            inOrder.verify(((Processor) spyObject)).process(Mockito.any(Exchange.class));
                        } else {
                            inOrder.verify(((Predicate) spyObject)).matches(Mockito.any(Exchange.class));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

}
