package com.walmart.routeplanner.services.map.processor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

/**
 * Common stuff for map processing/importing tests.
 *
 * @author Rodrigo Marchesini
 */
public class BaseMapProcessingTest {

    public BaseMapProcessingTest() {
        super();
    }

    protected InputStream toInputStream(String routes) {
        return new ByteArrayInputStream(routes.getBytes());
    }

    protected InputStream toInputStream(String[] routes) {
        return new ByteArrayInputStream(StringUtils.join(routes, "\n").getBytes());
    }

}