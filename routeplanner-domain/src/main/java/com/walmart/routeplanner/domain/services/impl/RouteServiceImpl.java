package com.walmart.routeplanner.domain.services.impl;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.domain.model.PathInfo;
import com.walmart.routeplanner.domain.model.entity.Point;
import com.walmart.routeplanner.domain.model.entity.RelationshipTypes;
import com.walmart.routeplanner.domain.model.entity.Route;
import com.walmart.routeplanner.domain.repositories.PointRepository;
import com.walmart.routeplanner.domain.services.RouteService;

@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private Neo4jTemplate template;

    @Autowired
    private PointRepository pointRepository;

    @Transactional
    @Override
    public PathInfo shortestPath(String mapName, String from, String to) {
        if (from.equals(to)) {
            return PathInfo.singlePoint(from);
        }

        Node fromNode = findNodeByPointNameAndMap(from, mapName);
        Node toNode = findNodeByPointNameAndMap(to, mapName);

        WeightedPath path = dijkstra(fromNode, toNode);
        if (path == null) {
            return PathInfo.noPath();
        }
        return extractSteps(path);
    }

    private PathInfo extractSteps(WeightedPath path) {
        PathInfo pathInfo = new PathInfo();
        for (Relationship route : path.relationships()) {
            pathInfo.addStep(getPointName(route.getStartNode()));
        }
        pathInfo.addStep(getPointName(path.lastRelationship().getEndNode()));
        pathInfo.setCost(path.weight());
        return pathInfo;
    }

    private WeightedPath dijkstra(Node fromNode, Node toNode) {
        WeightedPath path = GraphAlgoFactory
                .dijkstra(PathExpanders.forTypeAndDirection(RelationshipTypes.GOES_TO, Direction.OUTGOING), Route.PROP_COST)
                .findSinglePath(fromNode, toNode);
        return path;
    }

    private String getPointName(Node node) {
        return (String) node.getProperty("name");
    }

    private Node findNodeByPointNameAndMap(String pointName, String mapName) {
        // TODO handle point not found
        Point point = pointRepository.findByNameAndMap(pointName, mapName);
        return template.getNode(point.getId());
    }
}
