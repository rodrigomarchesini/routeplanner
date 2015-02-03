package com.walmart.routeplanner.services.map.processor;

import com.walmart.routeplanner.services.map.processor.exception.MalformedMapException;

/**
 * Parser for a map route.
 * A map route is composed by three tokens separated by space:
 * origin destination cost
 * Cost must be a positive integer.
 *
 * @author Rodrigo Marchesini
 */
public class MapRouteParser {

    private static final int LINE_TOKENS = 3;
    private static final int ORIGIN_INDEX = 0;
    private static final int DESTINATION_INDEX = 1;
    private static final int COST_INDEX = 2;

    public MapRouteParser() {
    }

    /**
     * Parse a map route String, if possible, splitting into origin, destination
     * and cost value. Otherwise throws exception.
     *
     * @param route Route as String
     * @param routeNumber Route index used for logging/error handling
     * @return Event composed by parsed route parts
     */
    public MapRouteParsedEvent parseRoute(String route, int routeNumber) {
        String[] tokens = split(route, routeNumber);

        String origin = tokens[ORIGIN_INDEX];
        String destination = tokens[DESTINATION_INDEX];
        Integer cost = parseCostValue(routeNumber, tokens);

        return new MapRouteParsedEvent(origin, destination, cost);
    }

    private Integer parseCostValue(int routeNumber, String[] tokens) {
        Integer cost;
        try {
            cost = Integer.parseInt(tokens[COST_INDEX]);
        } catch (NumberFormatException e) {
            throw new MalformedMapException("Invalid cost value at route #" + routeNumber);
        }

        if (cost < 0) {
            throw new MalformedMapException("Negative cost value at route #" + routeNumber);
        }
        return cost;
    }

    private String[] split(String route, int routeNumber) {
        String[] tokens = route.split(" ");
        if (tokens.length != LINE_TOKENS) {
            throw new MalformedMapException("Malformed route #" + routeNumber);
        }
        return tokens;
    }
}