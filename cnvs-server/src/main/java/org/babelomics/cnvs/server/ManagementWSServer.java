package org.babelomics.cnvs.server;

/**
 * Created by sgallego on 8/31/15.
 */

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Path("/management")
@Api(value = "stats", description = "DB Stats")
@Produces(MediaType.APPLICATION_JSON)
public class ManagementWSServer extends CNVSWSServer {
    public ManagementWSServer(@PathParam("version") String version, @Context UriInfo uriInfo, @Context HttpServletRequest httpServletRequest) throws IOException {
        super(version, uriInfo, httpServletRequest);
    }


    @POST
    @Path("/add")
    @Produces("application/json")
    @ApiOperation(value = "Add CNVs to DB")
//    public Response addCNV(
//                           @ApiParam(value = "host") @QueryParam("host") @DefaultValue("") String host,
//                           @ApiParam(value = "user") @QueryParam("user") @DefaultValue("") String user,
//                           @ApiParam(value = "sid") @QueryParam("sid") @DefaultValue("") String sid,
//                           @ApiParam(value = "list") @QueryParam("list") @DefaultValue("") String list){
    public Response addCNV(@ApiParam(value = "user") @QueryParam("user") @DefaultValue("") String user,
                           @ApiParam(value = "sid") @QueryParam("sid") @DefaultValue("") String sid,
                           @ApiParam(value="body", required = true) List<CNV> body) throws IOException {

        System.out.println("Llego al webservice el cnvs es: ");
        System.out.println("user:"+ user);
        System.out.println("sid:"+ sid);
        List<QueryResponse> queryResponses = new ArrayList<QueryResponse>();
        for(CNV c: body){
            System.out.println(c.toString());
            boolean res = qm.addCNV(body,user,sid);
            QueryResponse qr = createQueryResponse(res);
            queryResponses.add(qr);
        }

        QueryResponse qr = createQueryResponse(queryResponses);
        return createOkResponse(qr);

    }
}

