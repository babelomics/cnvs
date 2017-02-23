package org.babelomics.cnvs.server;

/**
 * Created by sgallego on 8/31/15.
 */

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang3.mutable.MutableLong;
import org.babelomics.cnvs.lib.models.CNV;
import org.babelomics.cnvs.lib.models.CNVmanager;
import org.babelomics.cnvs.lib.ws.QueryResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Path("/management")
@Api(value = "management", description = "DB Management")
@Produces(MediaType.APPLICATION_JSON)
public class ManagementWSServer extends CNVSWSServer {
    public ManagementWSServer(@PathParam("version") String version, @Context UriInfo uriInfo, @Context HttpServletRequest httpServletRequest) throws IOException {
        super(version, uriInfo, httpServletRequest);
    }


    @POST
    @Path("/add")
    @Produces("application/json")
    @ApiOperation(value = "Add CNVs to DB")
    public Response addCNV(@ApiParam(value = "user") @QueryParam("user") @DefaultValue("") String user,
                           @ApiParam(value = "sid") @QueryParam("sid") @DefaultValue("") String sid,
                           @ApiParam(value="body", required = true) List<CNVmanager> body) throws IOException {

        System.out.println("WS add. Add CNVS to DB: ");
        System.out.println("user:"+ user + "sid:"+ sid);

        int res = qm.addCNV(body, user, sid);
        QueryResponse qr = createQueryResponse(res);

        return createOkResponse(qr);

    }

    @GET
    @Path("/search")
    @Produces("application/json")
    @ApiOperation(value = "Search for CNVS added by a group")
    public Response searchCNVs(@ApiParam(value = "user") @QueryParam("user") @DefaultValue("") String user,
                               @ApiParam(value = "sid") @QueryParam("sid") @DefaultValue("") String sid,
                                @ApiParam(value = "limit") @QueryParam("limit") @DefaultValue("10") int limit,
                                @ApiParam(value = "skip") @QueryParam("skip") @DefaultValue("0") int skip){
        MutableLong count = new MutableLong(-1);
        Iterable<CNVmanager> cnvs =  qm.searchCNVs(user, sid, skip, limit, count);

        QueryResponse qr = createQueryResponse(cnvs);
        qr.setNumTotalResults(count.getValue());

        return createOkResponse(qr);
    }

}

