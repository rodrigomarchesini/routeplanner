package com.walmart.routeplanner.processor.map.path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walmart.routeplanner.core.model.PathInfo;
import com.walmart.routeplanner.core.services.PointNotFoundException;
import com.walmart.routeplanner.core.services.RouteService;
import com.walmart.routeplanner.processor.map.path.exception.InvalidInputException;

/**
 * Implementation of PathService
 * 
 * @author Rodrigo Marchesini
 */
@Service
public class PathServiceImpl implements PathService {

    private static final Logger logger = LoggerFactory.getLogger(PathServiceImpl.class);

    @Autowired
    private RouteService routeService;
    
    public PathServiceImpl() {
    }

    @Override
    public PathInfo shortestPath(String mapName, String origin, String destination, Double autonomy, Double fuelCost) 
            throws PointNotFoundException {
        checkValid(autonomy, "autonomy");
        checkValid(fuelCost, "fuelCost");

        logger.info("Searching shortest path map={} origin={} destination={}", mapName, origin, destination);

        PathInfo path = routeService.shortestPath(mapName, origin, destination);
        if (path.isPathExists()) {
            logger.info("Found shortest path map={} origin={} destination={} length={}",
                    mapName, origin, destination, path.getLength());

            Double cost = calculateCost(path, autonomy, fuelCost);
            logger.info("Calculated path cost map={} origin={} destination={} length={} autonomy={} fuelCost={} cost={}",
                    mapName, origin, destination, path.getLength(), autonomy, fuelCost, cost);
            path.setCost(cost);
        } else {
            logger.info("Path not found map={} origin={} destination={}",
                    mapName, origin, destination);
        }

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
