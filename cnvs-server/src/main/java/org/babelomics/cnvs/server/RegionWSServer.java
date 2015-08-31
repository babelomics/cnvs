package org.babelomics.cnvs.server;

/**
 * Created by sgallego on 8/31/15.
 */

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang3.mutable.MutableLong;
import org.babelomics.cnvs.lib.models.CNV;
import org.babelomics.cnvs.lib.ws.QueryResponse;
import org.opencb.biodata.models.feature.Region;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Path("/regions")
@Api(value = "regions", description = "Regions")
@Produces(MediaType.APPLICATION_JSON)
public class RegionWSServer extends CNVSWSServer {
    public RegionWSServer(@PathParam("version") String version, @Context UriInfo uriInfo, @Context HttpServletRequest httpServletRequest) throws IOException {
        super(version, uriInfo, httpServletRequest);
    }

    @GET
    @Path("/{regions}/fetch")
    @Produces("application/json")
    @ApiOperation(value = "Get Variants By Region")
    public Response getVariantsByRegion(@ApiParam(value = "regions") @PathParam("regions") String regions
    ) {

        List<Region> regionList = new ArrayList<>();

        String[] regionSplits = regions.split(",");

        for(String region : regionSplits){
            Region r = new Region(region);
            regionList.add(r);
        }


        MutableLong count = new MutableLong(-1);

        Iterable<CNV> res = qm.getVariantsByRegionList(regionList, 0, 10, count);

        QueryResponse qr = createQueryResponse(res);
        qr.setNumTotalResults(count.getValue());

        return createOkResponse(qr);
    }
}

