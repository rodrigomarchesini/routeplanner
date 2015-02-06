package com.walmart.routeplanner.services.map.path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walmart.routeplanner.domain.model.PathInfo;
import com.walmart.routeplanner.domain.services.PointNotFoundException;
import com.walmart.routeplanner.domain.services.RouteService;
import com.walmart.routeplanner.services.map.path.exception.InvalidInputException;

/**
 * Implementation of PathService
 * 
 * @author Rodrigo Marchesini
 */
@Service
public class PathServiceImpl implements PathService {

    @Autowired
    private RouteService routeService;
    
    public PathServiceImpl() {
    }

    @Override
    public PathInfo shortestPath(String mapName, String origin, String destination, Double autonomy, Double fuelCost) 
            throws PointNotFoundException {
        checkValid(autonomy, "autonomy");
        checkValid(fuelCost, "fuelCost");

        PathInfo path = routeService.shortestPath(mapName, origin, destination);
        path.setCost(calculateCost(path, autonomy, fuelCost));
        return path;
    }

    private Double calculateCost(PathInfo path, Double autonomy, Double fuelCost) {
        return path.getLength() * fuelCost / autonomy;
    }

    private void checkValid(Double input, String name) {
        if (input == null || input <= 0d) {
            throw new InvalidInputException("Invalid " + name);
        }
    }
}
