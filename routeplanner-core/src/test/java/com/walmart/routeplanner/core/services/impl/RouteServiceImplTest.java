package com.walmart.routeplanner.core.services.impl;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.core.model.PathInfo;
import com.walmart.routeplanner.core.model.entity.MapInfo;
import com.walmart.routeplanner.core.services.PointNotFoundException;
import com.walmart.routeplanner.core.services.RouteService;

/**
 * Tests for MapServiceImpl
 *
 * @author Rodrigo Marchesini
 */
@ContextConfiguration(locations = "classpath:/META-INF/spring-core-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RouteServiceImplTest extends BaseMapTest {

    @Autowired
    private RouteService routeService;

    @Test
    public void shortestPathInSimpleMap() throws PointNotFoundException {
        String mapName = "map1";
        MapInfo map = createSimpleMapInfo(mapName);
        createMap(map);

        PathInfo path = routeService.shortestPath(mapName, "A", "D");
        assertShortestPath(path, true, 25d, "A B D");
    }

    @Test
    public void shortestPathInSinglePointMap() throws PointNotFoundException {
        String mapName = "map1";
        MapInfo map = new MapInfo(mapName, Collections.singletonList(r(mapName, "A", "A", 10)));
        createMap(map);

        PathInfo path = routeService.shortestPath(mapName, "A", "A");
        assertShortestPath(path, true, 0d, "A");
    }

    @Test
    public void shortestPathSameOriginAndDestination() throws PointNotFoundException {
        String mapName = "map1";
        MapInfo map = new MapInfo(mapName, Collections.singletonList(r(mapName, "A", "A", 10)));
        createMap(map);

        PathInfo path = routeService.shortestPath(mapName, "A", "A");
        assertShortestPath(path, true, 0d, "A");
    }

    @Test
    public void shortestPathNotFoundInDisconnectedMap() throws PointNotFoundException {
        String mapName = "map1";
        MapInfo map = createDisconnectedMapInfo(mapName);

        createMap(map);
        PathInfo path;

        path = routeService.shortestPath(mapName, "A", "B");
        assertShortestPath(path, false, 0d, "");

        path = routeService.shortestPath(mapName, "C", "A");
        assertShortestPath(path, false, 0d, "");

        path = routeService.shortestPath(mapName, "A", "E");
        assertShortestPath(path, false, 0d, "");
    }

    private void assertShortestPath(
            PathInfo path,
            boolean shouldExist,
            Double expectedLength,
            String expectedPath) {
        Assert.assertEquals(shouldExist, path.isPathExists());
        Assert.assertEquals(0, Double.compare(expectedLength, path.getLength()));
        Assert.assertEquals(expectedPath, path.getPointsAsLineString());
    }
}