package com.walmart.routeplanner.web.rest;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walmart.routeplanner.domain.model.PathInfo;
import com.walmart.routeplanner.domain.services.PointNotFoundException;
import com.walmart.routeplanner.services.map.importer.temp.MapTempImporterService;
import com.walmart.routeplanner.services.map.manager.MapCreationResponse;
import com.walmart.routeplanner.services.map.manager.MapManagerService;
import com.walmart.routeplanner.services.map.path.PathService;
import com.walmart.routeplanner.web.model.GenericResponse;

/**
 * Rest services to manage maps and calculate
 * cheapest routes between points.
 * 
 * @author Rodrigo Marchesini
 */
@Component
@Path("/map")
public class MapRest {

    private static final Logger logger = LoggerFactory.getLogger(MapRest.class);

    @Autowired
    private MapTempImporterService mapService;

    @Autowired
    private MapManagerService mapManager;

    @Autowired
    private PathService pathService;

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{mapName}")
    public Response createOrReplaceMap(
            @PathParam("mapName") String mapName,
            InputStream inputStream) {
        logger.info("Requested rest service createOrReplaceMap map={}", mapName);

        MapCreationResponse response = mapManager.addMap(mapName, inputStream);
        switch (response) {
            case OK_SCHEDULED:
                return Response
                        .ok(GenericResponse.as("OK: Map will be imported."), MediaType.APPLICATION_JSON)
                        .build(); 
            case ERROR_INVALID_MAP_NAME:
                return Response
                        .status(Status.BAD_REQUEST)
                        .type(MediaType.APPLICATION_JSON )
                        .entity(GenericResponse.as("Error: invalid map name."))
                        .build();
            case ERROR_BUSY:
                return Response
                        .status(Status.BAD_REQUEST)
                        .type(MediaType.APPLICATION_JSON )
                        .entity(GenericResponse.as("Error: map is already being imported."))
                        .build();
            default:
                throw new AssertionError();
        }
    }

    @GET
    @Path("/{mapName}")
    @Produces(MediaType.APPLICATION_JSON)
    public PathInfo cheapestRoute(
            @PathParam("mapName") String mapName,
            @QueryParam("origin") String origin,
            @QueryParam("destination") String destination,
            @QueryParam("autonomy") Double autonomy,
            @QueryParam("fuelCost") Double fuelCost)
            throws PointNotFoundException {
        logger.info("Requested rest service createOrReplaceMap map={} origin={} destination={} autonomy={} fuelCost={}",
                mapName, origin, destination, autonomy, fuelCost);
        return pathService.shortestPath(mapName, origin, destination, autonomy, fuelCost);
    }
}
