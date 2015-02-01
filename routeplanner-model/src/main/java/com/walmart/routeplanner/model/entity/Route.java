package com.walmart.routeplanner.model.entity;

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

    @GraphId
    private Long id;

    @StartNode
    private Point start;

    @EndNode
    private Point end;

    private Double cost;

    public Route() {
    }

    /**
     * @param start Origin point
     * @param end Destination point
     * @param cost Cost of the route
     */
    public Route(Point start, Point end, Double cost) {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public Double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("start", start)
                .append("end", end)
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
