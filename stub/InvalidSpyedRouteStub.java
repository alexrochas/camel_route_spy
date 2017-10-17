package br.com.dimed.integration.lib.stub;

import org.apache.camel.builder.RouteBuilder;

public class InvalidSpyedRouteStub extends RouteBuilder {

    private SpyedProcessorStub processorStub;

    public InvalidSpyedRouteStub(SpyedProcessorStub processorStub) {
        this.processorStub = processorStub;
    }

    public SpyedProcessorStub getProcessorStub() {
        return processorStub;
    }

    public void setProcessorStub(SpyedProcessorStub processorStub) {
        this.processorStub = processorStub;
    }

    @Override
    public void configure() throws Exception {

    }
}
