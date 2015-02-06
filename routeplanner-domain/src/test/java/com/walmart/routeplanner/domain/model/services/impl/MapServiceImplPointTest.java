package com.walmart.routeplanner.domain.model.services.impl;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.domain.model.entity.MapInfo;
import com.walmart.routeplanner.domain.model.entity.Point;

/**
 * Tests for MapServiceImpl dealing with Point
 *
 * @author Rodrigo Marchesini
 */
@ContextConfiguration(locations = "classpath:/META-INF/spring-domain-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MapServiceImplPointTest extends BaseMapTest {

    @Test
    public void createPoints() {
        String mapName = "map1";
        MapInfo map = createSimpleMapInfo(mapName);
        mapService.createPoints(map);

        assertPoints(mapName, map);
    }

    @Test
    public void createPointsPerMap() {
        String mapName1 = "map1";
        MapInfo map1 = createSimpleMapInfo(mapName1);
        mapService.createPoints(map1);

        String mapName2 = "map2";
        MapInfo map2 = createSimpleMapInfo(mapName2);
        mapService.createPoints(map2);

        assertPoints(mapName1, map1);
        assertPoints(mapName2, map2);
    }

    private void assertPoints(String mapName, MapInfo map) {
        Set<String> expectedPoints = map.buildPointsSet();
        Assert.assertEquals(expectedPoints.size(), pointRepository.countPointsByMap(mapName));
        for (String exptectedPoint : expectedPoints) {
            Point point = pointRepository.findByNameAndMap(exptectedPoint, mapName);
            Assert.assertNotNull(point);
            Assert.assertEquals(mapName, point.getMapName());
            Assert.assertEquals(exptectedPoint, point.getName());
        }
    }
}
