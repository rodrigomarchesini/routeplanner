package com.walmart.routeplanner.services.map.processor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import com.walmart.routeplanner.domain.model.entity.Point;
import com.walmart.routeplanner.domain.model.entity.Route;

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

    protected Route r(String map, String origin, String destination, Integer cost) {
        return new Route(p(origin, map), p(destination, map), cost);
    }

    protected Point p(String name, String map) {
        return new Point(name, map);
    }
}