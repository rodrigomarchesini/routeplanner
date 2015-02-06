package com.walmart.routeplanner.domain.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

/**
 * Representation of a route between two map points, with an associated cost.
 *
 * @author Rodrigo Marchesini
 */
@RelationshipEntity(type = "GOES_TO")
public class Route {

    public static final String PROP_COST = "cost";

    @GraphId
    private Long id;

    @StartNode
    private Point origin;

    @EndNode
    private Point destination;

    private Integer cost;

    public Route() {
    }

    /**
     * @param origin Origin point
     * @param destination Destination point
     * @param cost Cost of the route
     */
    public Route(Point origin, Point destination, Integer cost) {
        this.origin = origin;
        this.destination = destination;
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public Point getDestination() {
        return destination;
    }

    public void setDestination(Point destination) {
        this.destination = destination;
    }

    public Integer getCost() {
        return cost;
    }
    
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public static Route of(Point origin, Point destination, Integer cost) {
        return new Route(origin, destination, cost);
    }

    public String toStringLine() {
        return origin.getName() + " " + destination.getName() + " " + cost;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("origin", origin)
                .append("destination", destination)
                .append("cost", cost)
                .toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Route other = (Route) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
