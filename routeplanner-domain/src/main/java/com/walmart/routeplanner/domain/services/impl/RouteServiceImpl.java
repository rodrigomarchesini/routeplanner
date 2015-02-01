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

import com.walmart.routeplanner.domain.model.ShortestPathInfo;
import com.walmart.routeplanner.domain.model.entity.Point;
import com.walmart.routeplanner.domain.model.entity.RelationshipTypes;
import com.walmart.routeplanner.domain.repositories.PointRepository;
import com.walmart.routeplanner.domain.services.RouteService;

@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private Neo4jTemplate template;

    @Autowired
    private PointRepository pointRepository;

    @Override
    public ShortestPathInfo shortestPath(String mapName, String from, String to) {
        if (from.equals(to)) {
            return ShortestPathInfo.singlePoint(from);
        }

        Node fromNode = findNodeByPointNameAndMap(from, mapName);
        Node toNode = findNodeByPointNameAndMap(to, mapName);

        WeightedPath path = dijkstra(fromNode, toNode);
        if (path == null) {
            return ShortestPathInfo.noPath();
        }
        return extractSteps(path);
    }

    private ShortestPathInfo extractSteps(WeightedPath path) {
        ShortestPathInfo pathInfo = new ShortestPathInfo();
        for (Relationship route : path.relationships()) {
            pathInfo.addStep(getPointName(route.getStartNode()));
        }
        pathInfo.addStep(getPointName(path.lastRelationship().getEndNode()));
        pathInfo.setTotalCost(path.weight());
        return pathInfo;
    }

    private WeightedPath dijkstra(Node fromNode, Node toNode) {
        WeightedPath path = GraphAlgoFactory
                .dijkstra(PathExpanders.forTypeAndDirection(RelationshipTypes.GOES_TO, Direction.OUTGOING), "cost")
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
