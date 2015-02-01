package com.walmart.routeplanner.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Shortest path data, composed by a sequence of steps
 * between points and a total cost.
 * 
 * @author Rodrigo Marchesini
 */
public class ShortestPathInfo {

    private List<RouteInfo> steps;
    private Double totalCost;

    public ShortestPathInfo() {
        steps = new ArrayList<RouteInfo>();
        totalCost = 0d;
    }

    public boolean exists() {
        return getSteps().size() > 0;
    }

    private List<RouteInfo> getSteps() {
        return steps;
    }

    public void addStep(RouteInfo route) {
        steps.add(route);
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
        StringBuilder sb = new StringBuilder();
        for (Iterator<RouteInfo> it = steps.iterator(); it.hasNext();) {
            RouteInfo route = it.next();
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(route.getFrom());
            if (!it.hasNext()) {
                sb.append(" ");
                sb.append(route.getTo());
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (RouteInfo route : getSteps()) {
            if (sb.length() > 0) {
                sb.append(" -> ");
            }
            sb.append(route.getFrom())
                    .append(" --(")
                    .append(route.getCost())
                    .append(")-> ")
                    .append(route.getTo());
        }
        return sb.toString();
    }
}
