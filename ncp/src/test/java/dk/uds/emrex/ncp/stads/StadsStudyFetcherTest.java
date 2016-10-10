package dk.uds.emrex.ncp.stads;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created by sj on 30-08-16.
 */
public class StadsStudyFetcherTest {
    private static Logger LOG = LoggerFactory.getLogger(StadsStudyFetcher.class);

    private static String STADS_URL = "http://stads-dev31.northeurope.cloudapp.azure.com:4062/ws_STADS/services/GetStudentsResult/version_1_0";
    private static String[] SSN_ARRAY = { "020575-6557", "010101-aps1" };

    private StadsStudyFetcher studyFetcher;

    @Before
    public void setUp() throws Exception {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("dk.uds.emrex.stads.wsdl");

        this.studyFetcher = new StadsStudyFetcher();

        this.studyFetcher.setMarshaller(marshaller);
        this.studyFetcher.setUnmarshaller(marshaller);
    }

    @After
    public void tearDown() throws Exception {
        this.studyFetcher = null;
    }

    @Test
    public void testFetchStudies() throws Exception {
        try {
            for (final String ssn : SSN_ARRAY) {
                final Iterator<String> urlIterator = Collections.singleton(STADS_URL).iterator();
                final String elmo = this.studyFetcher.fetchStudies(urlIterator, ssn);
                if (StringUtils.isEmpty(elmo)) {
                    fail("ELMO response was empty");
                }
            }
        } catch (Exception ex) {
            LOG.warn("Error in test", ex);
            throw ex;
        }
    }
}