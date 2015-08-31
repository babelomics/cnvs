package org.babelomics.cnvs.server;

/**
 * Created by sgallego on 8/31/15.
 */

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

@Path("/test")
@Api(value = "test", description = "test web services")
public class TestWSServer extends CNVSWSServer {

    public TestWSServer(@PathParam("version") String version, @Context UriInfo uriInfo, @Context HttpServletRequest httpServletRequest) throws IOException {
        super(version, uriInfo, httpServletRequest);
    }

    @GET
    @Path("/echo/{message}")
    @Produces("text/plain")
    @ApiOperation(value = "Just to test the api")
    public Response echoGet(@ApiParam(value = "message", required = true) @PathParam("message") String message) {
        return buildResponse(Response.ok(message));
    }
}