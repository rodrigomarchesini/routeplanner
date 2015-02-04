package com.walmart.routeplanner.services.map.processor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Data holder for a parsed route of a map.
 *
 * @author Rodrigo Marchesini
 */
public class RouteParsedEvent {

    private final String origin;
    private final String destination;
    private final Integer cost;

    public RouteParsedEvent(String origin, String destination, Integer cost) {
        this.origin = origin;
        this.destination = destination;
        this.cost = cost;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public Integer getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return origin + " " + destination + " " + cost;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(origin)
                .append(destination)
                .append(cost)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RouteParsedEvent other = (RouteParsedEvent) obj;
        return new EqualsBuilder()
                .append(origin, other.origin)
                .append(destination, other.destination)
                .append(cost, other.cost)
                .isEquals();
    }

}
