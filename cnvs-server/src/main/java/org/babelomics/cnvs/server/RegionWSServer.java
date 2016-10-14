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
    public Response getVariantsByRegion(//@ApiParam(value = "centerId1") @PathParam("centerId1") String centerId1,
//                                        @ApiParam(value = "centerId2") @PathParam("centerId2") String centerId2,
//                                        @ApiParam(value = "centerId3") @PathParam("centerId3") String centerId3,
                                        @ApiParam(value = "regions") @PathParam("regions") String regions,
                                        @ApiParam(value = "code") @QueryParam("code") @DefaultValue("") String code,
                                        @ApiParam(value = "decipherId") @QueryParam("decipherId") @DefaultValue("") String decipherId,
                                        @ApiParam(value = "assembly") @QueryParam("assembly") @DefaultValue("") String assembly,
                                        @ApiParam(value = "band") @QueryParam("band") @DefaultValue("") String band,
                                        @ApiParam(value = "type") @QueryParam("type") @DefaultValue("") String type,
                                        @ApiParam(value = "doses") @QueryParam("doses") @DefaultValue("") String doses,
                                        @ApiParam(value = "clis") @QueryParam("clis") @DefaultValue("") String clis,
                                        @ApiParam(value = "inheritance") @QueryParam("inheritance") @DefaultValue("") String inheritance,
                                        @ApiParam(value = "nv") @QueryParam("nv") @DefaultValue("-1") int nv,
                                        @ApiParam(value = "cl") @QueryParam("cl") @DefaultValue("-1") int cl,
                                        @ApiParam(value = "gender") @QueryParam("gender") @DefaultValue("-1") int gender,
                                        @ApiParam(value = "status") @QueryParam("status") @DefaultValue("") String status,
                                        @ApiParam(value = "typeS") @QueryParam("typeS") @DefaultValue("") String typeS,
                                        @ApiParam(value = "referalDiag") @QueryParam("referalDiag") @DefaultValue("") String referalDiag,
                                        @ApiParam(value = "hpo") @QueryParam("hpo") @DefaultValue("") String hpo,
                                        @ApiParam(value = "year") @QueryParam("year") @DefaultValue("") String year,
                                        @ApiParam(value = "yearTest") @QueryParam("yearTest") @DefaultValue("") String yearTest,
                                        @ApiParam(value = "age") @QueryParam("age") @DefaultValue("") String age,
                                        @ApiParam(value = "agePrenatal") @QueryParam("agePrenatal") @DefaultValue("") String agePrenatal,
                                        @ApiParam(value = "ethic") @QueryParam("ethic") @DefaultValue("") String ethic,
                                        @ApiParam(value = "geo") @QueryParam("geo") @DefaultValue("") String geo,
                                        @ApiParam(value = "arrayPlatform") @QueryParam("arrayPlatform") @DefaultValue("") String arrayPlatform,
                                        @ApiParam(value = "arrayId") @QueryParam("arrayId") @DefaultValue("") String arrayId,
                                        @ApiParam(value = "syndrome") @QueryParam("syndrome") @DefaultValue("") String syndrome,
                                        @ApiParam(value = "limit") @QueryParam("limit") @DefaultValue("10") int limit,
                                        @ApiParam(value = "skip") @QueryParam("skip") @DefaultValue("0") int skip,
                                        @ApiParam(value = "host") @QueryParam("host") @DefaultValue("") String host,
                                        @ApiParam(value = "user") @QueryParam("user") @DefaultValue("") String user,
                                        @ApiParam(value = "pass") @QueryParam("pass") @DefaultValue("") String pass

    ) {

        List<String> regionList = Splitter.on(",").splitToList(regions);

        List<Iterable<CNV>> res = new ArrayList<>();
        int totalcount = 0;
        MutableLong count = new MutableLong(-1);

        for (String regionElem : regionList) {
            List<CNV> auxList = new ArrayList<>();
//            Iterable<CNV> variantes = getCNVs(centerId1, centerId2, centerId3, regionElem, code, decipherId, assembly, band, type, doses, clis, inheritance, nv, cl, gender,
//                    status, typeS, referalDiag, hpo, year,  yearTest, age, agePrenatal, ethic, geo, arrayPlatform, arrayId, syndrome, limit,
//                    skip, host, user, pass, count);
            Iterable<CNV> variantes = getCNVs(regionElem, code, decipherId, assembly, band, type, doses, clis, inheritance, nv, cl, gender,
                    status, typeS, referalDiag, hpo, year,  yearTest, age, agePrenatal, ethic, geo, arrayPlatform, arrayId, syndrome, limit,
                    skip, host, user, pass, count);
            for (CNV c : variantes) {
                auxList.add(c);

            }

            res.add(auxList);
            totalcount += count.getValue();

        }

        QueryResponse qr = createQueryResponse(res);
        qr.setNumTotalResults(totalcount);

        return createOkResponse(qr);

    }

    @GET
    @Path("/fetch")
    @Produces("application/json")
    @ApiOperation(value = "Get Variants By Filters")
    public Response getCNVsByFilters(//@ApiParam(value = "centerId1") @PathParam("centerId1") String centerId1,
//                                     @ApiParam(value = "centerId2") @PathParam("centerId2") String centerId2,
//                                     @ApiParam(value = "centerId3") @PathParam("centerId3") String centerId3,
                                     @ApiParam(value = "regions") @QueryParam("regions") @DefaultValue("") String regions,
                                     @ApiParam(value = "code") @QueryParam("code") @DefaultValue("") String code,
                                     @ApiParam(value = "decipherId") @QueryParam("decipherId") @DefaultValue("") String decipherId,
                                     @ApiParam(value = "assembly") @QueryParam("assembly") @DefaultValue("") String assembly,
                                     @ApiParam(value = "band") @QueryParam("band") @DefaultValue("") String band,
                                     @ApiParam(value = "type") @QueryParam("type") @DefaultValue("") String type,
                                     @ApiParam(value = "doses") @QueryParam("doses") @DefaultValue("") String doses,
                                     @ApiParam(value = "clis") @QueryParam("clis") @DefaultValue("") String clis,
                                     @ApiParam(value = "inheritance") @QueryParam("inheritance") @DefaultValue("") String inheritance,
                                     @ApiParam(value = "nv") @QueryParam("nv") @DefaultValue("-1") int nv,
                                     @ApiParam(value = "cl") @QueryParam("cl") @DefaultValue("-1") int cl,
                                     @ApiParam(value = "gender") @QueryParam("gender") @DefaultValue("-1") int gender,
                                     @ApiParam(value = "status") @QueryParam("status") @DefaultValue("") String status,
                                     @ApiParam(value = "typeS") @QueryParam("typeS") @DefaultValue("") String typeS,
                                     @ApiParam(value = "referalDiag") @QueryParam("referalDiag") @DefaultValue("") String referalDiag,
                                     @ApiParam(value = "hpo") @QueryParam("hpo") @DefaultValue("") String hpo,
                                     @ApiParam(value = "year") @QueryParam("year") @DefaultValue("") String year,
                                     @ApiParam(value = "yearTest") @QueryParam("yearTest") @DefaultValue("") String yearTest,
                                     @ApiParam(value = "age") @QueryParam("age") @DefaultValue("") String age,
                                     @ApiParam(value = "agePrenatal") @QueryParam("agePrenatal") @DefaultValue("") String agePrenatal,
                                     @ApiParam(value = "ethic") @QueryParam("ethic") @DefaultValue("") String ethic,
                                     @ApiParam(value = "geo") @QueryParam("geo") @DefaultValue("") String geo,
                                     @ApiParam(value = "arrayPlatform") @QueryParam("arrayPlatform") @DefaultValue("") String arrayPlatform,
                                     @ApiParam(value = "arrayId") @QueryParam("arrayId") @DefaultValue("") String arrayId,
                                     @ApiParam(value = "syndrome") @QueryParam("syndrome") @DefaultValue("") String syndrome,
                                     @ApiParam(value = "limit") @QueryParam("limit") @DefaultValue("10") int limit,
                                     @ApiParam(value = "skip") @QueryParam("skip") @DefaultValue("0") int skip,
                                     @ApiParam(value = "host") @QueryParam("host") @DefaultValue("") String host,
                                     @ApiParam(value = "user") @QueryParam("user") @DefaultValue("") String user,
                                     @ApiParam(value = "pass") @QueryParam("pass") @DefaultValue("") String pass) {

        MutableLong count = new MutableLong(-1);

//        Iterable<CNV> variantes = getCNVs(centerId1, centerId2, centerId3, regions, code, decipherId, assembly, band, type, doses, clis, inheritance, nv, cl, gender, status,
//                typeS, referalDiag, hpo, year, yearTest, age, agePrenatal, ethic, geo, arrayPlatform, arrayId, syndrome, limit, skip, host,
//                user, pass, count);

        Iterable<CNV> variantes = getCNVs(regions, code, decipherId, assembly, band, type, doses, clis, inheritance, nv, cl, gender, status,
                typeS, referalDiag, hpo, year, yearTest, age, agePrenatal, ethic, geo, arrayPlatform, arrayId, syndrome, limit, skip, host,
                user, pass, count);
        QueryResponse qr = createQueryResponse(variantes);
        qr.setNumTotalResults(count.getValue());

        return createOkResponse(qr);
    }

    private Iterable<CNV> getCNVs(//@ApiParam(value = "centerId1") @PathParam("centerId1") String centerId1,
//                                  @ApiParam(value = "centerId2") @PathParam("centerId2") String centerId2,
//                                  @ApiParam(value = "centerId3") @PathParam("centerId3") String centerId3,
                                  @ApiParam(value = "regions") @QueryParam("regions") @DefaultValue("") String regions,
                                  @ApiParam(value = "code") @QueryParam("code") @DefaultValue("") String code,
                                  @ApiParam(value = "decipherId") @QueryParam("decipherId") @DefaultValue("") String decipherId,
                                  @ApiParam(value = "assembly") @QueryParam("assembly") @DefaultValue("") String assembly,
                                  @ApiParam(value = "band") @QueryParam("band") @DefaultValue("") String band,
                                  @ApiParam(value = "type") @QueryParam("type") @DefaultValue("") String type,
                                  @ApiParam(value = "doses") @QueryParam("doses") @DefaultValue("") String doses,
                                  @ApiParam(value = "clis") @QueryParam("clis") @DefaultValue("") String clis,
                                  @ApiParam(value = "inheritance") @QueryParam("inheritance") @DefaultValue("") String inheritance,
                                  @ApiParam(value = "nv") @QueryParam("nv") @DefaultValue("-1") int nv,
                                  @ApiParam(value = "cl") @QueryParam("cl") @DefaultValue("-1") int cl,
                                  @ApiParam(value = "gender") @QueryParam("gender") @DefaultValue("-1") int gender,
                                  @ApiParam(value = "status") @QueryParam("status") @DefaultValue("") String status,
                                  @ApiParam(value = "typeS") @QueryParam("typeS") @DefaultValue("") String typeS,
                                  @ApiParam(value = "referalDiag") @QueryParam("referalDiag") @DefaultValue("") String referalDiag,
                                  @ApiParam(value = "hpo") @QueryParam("hpo") @DefaultValue("") String hpo,
                                  @ApiParam(value = "year") @QueryParam("year") @DefaultValue("") String year,
                                  @ApiParam(value = "yearTest") @QueryParam("yearTest") @DefaultValue("") String yearTest,
                                  @ApiParam(value = "age") @QueryParam("age") @DefaultValue("") String age,
                                  @ApiParam(value = "agePrenatal") @QueryParam("agePrenatal") @DefaultValue("") String agePrenatal,
                                  @ApiParam(value = "ethic") @QueryParam("ethic") @DefaultValue("") String ethic,
                                  @ApiParam(value = "geo") @QueryParam("geo") @DefaultValue("") String geo,
                                  @ApiParam(value = "arrayPlatform") @QueryParam("arrayPlatform") @DefaultValue("") String arrayPlatform,
                                  @ApiParam(value = "arrayId") @QueryParam("arrayId") @DefaultValue("") String arrayId,
                                  @ApiParam(value = "syndrome") @QueryParam("syndrome") @DefaultValue("") String syndrome,
                                  @ApiParam(value = "limit") @QueryParam("limit") @DefaultValue("10") int limit,
                                  @ApiParam(value = "skip") @QueryParam("skip") @DefaultValue("0") int skip,
                                  @ApiParam(value = "host") @QueryParam("host") @DefaultValue("") String host,
                                  @ApiParam(value = "user") @QueryParam("user") @DefaultValue("") String user,
                                  @ApiParam(value = "pass") @QueryParam("pass") @DefaultValue("") String pass,
                                  MutableLong count) {

//        List<String> centerId1aux = new ArrayList<>();
//        if (centerId1.length() > 0) {
//            String[] centerId1Splits = centerId1.split(",");
//            for (String s : centerId1Splits) {
//                centerId1aux.add(s);
//            }
//        }
//
//        List<String> centerId2aux = new ArrayList<>();
//        if (centerId2.length() > 0) {
//            String[] centerId2Splits = centerId2.split(",");
//            for (String s : centerId2Splits) {
//                centerId2aux.add(s);
//            }
//        }
//
//        List<String> centerId3aux = new ArrayList<>();
//        if (centerId1.length() > 0) {
//            String[] centerId3Splits = centerId3.split(",");
//            for (String s : centerId3Splits) {
//                centerId3aux.add(s);
//            }
//        }

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

        List<Integer> clisaux = parsearListaInt(clis);

        List<Integer> inheritanceaux = parsearListaInt(inheritance);

        List<Integer> statusaux = parsearListaInt(status);

        List<Integer> typeSaux = parsearListaInt(typeS);

        List<String> rdaux = new ArrayList<>();
        if (referalDiag.length() > 0) {
            String[] referalDiagSplits = referalDiag.split(",");
            for (String s : referalDiagSplits) {
                rdaux.add(s);
            }
        }
        List<String> hpoaux = new ArrayList<>();
        if (hpo.length() > 0) {
            String[] hpoSplits = hpo.split(",");
            for (String s : hpoSplits) {
                hpoaux.add(s);
            }
        }
        List<Integer> yearaux = parsearListaInt(year);
        List<Integer> yearTestaux = parsearListaInt(yearTest);
        List<String> ageaux = new ArrayList<>();
        if (age.length() > 0) {
            String[] ageSplits = age.split(",");
            for (String s : ageSplits) {
                ageaux.add(s);
            }
        }

        List<Integer> agePrenatalaux = parsearListaInt(agePrenatal);

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

        List<String> arrayPlatformaux = new ArrayList<>();
        if (arrayPlatform.length() > 0) {
            String[] arrayPlatformSplits = arrayPlatform.split(",");
            for (String s : arrayPlatformSplits) {
                arrayPlatformaux.add(s);
            }
        }

        List<String> arrayIdaux = new ArrayList<>();
        if (arrayId.length() > 0) {
            String[] arrayIdSplits = arrayId.split(",");
            for (String s : arrayIdSplits) {
                arrayIdaux.add(s);
            }
        }

        List<Integer> syndromeaux = parsearListaInt(syndrome);


//        QueryCommandLine qpa = new QueryCommandLine(false, code, decipherIdaux, centerId1aux, centerId2aux, centerId3aux, regions, assembly, bandaux, typeaux, dosesaux, clisaux,
//                inheritanceaux, nv, cl, gender, statusaux, typeSaux, rdaux, hpoaux, yearaux, yearTestaux, ageaux, agePrenatalaux, ethicaux,
//                geoaux, arrayPlatformaux, arrayIdaux, syndromeaux, skip, limit, host, user, pass);

        QueryCommandLine qpa = new QueryCommandLine(false, code, decipherIdaux, regions, assembly, bandaux, typeaux, dosesaux, clisaux,
                inheritanceaux, nv, cl, gender, statusaux, typeSaux, rdaux, hpoaux, yearaux, yearTestaux, ageaux, agePrenatalaux, ethicaux,
                geoaux, arrayPlatformaux, arrayIdaux, syndromeaux, skip, limit, host, user, pass);

        Iterable<CNV> variantes = qm.getCNVsByFilters(qpa, count);

        return variantes;
    }

    private List<Integer> parsearListaInt(String c) {
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

