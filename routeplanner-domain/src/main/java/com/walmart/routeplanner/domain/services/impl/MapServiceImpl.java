package com.walmart.routeplanner.domain.services.impl;

import java.util.Collections;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.domain.model.entity.MapInfo;
import com.walmart.routeplanner.domain.model.entity.Point;
import com.walmart.routeplanner.domain.model.entity.RelationshipTypes;
import com.walmart.routeplanner.domain.model.entity.Route;
import com.walmart.routeplanner.domain.repositories.PointRepository;
import com.walmart.routeplanner.domain.services.MapService;

/**
 * MapService implementation
 *
 * @author Rodrigo Marchesini
 */
@Service
public class MapServiceImpl implements MapService {

    @Autowired
    private Neo4jTemplate template;

    @Autowired
    private PointRepository pointRepository;

    /*
     * (non-Javadoc)
     * @see com.walmart.routeplanner.domain.services.MapService#createPoints(com.walmart.routeplanner.domain.model.entity.MapInfo)
     */
    @Override
    @Transactional
    public void createPoints(MapInfo map) {
        MapCreationContext context = new MapCreationContext();

        for (Route route : map.getRoutes()) {
            Point origin = createOrLoadPoint(route.getOrigin(), context);
            route.setOrigin(origin);

            Point destination = createOrLoadPoint(route.getDestination(), context);
            route.setDestination(destination);
        }
    }
    
    private Point createOrLoadPoint(Point point, MapCreationContext context) {
        Point existingPoint = context.get(point);
        if (existingPoint != null) {
            return existingPoint;
        }

        Point newPoint = this.template.save(point);
        context.put(newPoint);
        return newPoint;
    }
    
    /*
     * (non-Javadoc)
     * @see com.walmart.routeplanner.domain.services.MapService#createRoutes(com.walmart.routeplanner.domain.model.entity.MapInfo, int, int)
     */
    @Override
    @Transactional
    public void createRoutes(MapInfo map, int fromIndex, int toIndex) {
        for (Route route : map.getRoutes().subList(fromIndex, toIndex)) {
            Point origin = route.getOrigin();
            Point destination = route.getDestination();
            Node originNode = template.getNode(origin.getId());
            Node destinationNode = template.getNode(destination.getId());

            Relationship routeRelationship = template.createRelationshipBetween(
                    originNode,
                    destinationNode,
                    RelationshipTypes.GOES_TO.name(),
                    Collections.singletonMap(Route.PROP_COST, (Object) route.getCost()));
            route.setId(routeRelationship.getId());

            // slower
            // template.createRelationshipBetween(origin, destination, Route.class, "GOES_TO", false);

            // slowest
            // origin.goesTo(destination, route.getCost());
            // this.template.save(origin);
        }
    }

    @Transactional
    @Override
    public void deleteAllPoints(String mapName) {
        pointRepository.deleteAllPointsByMap(mapName);
    }

    @Transactional
    @Override
    public void deleteAllRoutes(String mapName) {
        pointRepository.deleteAllRoutesByMap(mapName);        
    }

}
