package br.com.dimed.integration.lib.test;

import br.com.dimed.integration.lib.SpyPipeline;
import br.com.dimed.integration.lib.SpyPipelineElement;
import br.com.dimed.integration.lib.stub.SpyedProcessorStub;
import br.com.dimed.integration.lib.stub.SpyedRouteStub;
import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;

public class SpyPipelineElementTest {

    @Test
    public void shouldReturnNewInstanceOfSpyPipelineElement() {
        assertTrue(SpyPipelineElement.spy() instanceof SpyPipelineElement);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenPipelineIsNotDefined() throws Exception {
        // given
        Class mockedRoute = Processor.class;
        CamelContext mockedContext = Mockito.mock(CamelContext.class);
        // when
        SpyPipelineElement.spy()
                .route(mockedRoute)
                .withContext(mockedContext)
                .thenAssert((spy) -> {
                    assertTrue(true);
                });
        // then should throw exception
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenRouteIsNotDefined() throws Exception {
        // given
        CamelContext mockedContext = Mockito.mock(CamelContext.class);
        RouteBuilder mockedPipeline = Mockito.mock(RouteBuilder.class);
        // when
        SpyPipelineElement.spy()
                .fromPipeline(mockedPipeline)
                .withContext(mockedContext)
                .thenAssert((spy) -> {
                    assertTrue(true);
                });
        // then should throw exception
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenContextIsNotDefined() throws Exception {
        // given
        Class mockedRoute = Processor.class;
        RouteBuilder mockedPipeline = Mockito.mock(RouteBuilder.class);
        // when
        SpyPipelineElement.spy()
                .route(mockedRoute)
                .fromPipeline(mockedPipeline)
                .thenAssert((spy) -> {
                    assertTrue(true);
                });
        // then should throw exception
    }

    @Test
    public void shouldAssertInstanceOfSpyedProcessor() throws Exception {
        // given
        Class route = SpyedProcessorStub.class;
        CamelContext mockedContext = Mockito.mock(CamelContext.class);
        SpyedRouteStub stubedPipeline = new SpyedRouteStub(new SpyedProcessorStub());
        // when
        SpyPipelineElement.spy()
                .route(route)
                .fromPipeline(stubedPipeline)
                .withContext(mockedContext)
                .thenAssert((spy) -> {
                    assertTrue(spy instanceof SpyedProcessorStub);
                });
        // then should assert true
    }
}

