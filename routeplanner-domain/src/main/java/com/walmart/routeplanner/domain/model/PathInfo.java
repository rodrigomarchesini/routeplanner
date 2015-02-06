package com.walmart.routeplanner.domain.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Shortest path data, composed by a sequence of steps
 * between points and a total cost.
 *
 * @author Rodrigo Marchesini
 */
public class PathInfo {

    private Double cost;
    private List<String> steps;

    public PathInfo() {
        steps = new ArrayList<String>();
        cost = 0d;
    }

    public boolean exists() {
        return getSteps().size() > 0;
    }

    private List<String> getSteps() {
        return steps;
    }

    public PathInfo addStep(String pointName) {
        steps.add(pointName);
        return this;
    }

    public Double getCost() {
        return cost;
    }

    public PathInfo setCost(Double cost) {
        this.cost = cost;
        return this;
    }

    /**
     * String representation of the path in the form "A B C D".
     *
     * @return Route points separated by space
     */
    public String getPoints() {
        return StringUtils.join(steps, " ");
    }

    @Override
    public String toString() {
        return getPoints() + " " + getCost();
    }

    public static PathInfo noPath() {
        return new PathInfo();
    }

    public static PathInfo singlePoint(String pointName) {
        return new PathInfo()
                .addStep(pointName)
                .setCost(0d);
    }
}