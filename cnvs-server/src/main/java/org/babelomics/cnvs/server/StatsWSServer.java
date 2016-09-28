package org.babelomics.cnvs.server;

/**
 * Created by sgallego on 8/31/15.
 */

import com.google.common.base.Splitter;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang3.mutable.MutableLong;
import org.babelomics.cnvs.lib.cli.QueryCommandLine;
import org.babelomics.cnvs.lib.models.CNV;
import org.babelomics.cnvs.lib.ws.QueryResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Path("/stats")
@Api(value = "stats", description = "DB Stats")
@Produces(MediaType.APPLICATION_JSON)
public class StatsWSServer extends CNVSWSServer {
    public StatsWSServer(@PathParam("version") String version, @Context UriInfo uriInfo, @Context HttpServletRequest httpServletRequest) throws IOException {
        super(version, uriInfo, httpServletRequest);
    }


    @GET
    @Path("/count")
    @Produces("application/json")
    @ApiOperation(value = "Count all DB cases")
    /*public Response getStatsCount(@ApiParam(value = "limit") @QueryParam("limit") @DefaultValue("-1") int limit,
                                    @ApiParam(value = "skip") @QueryParam("skip") @DefaultValue("-1") int skip,
                                    @ApiParam(value = "host") @QueryParam("host") @DefaultValue("") String host,
                                    @ApiParam(value = "user") @QueryParam("user") @DefaultValue("") String user,
                                    @ApiParam(value = "pass") @QueryParam("pass") @DefaultValue("") String pass) {*/
    public Response getStatsCount(){
        int res = qm.getStatsCount();
        QueryResponse qr = createQueryResponse(res);
        return createOkResponse(qr);

    }
}

