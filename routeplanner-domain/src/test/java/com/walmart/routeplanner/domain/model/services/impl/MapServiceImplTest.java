package com.walmart.routeplanner.domain.model.services.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Relationship;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.domain.model.entity.MapInfo;
import com.walmart.routeplanner.domain.model.entity.Point;
import com.walmart.routeplanner.domain.model.entity.Route;

/**
 * Tests for MapServiceImpl
 *
 * @author Rodrigo Marchesini
 */
@ContextConfiguration(locations = "classpath:/META-INF/spring-domain-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MapServiceImplTest extends BaseMapTest {

    @Test
    public void deleteOnlySpecifiedMap() {
        final String mapName1 = "map1";
        MapInfo map1 = createSimpleMapInfo(mapName1);
        createMap(map1);

        final String mapName2 = "map2";
        MapInfo map2 = createSimpleMapInfo(mapName2);
        createMap(map2);

        Assert.assertEquals(map1.buildPointsSet().size(), pointRepository.countPointsByMap(mapName1));
        Assert.assertEquals(map2.buildPointsSet().size(), pointRepository.countPointsByMap(mapName2));

        mapService.deleteMap(mapName1);

        Assert.assertEquals(0, pointRepository.countPointsByMap(mapName1));
        Assert.assertEquals(map2.buildPointsSet().size(), pointRepository.countPointsByMap(mapName2));
    }

    @Test
    public void simpleMapCreation() {
        String mapName = "map1";
        MapInfo map = createSimpleMapInfo(mapName);

        createMap(map);
        Assert.assertEquals(map.buildPointsSet().size(), pointRepository.countPointsByMap(mapName));
    }

    @Test
    public void mapNameIndexing() {
        final String mapName1 = "map1";
        MapInfo map1 = createSimpleMapInfo(mapName1);
        createMap(map1);

        final String mapName2 = "map2";
        MapInfo map2 = createSimpleMapInfo(mapName2);
        createMap(map2);

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

        createMap(map);
        Assert.assertEquals(map.buildPointsSet().size(), pointRepository.countPointsByMap(mapName));

        Point a = pointRepository.findByNameAndMap("A", mapName);
        Point b = pointRepository.findByNameAndMap("B", mapName);
        Point e = pointRepository.findByNameAndMap("E", mapName);

        Relationship ab = template.getRelationshipBetween(a, b, "GOES_TO");
        Relationship be = template.getRelationshipBetween(b, e, "GOES_TO");

        Assert.assertEquals(0, Double.compare(10, (Integer) ab.getProperty(Route.PROP_COST)));
        Assert.assertEquals(0, Double.compare(50, (Integer) be.getProperty(Route.PROP_COST)));
    }
}