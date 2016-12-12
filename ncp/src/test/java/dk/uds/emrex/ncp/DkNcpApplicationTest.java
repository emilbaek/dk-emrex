package dk.uds.emrex.ncp;

import junit.framework.TestCase;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Ignore;

/**
 * Created by marko.hollanti on 04/09/15.
 */
@Ignore
public class DkNcpApplicationTest extends TestCase {

	private static final String ELMO_XML_KAISA = "src/main/resources/kaisa.xml";

	public static String getElmo() throws Exception {
		return new String(Files.readAllBytes(Paths.get(new File(ELMO_XML_KAISA).getAbsolutePath())));
	}

    public void testGetElmo() throws Exception {

        final String elmoXml = getElmo();
        assertNotNull(elmoXml);
        assertTrue(elmoXml.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
    }
}