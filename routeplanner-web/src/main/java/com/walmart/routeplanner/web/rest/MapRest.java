package com.walmart.routeplanner.web.rest;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walmart.routeplanner.services.map.importer.temp.MapTempImporterService;

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

    @PUT
    @Path("/{mapName}")
    public Response createOrReplaceMap(
            @PathParam("mapName") String mapName,
            InputStream inputStream) {
        mapService.importMap(mapName, inputStream);
        return Response.ok("Map saved. mapName=" + mapName).build();
    }

    @GET
    @Path("/{mapName}")
    public Response cheapestRoute(
            @PathParam("mapName") String mapName,
            @QueryParam("origin") String origin,
            @QueryParam("destination") String destination,
            @QueryParam("autonomy") Double autonomy,
            @QueryParam("fuelCost") Double fuelCost) {
        String format = "mapName=%s origin=%s destination=%s autonomy=%.2f fuelCost=%.2f";
        return Response
                .ok(String.format(format, mapName, origin, destination, autonomy, fuelCost))
                .build();
    }
}
