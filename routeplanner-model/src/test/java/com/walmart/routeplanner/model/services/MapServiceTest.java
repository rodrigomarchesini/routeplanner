package com.walmart.routeplanner.model.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.model.MapInfo;
import com.walmart.routeplanner.model.RouteInfo;
import com.walmart.routeplanner.model.ShortestPathInfo;
import com.walmart.routeplanner.model.entity.Point;
import com.walmart.routeplanner.model.repositories.PointRepository;

/**
 * Tests for MapService
 *
 * @author Rodrigo Marchesini
 */
@ContextConfiguration(locations = "classpath:/spring-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MapServiceTest {

    @Autowired
    private Neo4jTemplate template;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointService pointService;

    @Autowired
    private MapService mapService;

    @Rollback(false)
    @BeforeTransaction
    public void cleanUpGraph() {
        Neo4jHelper.cleanDb(template);
    }

    @Test
    public void deleteOnlySpecifiedMap() {
        final String mapName1 = "map1";
        MapInfo map1 = createSimpleMapInfo(mapName1);
        mapService.createMap(map1);

        final String mapName2 = "map2";
        MapInfo map2 = createSimpleMapInfo(mapName2);
        mapService.createMap(map2);

        Assert.assertEquals(map1.getPoints().size(), pointService.countByMap(mapName1));
        Assert.assertEquals(map2.getPoints().size(), pointService.countByMap(mapName2));

        mapService.deleteMap(mapName1);

        Assert.assertEquals(0, pointService.countByMap(mapName1));
        Assert.assertEquals(map2.getPoints().size(), pointService.countByMap(mapName2));
    }

    @Test
    public void simpleMapCreation() {
        String mapName = "map1";
        MapInfo map = createSimpleMapInfo(mapName);

        mapService.createMap(map);
        Assert.assertEquals(map.getPoints().size(), pointService.countByMap(mapName));
    }

    @Test
    public void mapNameIndexing() {
        final String mapName1 = "map1";
        MapInfo map1 = createSimpleMapInfo(mapName1);
        mapService.createMap(map1);

        final String mapName2 = "map2";
        MapInfo map2 = createSimpleMapInfo(mapName2);
        mapService.createMap(map2);

        Point a1 = pointService.findPoint("A", mapName1);
        Point a2 = pointService.findPoint("A", mapName2);

        Assert.assertEquals(mapName1, a1.getMapName());
        Assert.assertEquals(mapName2, a2.getMapName());
    }

    @Test
    public void createMapWithDuplicatedRoute() {
        String mapName = "map1";
        MapInfo map = createDuplicatedRouteMapInfo(mapName);

        mapService.createMap(map);
        Assert.assertEquals(map.getPoints().size(), pointService.countByMap(mapName));

        Point a = pointService.findPoint("A", mapName);
        Point b = pointService.findPoint("B", mapName);
        Point e = pointService.findPoint("E", mapName);

        Relationship ab = template.getRelationshipBetween(a, b, "GOES_TO");
        Relationship be = template.getRelationshipBetween(b, e, "GOES_TO");

        Assert.assertEquals(0, Double.compare(10d, (Double) ab.getProperty("cost")));
        Assert.assertEquals(0, Double.compare(50d, (Double) be.getProperty("cost")));
    }

    @Test
    public void shortestPathInSimpleMap() {
        String mapName = "map1";
        MapInfo map = createSimpleMapInfo(mapName);

        mapService.createMap(map);

        ShortestPathInfo path = mapService.shortestPath(mapName, "A", "D");

        Assert.assertTrue(path.exists());
        Assert.assertEquals(0, Double.compare(25d, path.getTotalCost()));
        Assert.assertEquals("A B D", path.getPoints());
    }

    @Test
    public void shortestPathNotFoundInDisconnectedMap() {
        String mapName = "map1";
        MapInfo map = createDisconnectedMapInfo(mapName);

        mapService.createMap(map);

        ShortestPathInfo path;
        path = mapService.shortestPath(mapName, "A", "B");
        Assert.assertFalse(path.exists());
        Assert.assertEquals(0, Double.compare(0d, path.getTotalCost()));
        Assert.assertTrue(path.getPoints().isEmpty());

        path = mapService.shortestPath(mapName, "C", "A");
        Assert.assertFalse(path.exists());
        Assert.assertEquals(0, Double.compare(0d, path.getTotalCost()));
        Assert.assertTrue(path.getPoints().isEmpty());

        path = mapService.shortestPath(mapName, "A", "E");
        Assert.assertFalse(path.exists());
        Assert.assertEquals(0, Double.compare(0d, path.getTotalCost()));
        Assert.assertTrue(path.getPoints().isEmpty());
    }

    private MapInfo createSimpleMapInfo(String mapName) {
        List<RouteInfo> routes = new ArrayList<RouteInfo>();
        routes.add(RouteInfo.of("A", "B", 10d));
        routes.add(RouteInfo.of("B", "D", 15d));
        routes.add(RouteInfo.of("A", "C", 20d));
        routes.add(RouteInfo.of("C", "D", 30d));
        routes.add(RouteInfo.of("B", "E", 50d));
        routes.add(RouteInfo.of("D", "E", 30d));
        MapInfo map = new MapInfo(mapName, routes);
        return map;
    }

    private MapInfo createDuplicatedRouteMapInfo(String mapName) {
        List<RouteInfo> routes = new ArrayList<RouteInfo>();
        routes.add(RouteInfo.of("A", "B", 10d));
        routes.add(RouteInfo.of("B", "D", 15d));
        routes.add(RouteInfo.of("A", "C", 20d));
        routes.add(RouteInfo.of("C", "D", 30d));
        routes.add(RouteInfo.of("B", "E", 50d));
        routes.add(RouteInfo.of("D", "E", 30d));
        // duplications
        routes.add(RouteInfo.of("A", "B", 2d));
        routes.add(RouteInfo.of("B", "E", 50d));
        MapInfo map = new MapInfo(mapName, routes);
        return map;
    }

    private MapInfo createDisconnectedMapInfo(String mapName) {
        List<RouteInfo> routes = new ArrayList<RouteInfo>();
        routes.add(RouteInfo.of("A", "C", 1d));
        routes.add(RouteInfo.of("B", "C", 1d));
        routes.add(RouteInfo.of("D", "E", 2d));
        routes.add(RouteInfo.of("D", "F", 1d));
        MapInfo map = new MapInfo(mapName, routes);
        return map;
    }

}