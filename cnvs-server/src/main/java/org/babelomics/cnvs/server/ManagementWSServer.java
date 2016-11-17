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
//    public Response addCNV(@ApiParam(value = "limit") @QueryParam("limit") @DefaultValue("-1") int limit,
//                           @ApiParam(value = "skip") @QueryParam("skip") @DefaultValue("-1") int skip,
//                           @ApiParam(value = "host") @QueryParam("host") @DefaultValue("") String host,
//                           @ApiParam(value = "user") @QueryParam("user") @DefaultValue("") String user,
//                           @ApiParam(value = "sid") @QueryParam("sid") @DefaultValue("") String sid,
//                           @ApiParam(value = "list") @QueryParam("list") @DefaultValue("") String list){
    public Response addCNV(@ApiParam(value = "user") @QueryParam("user") @DefaultValue("") String user,
                           @ApiParam(value = "sid") @QueryParam("sid") @DefaultValue("") String sid,
                           @ApiParam(value="cnvs", required = true) List<CNV> cnvs) throws IOException {

        for(CNV c: cnvs){
            System.out.println(c.toString());
        }
        boolean res = qm.addCNV(cnvs,user,sid);
        QueryResponse qr = createQueryResponse(res);
        return createOkResponse(qr);

    }

//    @POST
//    @Path("/{projectId}/update")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @ApiOperation(value = "Update by POST [NO TESTED]", position = 4, response = Project.class)
//    public Response updateByPost(@ApiParam(value = "projectId", required = true) @PathParam("projectId") String projectIdStr,
//                                 @ApiParam(value = "params", required = true) Map<String, Object> params) throws IOException {
//        try {
//            ObjectMap objectMap = new ObjectMap(params);
//            long projectId = catalogManager.getProjectId(projectIdStr);
//            QueryResult result = catalogManager.modifyProject(projectId, objectMap, sessionId);
//            return createOkResponse(result);
//        } catch (Exception e) {
//            return createErrorResponse(e);
//        }
//    }
//    @POST
//    @Path("/{fileId}/update")
//    @ApiOperation(value = "Modify file", position = 16, response = File.class)
//    public Response updatePOST(@ApiParam(value = "File id") @PathParam(value = "fileId") String fileIdStr,
//                               @ApiParam(name = "params", value = "Parameters to modify", required = true) UpdateFile params) {
//        try {
//            long fileId = catalogManager.getFileId(convertPath(fileIdStr, sessionId), sessionId);
//            QueryResult<File> queryResult = catalogManager.getFileManager().update(fileId,
//                    new ObjectMap(jsonObjectMapper.writeValueAsString(params)), queryOptions, sessionId);
//            return createOkResponse(queryResult);
//        } catch (Exception e) {
//            return createErrorResponse(e);
//        }
}

