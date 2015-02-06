package com.walmart.routeplanner.core.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Shortest path data, composed by a sequence of steps
 * between points and a total length.
 *
 * @author Rodrigo Marchesini
 */
public class PathInfo {

    private Double length;
    private Double cost;
    private List<String> steps;

    public PathInfo() {
        steps = new ArrayList<String>();
        length = 0d;
        cost = 0d;
    }

    public List<String> getPoints() {
        return steps;
    }

    public PathInfo addStep(String pointName) {
        steps.add(pointName);
        return this;
    }

    public Double getLength() {
        return length;
    }

    public PathInfo setLength(Double length) {
        this.length = length;
        return this;
    }

    public Double getCost() {
        return cost;
    }

    public PathInfo setCost(Double cost) {
        this.cost = cost;
        return this;
    }

    public boolean isPathExists() {
        return !this.steps.isEmpty();
    }

    /**
     * String representation of the path in the form "A B C D".
     *
     * @return Route points separated by space
     */
    @JsonIgnore
    public String getPointsAsLineString() {
        return StringUtils.join(steps, " ");
    }

    @Override
    public String toString() {
        return getPointsAsLineString() + " " + getLength();
    }

    public static PathInfo noPath() {
        return new PathInfo();
    }

    public static PathInfo singlePoint(String pointName) {
        return new PathInfo()
                .addStep(pointName)
                .setLength(0d);
    }
}
