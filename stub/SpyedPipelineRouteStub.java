package br.com.dimed.integration.lib.stub;

import org.apache.camel.builder.RouteBuilder;

public class SpyedPipelineRouteStub extends RouteBuilder {

    private SpyedProcessorStub processorStub;
    private SpyedPredicateStub predicateStub;
    private SpyedProcessorTwoStub processorTwoStub;
    private SpyedPredicateTwoStub predicateTwoStub;

    @Override
    public void configure() throws Exception {
        from("direct:pipeline")
                .process(processorTwoStub)
                .filter(predicateStub)
                .process(processorStub)
                .end();
    }

    public SpyedProcessorStub getProcessorStub() {
        return processorStub;
    }

    public void setProcessorStub(SpyedProcessorStub processorStub) {
        this.processorStub = processorStub;
    }

    public SpyedPredicateStub getPredicateStub() {
        return predicateStub;
    }

    public void setPredicateStub(SpyedPredicateStub predicateStub) {
        this.predicateStub = predicateStub;
    }

    public SpyedProcessorTwoStub getProcessorTwoStub() {
        return processorTwoStub;
    }

    public void setProcessorTwoStub(SpyedProcessorTwoStub processorTwoStub) {
        this.processorTwoStub = processorTwoStub;
    }

    public SpyedPredicateTwoStub getPredicateTwoStub() {
        return predicateTwoStub;
    }

    public void setPredicateTwoStub(SpyedPredicateTwoStub predicateTwoStub) {
        this.predicateTwoStub = predicateTwoStub;
    }
}
