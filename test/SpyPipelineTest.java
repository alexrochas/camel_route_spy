package br.com.dimed.integration.lib.test;

import br.com.dimed.integration.lib.SpyPipeline;
import br.com.dimed.integration.lib.stub.*;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public class SpyPipelineTest extends CamelTestSupport {

    @Test
    @DirtiesContext
    public void shouldAssertProcessorIsCalled() throws Exception {
        // given
        CamelContext mockedContext = super.createCamelContext();
        SpyedPipelineRouteStub stubedPipeline = getSpyedPipelineRouteStub();
        // when
        SpyPipeline.spy()
                .fromPipeline(stubedPipeline)
                .withContext(context)
                .addEvent(() -> template.send("direct:pipeline", new DefaultExchange(mockedContext)))
                .trace(SpyedProcessorTwoStub.class);
        // then should assert true
    }

    private SpyedPipelineRouteStub getSpyedPipelineRouteStub() {
        SpyedPipelineRouteStub stubedPipeline = new SpyedPipelineRouteStub();
        stubedPipeline.setPredicateStub(new SpyedPredicateStub());
        stubedPipeline.setPredicateTwoStub(new SpyedPredicateTwoStub());
        stubedPipeline.setProcessorStub(new SpyedProcessorStub());
        stubedPipeline.setProcessorTwoStub(new SpyedProcessorTwoStub());
        return stubedPipeline;
    }

    @Test
    @DirtiesContext
    public void shouldAssertPredicateIsCalled() throws Exception {
        // given
        CamelContext mockedContext = super.createCamelContext();
        SpyedPipelineRouteStub stubedPipeline = getSpyedPipelineRouteStub();
        // when
        SpyPipeline.spy()
                .fromPipeline(stubedPipeline)
                .withContext(context)
                .addEvent(() -> template.send("direct:pipeline", new DefaultExchange(mockedContext)))
                .trace(SpyedProcessorTwoStub.class, SpyedPredicateStub.class);
        // then should assert true
    }

    @Test
    @DirtiesContext
    public void shouldThrowExceptionBecausePredicateTwoIsNotCalled() throws Exception {
        // given
        CamelContext mockedContext = super.createCamelContext();
        SpyedPipelineRouteStub stubedPipeline = getSpyedPipelineRouteStub();
        // when
        SpyPipeline.spy()
                .fromPipeline(stubedPipeline)
                .withContext(context)
                .addEvent(() -> template.send("direct:pipeline", new DefaultExchange(mockedContext)))
                .trace(SpyedProcessorTwoStub.class, SpyedPredicateStub.class);
        // then should assert true
    }

    @Test
    @DirtiesContext
    public void shouldAssertExecutionInOrder() throws Exception {
        // given
        CamelContext mockedContext = super.createCamelContext();
        SpyedPipelineRouteStub stubedPipeline = getSpyedPipelineRouteStub();
        // when
        SpyPipeline.spy()
                .fromPipeline(stubedPipeline)
                .withContext(context)
                .addEvent(() -> template.send("direct:pipeline", new DefaultExchange(mockedContext)))
                .traceInOrder(SpyedProcessorTwoStub.class, SpyedPredicateStub.class);
        // then should assert true
    }
}


