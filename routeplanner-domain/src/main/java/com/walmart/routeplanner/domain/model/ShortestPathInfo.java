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
public class ShortestPathInfo {

    private Double totalCost;
    private List<String> steps;

    public ShortestPathInfo() {
        steps = new ArrayList<String>();
        totalCost = 0d;
    }

    public boolean exists() {
        return getSteps().size() > 0;
    }

    private List<String> getSteps() {
        return steps;
    }

    public ShortestPathInfo addStep(String pointName) {
        steps.add(pointName);
        return this;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public ShortestPathInfo setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
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
        return getPoints() + " " + getTotalCost();
    }

    public static ShortestPathInfo noPath() {
        return new ShortestPathInfo();
    }

    public static ShortestPathInfo singlePoint(String pointName) {
        return new ShortestPathInfo()
                .addStep(pointName)
                .setTotalCost(0d);
    }
}
