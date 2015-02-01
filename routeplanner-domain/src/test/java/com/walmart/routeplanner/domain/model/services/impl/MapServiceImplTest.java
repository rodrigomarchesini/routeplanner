package com.walmart.routeplanner.domain.model.services.impl;

import java.util.ArrayList;
import java.util.Collections;
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

import com.walmart.routeplanner.domain.model.MapInfo;
import com.walmart.routeplanner.domain.model.RouteInfo;
import com.walmart.routeplanner.domain.model.ShortestPathInfo;
import com.walmart.routeplanner.domain.model.entity.Point;
import com.walmart.routeplanner.domain.repositories.PointRepository;
import com.walmart.routeplanner.domain.services.MapService;

/**
 * Tests for MapServiceImpl
 *
 * @author Rodrigo Marchesini
 */
@ContextConfiguration(locations = "classpath:/spring-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MapServiceImplTest {

    @Autowired
    private Neo4jTemplate template;

    @Autowired
    private PointRepository pointRepository;

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

        Assert.assertEquals(map1.getPoints().size(), pointRepository.countPointsByMap(mapName1));
        Assert.assertEquals(map2.getPoints().size(), pointRepository.countPointsByMap(mapName2));

        mapService.deleteMap(mapName1);

        Assert.assertEquals(0, pointRepository.countPointsByMap(mapName1));
        Assert.assertEquals(map2.getPoints().size(), pointRepository.countPointsByMap(mapName2));
    }

    @Test
    public void simpleMapCreation() {
        String mapName = "map1";
        MapInfo map = createSimpleMapInfo(mapName);

        mapService.createMap(map);
        Assert.assertEquals(map.getPoints().size(), pointRepository.countPointsByMap(mapName));
    }

    @Test
    public void mapNameIndexing() {
        final String mapName1 = "map1";
        MapInfo map1 = createSimpleMapInfo(mapName1);
        mapService.createMap(map1);

        final String mapName2 = "map2";
        MapInfo map2 = createSimpleMapInfo(mapName2);
        mapService.createMap(map2);

        Point a1 = pointRepository.findByNameAndMap("A", mapName1);
        Point a2 = pointRepository.findByNameAndMap("A", mapName2);

        Assert.assertEquals(mapName1, a1.getMapName());
        Assert.assertEquals(mapName2, a2.getMapName());
        Assert.assertNotEquals(a1.getId(), a2.getId());
    }

    @Test
    public void createMapWithDuplicatedRoute() {
        String mapName = "map1";
        MapInfo map = createDuplicatedRouteMapInfo(mapName);

        mapService.createMap(map);
        Assert.assertEquals(map.getPoints().size(), pointRepository.countPointsByMap(mapName));

        Point a = pointRepository.findByNameAndMap("A", mapName);
        Point b = pointRepository.findByNameAndMap("B", mapName);
        Point e = pointRepository.findByNameAndMap("E", mapName);

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
        assertShortestPath(path, true, 25d, "A B D");
    }

    @Test
    public void shortestPathInSinglePointMap() {
        String mapName = "map1";
        MapInfo map = new MapInfo(mapName, Collections.singletonList(RouteInfo.of("A", "A", 10d)));
        mapService.createMap(map);

        ShortestPathInfo path = mapService.shortestPath(mapName, "A", "A");
        assertShortestPath(path, true, 0d, "A");
    }

    @Test
    public void shortestPathSameOriginAndDestination() {
        String mapName = "map1";
        MapInfo map = new MapInfo(mapName, Collections.singletonList(RouteInfo.of("A", "A", 10d)));
        mapService.createMap(map);

        ShortestPathInfo path = mapService.shortestPath(mapName, "A", "A");
        assertShortestPath(path, true, 0d, "A");
    }

    @Test
    public void shortestPathNotFoundInDisconnectedMap() {
        String mapName = "map1";
        MapInfo map = createDisconnectedMapInfo(mapName);

        mapService.createMap(map);
        ShortestPathInfo path;

        path = mapService.shortestPath(mapName, "A", "B");
        assertShortestPath(path, false, 0d, "");

        path = mapService.shortestPath(mapName, "C", "A");
        assertShortestPath(path, false, 0d, "");

        path = mapService.shortestPath(mapName, "A", "E");
        assertShortestPath(path, false, 0d, "");
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

    private void assertShortestPath(
            ShortestPathInfo path,
            boolean shouldExist,
            Double expectedCost,
            String expectedPath) {
        Assert.assertEquals(shouldExist, path.exists());
        Assert.assertEquals(0, Double.compare(expectedCost, path.getTotalCost()));
        Assert.assertEquals(expectedPath, path.getPoints());
    }
}