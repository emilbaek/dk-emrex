package dk.uds.emrex.ncp;

import dk.uds.emrex.ncp.util.TestUtil;
import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

/**
 * Created by marko.hollanti on 06/10/15.
 */
@SpringApplicationConfiguration
public class DataSignTest extends TestCase {

    private DataSign instance;

    @Before
    public void setUp() throws Exception {
        instance = new DataSign();
        instance.setCertificatePath("dk-emrex-dev.cer");
        instance.setEncryptionKeyPath("dk-emrex-dev.key");
        instance.setEnvironment("dev");
    }
    
    @Test
    public void testDanskWlXml() throws Exception {
    	testSign("Example-elmo-Denmark-wl-unsigned-beautified.xml");
    }

    @Test
    public void testDanskTomcatXml() throws Exception {
    	testSign("Example-elmo-Denmark-tomcat-unsigned-beautified.xml");
    }

    @Test
    public void testWlBase64ZippedXml() throws Exception {
    	testSignedBase64ZippedXml("Example-elmo-Denmark-wl-signed-zipped-base64.txt", false);
    }
    
    @Test
    public void testWlBase64ZippedXml2() throws Exception {
    	testSignedBase64ZippedXml("wl.txt", true); // Hmmm burde fejle
    }
    
    @Test
    public void testWlBase64ZippedXml3() throws Exception {
    	testSignedBase64ZippedXml("wl2.txt", true); // Hmmm burde fejle
    }
    
    @Test
    public void testTomcatBase64ZippedXml() throws Exception {
    	testSignedBase64ZippedXml("Example-elmo-Denmark-tomcat-signed-zipped-base64.txt", true);
    }

    private void testSignedBase64ZippedXml(String fileName, boolean shouldBeValid) throws Exception {
    	String base64EncodedCompressedXml = TestUtil.getFileContent(fileName);
    	System.out.println(instance.unpackBase64CompressedXml(base64EncodedCompressedXml));
		boolean valid = instance.isValidSignature(base64EncodedCompressedXml, StandardCharsets.UTF_8);
		Assert.assertTrue("Skal være valid", valid == shouldBeValid);
    }

    private void testSign(String fileName) throws Exception {
    	
    	System.out.println(System.getProperty("file.encoding"));

        final String data = TestUtil.getFileContent(fileName);

        final String result = instance.sign(data, StandardCharsets.UTF_8);

        final byte[] decoded = DatatypeConverter.parseBase64Binary(result);
        assertNotNull(decoded);

        final byte[] decompressed = GzipUtil.gzipDecompressBytes(decoded);
        assertNotNull(decompressed);

        final String s = new String(decompressed);
        System.out.println(s);

        assertTrue(s.contains("<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>"));
        assertTrue(s.contains("</X509Certificate></X509Data></KeyInfo></Signature></"));
    }
}