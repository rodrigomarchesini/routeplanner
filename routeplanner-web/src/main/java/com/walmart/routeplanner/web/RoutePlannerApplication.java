package com.walmart.routeplanner.web;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.walmart.routeplanner.web.rest.MapRest;

/**
 * Application definition.
 * 
 * @author Rodrigo Marchesini
 */
@ApplicationPath("")
public class RoutePlannerApplication extends Application {
    private static final Set<Object> singletons = new HashSet<Object>();

    public RoutePlannerApplication() {
        singletons.add(new MapRest());
    }

    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> set = new HashSet<Class<?>>();
        return set;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
