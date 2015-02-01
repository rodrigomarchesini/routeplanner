package com.walmart.routeplanner.model;

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

    public void addStep(String pointName) {
        steps.add(pointName);
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
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
}
