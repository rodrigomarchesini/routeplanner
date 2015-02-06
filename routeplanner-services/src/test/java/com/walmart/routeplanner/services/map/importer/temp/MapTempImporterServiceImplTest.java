package com.walmart.routeplanner.services.map.importer.temp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.walmart.routeplanner.services.map.processor.BaseMapProcessingTest;
import com.walmart.routeplanner.services.map.processor.exception.MalformedMapException;

/**
 * Tests for MapTempImporterServiceImpl.
 *
 * @author Rodrigo Marchesini
 */
@ContextConfiguration(locations = "classpath:/spring-services-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MapTempImporterServiceImplTest extends BaseMapProcessingTest {

    @Autowired
    private MapTempImporterService services;

    @Test
    public void importMapToFile() {
        String input = "A B 10\nB D 15\nA C 20\nC D 30\nB E 50\nD E 30";
        services.importMap("simplemap", toInputStream(input));
    }

    @Test(expected = MalformedMapException.class)
    public void importBadMap() {
        String input = "A 10\nB D 15\nA C 20\nC D 30\nB E 50\nD E 30";
        services.importMap("badmap", toInputStream(input));
    }

}
