package dk.uds.emrex.ncp.stads;

import static org.junit.Assert.assertNotNull;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import javax.xml.bind.*;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import dk.uds.emrex.ncp.util.TestUtil;
import dk.uds.emrex.stads.wsdl.GetStudentsResultsResponse;

/**
 * Examples created with StadsStudyFetcherTest class.
 */
public class StadsToElmoTest {

	private Jaxb2Marshaller marshaller = null;
	
	@Before
	public void setUp() {
        this.marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("dk.uds.emrex.stads.wsdl");
	}
/*	
	@Test
	public void testJaxb() {
		// Create Transformer
        TransformerFactory tf = TransformerFactory.newInstance();
        StreamSource xslt = new StreamSource("src/test/resources/stads-to-elmo.xsl");
        Transformer transformer = tf.newTransformer(xslt);
 
        // Source
        JAXBContext jc = JAXBContext.newInstance(dk.uds.emrex.stads.wsdl.Elmo.class);
        JAXBSource source = new JAXBSource(jc, catalog);
 
        // Result
        StreamResult result = new StreamResult(System.out);
         
        // Transform
        transformer.transform(source, result);	
    }
*/	
/*	
	@Test
	public void testElmoUnmarshall() throws Throwable {
		try {
			String stadsXml = TestUtil.getFileContent("stads-elmo1.xml");
	        JAXBContext stadsContext = JAXBContext.newInstance(dk.uds.emrex.stads.wsdl.Elmo.class);
	        Unmarshaller stadsUnmarshaller = stadsContext.createUnmarshaller();
	        dk.uds.emrex.stads.wsdl.Elmo stads = (dk.uds.emrex.stads.wsdl.Elmo) stadsUnmarshaller.unmarshal(new StringReader(stadsXml));
			
			String elmoXml = TestUtil.getFileContent("Example-elmo-sweden-1.0.xml");
	        JAXBContext elmoContext = JAXBContext.newInstance("https.github_com.emrex_eu.elmo_schemas.tree.v1");
	        Unmarshaller elmoUnmarshaller = elmoContext.createUnmarshaller();
	        https.github_com.emrex_eu.elmo_schemas.tree.v1.Elmo elmo = (https.github_com.emrex_eu.elmo_schemas.tree.v1.Elmo) elmoUnmarshaller.unmarshal(new StringReader(elmoXml));
	        assertNotNull(elmo);
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}
	
	@Test
	public void testStadsUnmarshall() throws Throwable {
		try {
			String stadsXml = TestUtil.getFileContent("stads-elmo1.xml");

	        //JAXBContext stadsContext = JAXBContext.newInstance("http://GetStudentsResultWS_V1_0.webservice.stads.logica.dk/");
	        JAXBContext stadsContext = JAXBContext.newInstance(GetStudentsResultsResponse.class);
	        Unmarshaller stadsUnmarshaller = stadsContext.createUnmarshaller();
	        dk.uds.emrex.stads.wsdl.Elmo stads = (dk.uds.emrex.stads.wsdl.Elmo) stadsUnmarshaller.unmarshal(new StringReader(stadsXml));
	        assertNotNull(stads);
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}
*/	
}
