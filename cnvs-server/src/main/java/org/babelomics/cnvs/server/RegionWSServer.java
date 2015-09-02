package org.babelomics.cnvs.server;

/**
 * Created by sgallego on 8/31/15.
 */

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang3.mutable.MutableLong;
import org.babelomics.cnvs.lib.cli.QueryCommandLine;
import org.babelomics.cnvs.lib.models.CNV;
import org.babelomics.cnvs.lib.ws.QueryResponse;
import org.opencb.biodata.models.feature.Region;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
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

    /*
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

            Iterable<CNV> res = qm.getCNVsByRegionList(regionList, 0, 10, count);

            QueryResponse qr = createQueryResponse(res);
            qr.setNumTotalResults(count.getValue());

            return createOkResponse(qr);
        }
        */
    @GET
    @Path("/fetch")
    @Produces("application/json")
    @ApiOperation(value = "Get Variants By Filters")
    public Response getCNVsByFilters(/*@ApiParam(value = "regions") @QueryParam("regions") @DefaultValue("") String regions,
                                         @ApiParam(value = "code") @QueryParam("code") @DefaultValue("") String code,
                                         @ApiParam(value = "decipherId") @QueryParam("decipherId") @DefaultValue("-1") List<Long> decipherId,
                                         @ApiParam(value = "assembly") @QueryParam("assembly") @DefaultValue("") String assembly,
                                         @ApiParam(value = "band") @QueryParam("band") @DefaultValue("") List<String> band,
                                         @ApiParam(value = "type") @QueryParam("type") @DefaultValue("-1") List<Integer> type,
                                         @ApiParam(value = "doses") @QueryParam("doses") @DefaultValue("-1") List<Integer> doses,
                                         @ApiParam(value = "clis") @QueryParam("clis") @DefaultValue("-1") List<Integer>  clis,
                                         @ApiParam(value = "inheritance") @QueryParam("inheritance") @DefaultValue("-1") List<Integer>  inheritance,
                                         @ApiParam(value = "cl") @QueryParam("cl") @DefaultValue("-1") int cl,
                                         @ApiParam(value = "gender") @QueryParam("gender") @DefaultValue("-1") int gender,
                                         @ApiParam(value = "status") @QueryParam("status") @DefaultValue("-1") List<Integer> status,
                                         @ApiParam(value = "typeS") @QueryParam("typeS") @DefaultValue("-1") List<Integer> typeS,
                                         @ApiParam(value = "hpo") @QueryParam("hpo") @DefaultValue("") List<String> hpo,
                                         @ApiParam(value = "year") @QueryParam("year") @DefaultValue("-1") List<Integer> year,
                                         @ApiParam(value = "ethic") @QueryParam("ethic") @DefaultValue("") List<String> ethic,
                                         @ApiParam(value = "geo") @QueryParam("geo") @DefaultValue("") List<String> geo,
                                         @ApiParam(value = "limit") @QueryParam("limit") @DefaultValue("10") int limit,
                                         @ApiParam(value = "skip") @QueryParam("skip") @DefaultValue("0") int skip,
                                         @ApiParam(value = "host") @QueryParam("host") @DefaultValue("") String host,
                                         @ApiParam(value = "user") @QueryParam("user") @DefaultValue("") String user,
                                         @ApiParam(value = "pass") @QueryParam("pass") @DefaultValue("") String pass*/
                                     @ApiParam(value = "regions") @QueryParam("regions") @DefaultValue("") String regions,
                                     @ApiParam(value = "code") @QueryParam("code") @DefaultValue("") String code,
                                     @ApiParam(value = "decipherId") @QueryParam("decipherId") @DefaultValue("") String decipherId,
                                     @ApiParam(value = "assembly") @QueryParam("assembly") @DefaultValue("") String assembly,
                                     @ApiParam(value = "band") @QueryParam("band") @DefaultValue("") String band,
                                     @ApiParam(value = "type") @QueryParam("type") @DefaultValue("") String type,
                                     @ApiParam(value = "doses") @QueryParam("doses") @DefaultValue("") String doses,
                                     @ApiParam(value = "clis") @QueryParam("clis") @DefaultValue("") String  clis,
                                     @ApiParam(value = "inheritance") @QueryParam("inheritance") @DefaultValue("") String inheritance,
                                     @ApiParam(value = "cl") @QueryParam("cl") @DefaultValue("-1") int cl,
                                     @ApiParam(value = "gender") @QueryParam("gender") @DefaultValue("-1") int gender,
                                     @ApiParam(value = "status") @QueryParam("status") @DefaultValue("") String status,
                                     @ApiParam(value = "typeS") @QueryParam("typeS") @DefaultValue("") String typeS,
                                     @ApiParam(value = "hpo") @QueryParam("hpo") @DefaultValue("") String hpo,
                                     @ApiParam(value = "year") @QueryParam("year") @DefaultValue("") String year,
                                     @ApiParam(value = "ethic") @QueryParam("ethic") @DefaultValue("") String ethic,
                                     @ApiParam(value = "geo") @QueryParam("geo") @DefaultValue("") String geo,
                                     @ApiParam(value = "limit") @QueryParam("limit") @DefaultValue("10") int limit,
                                     @ApiParam(value = "skip") @QueryParam("skip") @DefaultValue("0") int skip,
                                     @ApiParam(value = "host") @QueryParam("host") @DefaultValue("") String host,
                                     @ApiParam(value = "user") @QueryParam("user") @DefaultValue("") String user,
                                     @ApiParam(value = "pass") @QueryParam("pass") @DefaultValue("") String pass

    ) {


    List<Long> decipherIdaux = new ArrayList<>();
    if (decipherId.length() > 0) {
        String[] decipherIdSplits = decipherId.split(",");
        for (String s : decipherIdSplits) {
            decipherIdaux.add(Long.parseLong(s));
        }
    }
    List<String> bandaux = new ArrayList<>();
    if (band.length() > 0) {
        String[] bandSplits = band.split(",");
        for (String s : bandSplits) {
            bandaux.add(s);
        }
    }
    List<Integer> typeaux = parsearListaInt(type);

    List<Integer> dosesaux = parsearListaInt(doses);

    List<Integer> clisaux =  parsearListaInt(clis);

    List<Integer> inheritanceaux = parsearListaInt(inheritance);

    List<Integer> statusaux = parsearListaInt(status);

    List<Integer> typeSaux = parsearListaInt(typeS);

    List<String> hpoaux = new ArrayList<>();
    if (hpo.length() > 0) {
        String[] hpoSplits = hpo.split(",");
        for (String s : hpoSplits) { parsearListaInt(type);

            hpoaux.add(s);

        }
    }
    List<Integer> yearaux = parsearListaInt(year);

    List<String> ethicaux = new ArrayList<>();
    if (ethic.length() > 0) {
        String[] ethicSplits = ethic.split(",");
        for (String s : ethicSplits) {

            ethicaux.add(s);

        }
    }
    List<String> geoaux = new ArrayList<>();
    if (geo.length() > 0) {
        String[] geoSplits = geo.split(",");
        for (String s : geoSplits) {

            geoaux.add(s);

        }
    }


    QueryCommandLine qpa = new QueryCommandLine(false, code, decipherIdaux, regions, assembly, bandaux, typeaux, dosesaux, clisaux, inheritanceaux, cl, gender, statusaux, typeSaux, hpoaux,
            yearaux, ethicaux, geoaux, skip, limit, host, user, pass);
    MutableLong count = new MutableLong(-1);

        Iterable<CNV> variantes = qm.getCNVsByFilters(qpa, count);

        QueryResponse qr = createQueryResponse(variantes);
      //  qr.setNumTotalResults(count.getValue());
       // qr.addQueryOption("regions", regionList);
       // qr.addQueryOption("doses", dosesaux);

       /* List<Region> regionList = new ArrayList<>();
        List<Integer> diseaseList = new ArrayList<>();
        int regionsSize = 0;


        if (regions.length() > 0) {
            String[] regSplits = regions.split(",");
            for (String s : regSplits) {
                Region r = Region.parseRegion(s);
                regionList.add(r);
                regionsSize += r.getEnd() - r.getStart();
            }
        }



        MutableLong count = new MutableLong(-1);

//        if (csv) {
//            skip = 0;
//            limit = 200;
//        }

        Iterable<CNV> variants = qm.getCNVsByRegionList(regionList, 0, 1, count);

        QueryResponse qr = createQueryResponse(variants);
        qr.setNumTotalResults(count.getValue());

        qr.addQueryOption("regions", regionList);


//        if (!csv) {
//            qr.addQueryOption("limit", limit);
//            qr.addQueryOption("skip", skip);
//        } else {
//            qr.addQueryOption("csv", csv);
//        }
*/

        return createOkResponse(qr);
    }
    private List<Integer> parsearListaInt (String c) {
        List<Integer> listaaux = new ArrayList<>();

        if (c.length() > 0) {
            String[] cSplits = c.split(",");
            for (String s : cSplits) {
                listaaux.add(Integer.parseInt(s));
            }
        }
        return listaaux;
    }
}
