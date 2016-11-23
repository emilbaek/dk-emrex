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
import https.github_com.emrex_eu.elmo_schemas.tree.v1.Elmo;

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
	@Test
	public void testConvertStadsToElmo() throws Exception {
		try {
			String stadsXml = TestUtil.getFileContent("stads-elmo1.xml");
	        JAXBContext elmoContext = JAXBContext.newInstance("https.github_com.emrex_eu.elmo_schemas.tree.v1");
	        //JAXBContext jc = JAXBContext.newInstance(dk.uds.emrex.stads.wsdl.Elmo.class);
	        Unmarshaller elmoUn = elmoContext.createUnmarshaller();
	        dk.uds.emrex.stads.wsdl.Elmo elmo = (dk.uds.emrex.stads.wsdl.Elmo) u.unmarshal(new StringReader(stadsXml));
	        assertNotNull(elmo);
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}
	
}
