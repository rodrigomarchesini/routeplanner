package com.walmart.routeplanner.model.services;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphalgo.WeightedPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.model.entity.Point;
import com.walmart.routeplanner.model.repositories.PointRepository;

@ContextConfiguration(locations = "classpath:/spring-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PointServiceTest {
    @Autowired
    private Neo4jTemplate template;

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
    public void simpleMap() {
        final String mapName = "Brasil";
        Set<Point> points = createSimpleMap(mapName);
        // printAllPoints();

        Point a = pointService.findPoint("A", mapName);
        Point b = pointService.findPoint("D", mapName);
        WeightedPath path = pointService.shortestPath(a, b);
        Assert.assertEquals(0, Double.compare(25d, path.weight()));
    }

    @Test
    public void clearOneMapKeepAnother() {
        final String mapName1 = "map1";
        Set<Point> points1 = createSimpleMap(mapName1);

        final String mapName2 = "map2";
        Set<Point> points2 = createSimpleMap(mapName2);

        Assert.assertEquals(points1.size(), pointService.countByMap(mapName1));
        Assert.assertEquals(points2.size(), pointService.countByMap(mapName2));

        mapService.deleteMap(mapName1);
        Assert.assertEquals(0, pointService.countByMap(mapName1));
        Assert.assertEquals(points2.size(), pointService.countByMap(mapName2));
    }

    private void printAllPoints() {
        for (Point point : pointService.getAllPoints()) {
            System.out.println(point);
        }
    }

    private Set<Point> createSimpleMap(String mapName) {
        /*
         * A B 10
         * B D 15
         * A C 20
         * C D 30
         * B E 50
         * D E 30
         */
        Set<Point> points = new HashSet<Point>();

        Point a = pointService.createPoint("A", mapName);
        Point b = pointService.createPoint("B", mapName);
        Point c = pointService.createPoint("C", mapName);
        Point d = pointService.createPoint("D", mapName);
        Point e = pointService.createPoint("E", mapName);

        pointService.createRoute(a, b, 10d);
        pointService.createRoute(b, d, 15d);
        pointService.createRoute(a, c, 20d);
        pointService.createRoute(c, d, 30d);
        pointService.createRoute(b, e, 50d);
        pointService.createRoute(d, e, 30d);

        points.add(a);
        points.add(b);
        points.add(c);
        points.add(d);
        points.add(e);
        return points;
    }
}
