package com.alex.camelroutespy.lib.stub;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

public class SpyedPredicateTwoStub implements Predicate {

    @Override
    public boolean matches(Exchange exchange) {
        return false;
    }
}
