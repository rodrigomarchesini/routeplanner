package com.walmart.routeplanner.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Route data, composed by origin and destination points
 * and a cost to traverse it.
 * 
 * @author Rodrigo Marchesini
 */
public class RouteInfo implements Serializable {

    private static final long serialVersionUID = -5760830175224329963L;

    private final String from;
    private final String to;
    private final Double cost;

    /**
     * @param from origin
     * @param to destination
     * @param cost cost
     */
    private RouteInfo(String from, String to, Double cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Double getCost() {
        return cost;
    }

    public static RouteInfo of(String from, String to, Double cost) {
        return new RouteInfo(from, to, cost);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("from", from)
                .append("to", to)
                .append("cost", cost)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(from)
                .append(to)
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
        RouteInfo other = (RouteInfo) obj;
        return new EqualsBuilder()
                .append(this.from, other.getFrom())
                .append(this.to, other.getTo())
                .append(this.cost, other.getCost())
                .isEquals();
    }

}
