package br.com.dimed.integration.lib;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SpyPipelineElement {

    private RouteBuilder pipeline;
    private CamelContext context;
    private Class route;
    private List<Supplier> events;

    private SpyPipelineElement() {
        this.events = new ArrayList<>();
    }

    public static SpyPipelineElement spy() {
        return new SpyPipelineElement();
    }

    public SpyPipelineElement route(Class route) {
        this.route = route;
        return this;
    }

    public SpyPipelineElement fromPipeline(RouteBuilder pipeline) {
        this.pipeline = pipeline;
        return this;
    }

    public SpyPipelineElement withContext(CamelContext context) {
        this.context = context;
        return this;
    }

    public SpyPipelineElement addEvent(Supplier event) {
        this.events.add(event);
        return this;
    }

    public void thenAssert(Consumer assertion) throws Exception {
        if (this.route == null) throw new IllegalStateException("Route must be defined.");
        if (this.pipeline == null) throw new IllegalStateException("Pipeline must be defined");
        if (this.context == null) throw new IllegalStateException("Context must be defined");

        Class<? extends RouteBuilder> clazz = pipeline.getClass();
        RouteBuilder routeBuilder = clazz.newInstance();

        Stream.of(clazz.getDeclaredFields())
                .forEach(f -> {
                    f.setAccessible(true);
                    try {
                        f.set(routeBuilder, f.get(pipeline));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        Field field = Stream.of(clazz.getDeclaredFields())
                .filter(f -> f.getType().getName().equals(route.getName()))
                .map(f -> {
                    f.setAccessible(true);
                    return f;
                })
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException());

        Object gettedObject = field.get(routeBuilder);
        Object spyedObject = Mockito.spy(gettedObject);
        field.set(routeBuilder, spyedObject);
        context.addRoutes(routeBuilder);

        runEvents();
        assertion.accept(spyedObject);
    }

    private void runEvents() {
        for (Supplier s : events) {
            s.get();
        }
    }

}
