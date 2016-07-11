package org.babelomics.cnvs.lib.models;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sgallego on 2/2/16.
 */
public class CNVTest {

    public CNV cnv;


    @Before
    public void setUp() throws Exception {
        this.cnv = new CNV();
        this.cnv.setRef("C");
    }

    @After
    public void tearDown() throws Exception {
        this.cnv = null;
    }

    @Test
    public void testGetAssembly() throws Exception {

    }

    @org.junit.Test
    public void testSetAssembly() throws Exception {

    }

    @org.junit.Test
    public void testGetGenes() throws Exception {

    }

    @org.junit.Test
    public void testSetGenes() throws Exception {

    }

    @org.junit.Test
    public void testGetLocus() throws Exception {

    }

    @org.junit.Test
    public void testSetLocus() throws Exception {

    }

    @org.junit.Test
    public void testGetDoses() throws Exception {

    }

    @org.junit.Test
    public void testSetDoses() throws Exception {

    }

    @org.junit.Test
    public void testGetClinicalSig() throws Exception {

    }

    @org.junit.Test
    public void testSetClinicalSig() throws Exception {

    }

    @org.junit.Test
    public void testGetInheritance() throws Exception {

    }

    @org.junit.Test
    public void testSetInheritance() throws Exception {

    }

    @org.junit.Test
    public void testGetNv() throws Exception {

    }

    @org.junit.Test
    public void testSetNv() throws Exception {

    }

    @org.junit.Test
    public void testGetCellLine() throws Exception {

    }

    @org.junit.Test
    public void testSetCellLine() throws Exception {

    }

    @org.junit.Test
    public void testGetChromoGender() throws Exception {

    }

    @org.junit.Test
    public void testSetChromoGender() throws Exception {

    }

    @org.junit.Test
    public void testGetStatus() throws Exception {

    }

    @org.junit.Test
    public void testSetStatus() throws Exception {

    }

    @org.junit.Test
    public void testGetTypeSample() throws Exception {

    }

    @org.junit.Test
    public void testSetTypeSample() throws Exception {

    }

    @org.junit.Test
    public void testGetPhenotype() throws Exception {

    }

    @org.junit.Test
    public void testSetPhenotype() throws Exception {

    }

    @org.junit.Test
    public void testGetYearOfBirth() throws Exception {

    }

    @org.junit.Test
    public void testSetYearOfBirth() throws Exception {

    }

    @org.junit.Test
    public void testGetReferalDiag() throws Exception {

    }

    @org.junit.Test
    public void testSetReferalDiag() throws Exception {

    }

    @org.junit.Test
    public void testGetEthnicGroup() throws Exception {

    }

    @org.junit.Test
    public void testSetEthnicGroup() throws Exception {

    }

    @org.junit.Test
    public void testGetOrigin() throws Exception {

    }

    @org.junit.Test
    public void testSetOrigin() throws Exception {

    }

    @org.junit.Test
    public void testGetDecipherId() throws Exception {

    }

    @org.junit.Test
    public void testSetDecipherId() throws Exception {

    }

    @org.junit.Test
    public void testGetArrayPlatform() throws Exception {

    }

    @org.junit.Test
    public void testSetArrayPlatform() throws Exception {

    }

    @org.junit.Test
    public void testGetArrayId() throws Exception {

    }

    @org.junit.Test
    public void testSetArrayId() throws Exception {

    }

    @org.junit.Test
    public void testToString() throws Exception {

    }

    @org.junit.Test
    public void testGetCenterId1() throws Exception {

    }

    @org.junit.Test
    public void testSetCenterId1() throws Exception {

    }

    @org.junit.Test
    public void testGetCenterId2() throws Exception {

    }

    @org.junit.Test
    public void testSetCenterId2() throws Exception {

    }

    @org.junit.Test
    public void testGetCenterId3() throws Exception {

    }

    @org.junit.Test
    public void testSetCenterId3() throws Exception {

    }

    @org.junit.Test
    public void testGetComments() throws Exception {

    }

    @org.junit.Test
    public void testSetComments() throws Exception {

    }

    @org.junit.Test
    public void testGetChromosome() throws Exception {

    }

    @org.junit.Test
    public void testSetChromosome() throws Exception {

    }

    @org.junit.Test
    public void testGetStart() throws Exception {

    }

    @org.junit.Test
    public void testSetStart() throws Exception {

    }

    @org.junit.Test
    public void testGetEnd() throws Exception {

    }

    @org.junit.Test
    public void testSetEnd() throws Exception {

    }

    @org.junit.Test
    public void testGetType() throws Exception {

    }

    @org.junit.Test
    public void testSetType() throws Exception {

    }

    @org.junit.Test
    public void testGetRef() throws Exception {
        assertEquals(this.cnv.getRef(), "C");

    }

    @org.junit.Test
    public void testSetRef() {
        this.cnv.setRef("A");
        assertEquals(this.cnv.getRef(), "A");
    }
}