package com.walmart.routeplanner.domain.model.services.impl;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.domain.model.MapInfo;
import com.walmart.routeplanner.domain.model.RouteInfo;
import com.walmart.routeplanner.domain.model.ShortestPathInfo;
import com.walmart.routeplanner.domain.services.RouteService;

/**
 * Tests for MapServiceImpl
 *
 * @author Rodrigo Marchesini
 */
@ContextConfiguration(locations = "classpath:/META-INF/spring-domain-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RouteServiceImplTest extends BaseMapTest {

    @Autowired
    private RouteService routeService;

    @Test
    public void shortestPathInSimpleMap() {
        String mapName = "map1";
        MapInfo map = createSimpleMapInfo(mapName);
        mapService.createMap(map);

        ShortestPathInfo path = routeService.shortestPath(mapName, "A", "D");
        assertShortestPath(path, true, 25d, "A B D");
    }

    @Test
    public void shortestPathInSinglePointMap() {
        String mapName = "map1";
        MapInfo map = new MapInfo(mapName, Collections.singletonList(RouteInfo.of("A", "A", 10d)));
        mapService.createMap(map);

        ShortestPathInfo path = routeService.shortestPath(mapName, "A", "A");
        assertShortestPath(path, true, 0d, "A");
    }

    @Test
    public void shortestPathSameOriginAndDestination() {
        String mapName = "map1";
        MapInfo map = new MapInfo(mapName, Collections.singletonList(RouteInfo.of("A", "A", 10d)));
        mapService.createMap(map);

        ShortestPathInfo path = routeService.shortestPath(mapName, "A", "A");
        assertShortestPath(path, true, 0d, "A");
    }

    @Test
    public void shortestPathNotFoundInDisconnectedMap() {
        String mapName = "map1";
        MapInfo map = createDisconnectedMapInfo(mapName);

        mapService.createMap(map);
        ShortestPathInfo path;

        path = routeService.shortestPath(mapName, "A", "B");
        assertShortestPath(path, false, 0d, "");

        path = routeService.shortestPath(mapName, "C", "A");
        assertShortestPath(path, false, 0d, "");

        path = routeService.shortestPath(mapName, "A", "E");
        assertShortestPath(path, false, 0d, "");
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