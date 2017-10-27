package com.alex.camelroutespy.lib.stub;

import org.apache.camel.builder.RouteBuilder;

public class SpyedRouteStub extends RouteBuilder {

    private SpyedProcessorStub processorStub;

    public SpyedRouteStub() {
        super();
    }

    public SpyedRouteStub(SpyedProcessorStub processorStub) {
        this.processorStub = processorStub;
    }

    public SpyedProcessorStub getSpyedProcessorStub() {
        return processorStub;
    }

    public void setSpyedProcessorStub(SpyedProcessorStub processorStub) {
        this.processorStub = processorStub;
    }

    @Override
    public void configure() throws Exception {

    }
}
