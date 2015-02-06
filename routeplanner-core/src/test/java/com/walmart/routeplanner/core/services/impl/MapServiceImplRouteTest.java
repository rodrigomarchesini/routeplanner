package com.walmart.routeplanner.core.services.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Relationship;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.core.model.entity.MapInfo;
import com.walmart.routeplanner.core.model.entity.Point;
import com.walmart.routeplanner.core.model.entity.Route;

/**
 * Tests for MapServiceImpl dealing with Route
 *
 * @author Rodrigo Marchesini
 */
@ContextConfiguration(locations = "classpath:/META-INF/spring-core-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MapServiceImplRouteTest extends BaseMapTest {

    @Test
    public void createRoutes() {
        String mapName = "map1";
        MapInfo map = createSimpleMapInfo(mapName);
        mapService.createPoints(map);

        int total = map.getRoutes().size();
        mapService.createRoutes(map, 0, total);
        assertRoutes(map, 0, total);
    }

    @Test
    public void createRoutesSplittedTransaction() {
        String mapName = "map1";
        MapInfo map = createSimpleMapInfo(mapName);
        mapService.createPoints(map);

        int total = map.getRoutes().size();
        int half = Math.round(total / 2);

        mapService.createRoutes(map, 0, half);
        assertRoutes(map, 0, half);
        mapService.createRoutes(map, half, total);
        assertRoutes(map, half, total);
    }

    private void assertRoutes(MapInfo map, int fromIndex, int toIndex) {
        for (Route expectedRoute : map.getRoutes().subList(fromIndex, toIndex)) {
            Point origin = pointRepository.findByNameAndMap(
                    expectedRoute.getOrigin().getName(),
                    expectedRoute.getOrigin().getMapName());
            Point destination = pointRepository.findByNameAndMap(
                    expectedRoute.getDestination().getName(),
                    expectedRoute.getDestination().getMapName());

            Relationship relationship = template.getRelationship(expectedRoute.getId());
            Assert.assertNotNull(relationship);
            Assert.assertEquals(expectedRoute.getCost(), relationship.getProperty(Route.PROP_COST));
            Assert.assertEquals(origin.getId().longValue(), relationship.getStartNode().getId());
            Assert.assertEquals(destination.getId().longValue(), relationship.getEndNode().getId());
        }
    }

}
