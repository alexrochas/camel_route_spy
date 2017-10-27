package com.alex.camelroutespy.lib.stub;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class SpyedProcessorTwoStub implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println(exchange);
    }
}
