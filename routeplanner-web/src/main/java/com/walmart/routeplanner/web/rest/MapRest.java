package com.walmart.routeplanner.web.rest;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walmart.routeplanner.domain.model.PathInfo;
import com.walmart.routeplanner.services.map.importer.temp.MapTempImporterService;
import com.walmart.routeplanner.services.map.manager.MapCreationResponse;
import com.walmart.routeplanner.services.map.manager.MapManagerService;
import com.walmart.routeplanner.services.map.path.PathService;

/**
 * Rest services to manage maps and calculate
 * cheapest routes between points.
 * 
 * @author Rodrigo Marchesini
 */
@Component
@Path("/map")
public class MapRest {

    @Autowired
    private MapTempImporterService mapService;

    @Autowired
    private MapManagerService mapManager;

    @Autowired
    private PathService pathService;

    @PUT
    @Path("/{mapName}")
    public Response createOrReplaceMap(
            @PathParam("mapName") String mapName,
            InputStream inputStream) {
        MapCreationResponse response = mapManager.addMap(mapName, inputStream);
        switch (response) {
            case OK_SCHEDULED:
                return Response.accepted("OK: scheduled creation").build();
            case ERROR_INVALID_MAP_NAME:
                return Response.status(Status.BAD_REQUEST).entity("invalid map name").build();
            case ERROR_BUSY:
                return Response.status(Status.BAD_REQUEST).entity("map is busy").build();
            default:
                throw new AssertionError();
        }
    }

    @GET
    @Path("/{mapName}")
    @Produces("application/json")
    public PathInfo cheapestRoute(
            @PathParam("mapName") String mapName,
            @QueryParam("origin") String origin,
            @QueryParam("destination") String destination,
            @QueryParam("autonomy") Double autonomy,
            @QueryParam("fuelCost") Double fuelCost) {
        return pathService.shortestPath(mapName, origin, destination, autonomy, fuelCost);
    }
}
