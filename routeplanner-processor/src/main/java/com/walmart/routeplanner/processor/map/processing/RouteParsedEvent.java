package com.walmart.routeplanner.processor.map.processing;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Route data, composed by origin and destination points
 * and a cost destination traverse it.
 *
 * @author Rodrigo Marchesini
 */
class RouteParsedEvent implements Serializable {
    private static final long serialVersionUID = -5760830175224329963L;

    private final String origin;
    private final String destination;
    private final Integer cost;

    /**
     * @param origin origin
     * @param destination destination
     * @param cost cost
     */
    RouteParsedEvent(String origin, String destination, Integer cost) {
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

    public static RouteParsedEvent of(String from, String to, Integer cost) {
        return new RouteParsedEvent(from, to, cost);
    }

    public String toStringLine() {
        return origin + " " + destination + " " + cost;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("origin", origin)
                .append("destination", destination)
                .append("cost", cost)
                .toString();
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
                .append(this.origin, other.getOrigin())
                .append(this.destination, other.getDestination())
                .append(this.cost, other.getCost())
                .isEquals();
    }

}
