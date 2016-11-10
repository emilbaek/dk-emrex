package dk.uds.emrex.smp;

import java.io.File;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.subethamail.wiser.Wiser;

import dk.kmd.emrex.common.model.Person;
import junit.framework.TestCase;

/**
 * Created by jpentika on 02/11/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class PDFWriterTests extends TestCase {

    private InstitutionDataWriter institutionDataWriter;
    private InstitutionDataWriter institutionDataWriter2;
    private InstitutionDataWriter institutionDataWriter3;

    private static String pdfBaseDir = "/tmp/";
    private static String mapFile = "src/test/resources/test_dirmap.json";
    private static String institutionDir1 = "testFolderFor";
    private static String institutionDir2 = "Blaablaa";
    private static String institutionDir3 = "example";
    private final int SMTP_TEST_PORT =9050;
    private Wiser wiser;
    @Before
    public void setup() {
        File resourcesDirectory = new File("src/test/resources");
        String resourcePath = resourcesDirectory.getAbsolutePath();
        //mapFile = resourcePath + "/" + mapFile;

        Person user;
        user = new Person();
        user.setHeiOid("HEIOID");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setHomeOrganization("blaablaa.fi");

        institutionDataWriter = new InstitutionDataWriter(user, mapFile, pdfBaseDir);
        //institutionDataWriter.setPdfBaseDir(pdfBaseDir);
        //institutionDataWriter.setDirMap(resourcePath + "/" + mapFile);

        Person user2;
        user2 = new Person();
        user2.setHeiOid("HEIOID2");
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setHomeOrganization("example.com");

        institutionDataWriter2 = new InstitutionDataWriter(user2, mapFile, pdfBaseDir);
        //institutionDataWriter2.setPdfBaseDir(pdfBaseDir);
        //institutionDataWriter2.setDirMap(resourcePath + "/" + mapFile);

        Person user3;
        user3 = new Person();
        user3.setHeiOid("HEIOID");
        user3.setFirstName("firstName");
        user3.setLastName("lastName");
        user3.setHomeOrganization("blaablaa.fi");

        institutionDataWriter3 = new InstitutionDataWriter(user3, mapFile, pdfBaseDir);
        institutionDataWriter3.setEmailTopic("emrex testmail");
        institutionDataWriter3.setEmailBodyFile(resourcePath + "/emailbody.txt");
    }
    
    @Override
    @Before
    public void setUp() throws Exception{
    	super.setUp();
    	Properties properties = System.getProperties(); 
    	properties.setProperty("mail.smtp.host", "localhost");
        properties.setProperty("mail.smtp.port", Integer.toString(SMTP_TEST_PORT));
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.user", "user");
        properties.setProperty("mail.smtp.pass", "pass");
        wiser = new Wiser();
        wiser.setPort(SMTP_TEST_PORT); 
        System.out.println("===============PORTNUMBER===============");
        System.out.println(properties.getProperty("mail.smtp.port"));
        System.out.println("========================================");
        wiser.start();
        Thread.sleep(2000);
        
    }
    
    @Override 
    @After
    public void tearDown() throws Exception{
    	wiser.stop();
    }

    @Before
    public void clearPreviousData() {
        deleteFolder(new File(pdfBaseDir + institutionDir1 + "/" + institutionDir2));
        deleteFolder(new File(pdfBaseDir + institutionDir1 + "/" + institutionDir3));
        new File(pdfBaseDir + institutionDir1).delete();

    }

    @Test
    public void testWriteFile() throws Exception {
        String data = "justTesting";
        byte[] testData = data.getBytes("UTF-8");

        institutionDataWriter.writeDataToInstitutionFolder(testData, ".pdf");
        File contentDir = new File(pdfBaseDir + institutionDir1 + "/" + institutionDir2);
        File[] files = contentDir.listFiles();
        String content = "";
        assertEquals(1, files.length);
        try {
            for (File f : files) {
                content = FileUtils.readFileToString(f);
            }

        } catch (Exception ex) {
            fail("Write data to institution folder failed. " + ex.getMessage());
        }
        assertEquals(data, content);
        //assertEquals("test@example.com", institutionDataWriter.getEmail());
        //assertEquals("verypublickey", institutionDataWriter.getKey());
        deleteFolder(contentDir);
    }

    @Test
    public void testWriteFile2() throws Exception {
        String data = "justTesting";
        byte[] testData = data.getBytes("UTF-8");

        institutionDataWriter2.writeDataToInstitutionFolder(testData, ".pdf");
        File contentDir = new File(pdfBaseDir + institutionDir1 + "/" + institutionDir3);
        File[] files = contentDir.listFiles();
        String content = "";

        //assertEquals(1, files.length);
        try {
            for (File f : files) {
                content = FileUtils.readFileToString(f);
            }

        } catch (Exception ex) {
            fail("Write data to institution folder failed. " + ex.getMessage());
        }
        //assertEquals(data, content);
        assertNull(institutionDataWriter2.getEmail());
         deleteFolder(contentDir);
    }

    @Test
    public void testWriteFile3() throws Exception {
        String data = "justTesting";
        byte[] testData = data.getBytes("UTF-8");

        institutionDataWriter3.writeData(testData, testData);
        File contentDir = new File(pdfBaseDir + institutionDir1 + "/" + institutionDir2);
        File[] files = contentDir.listFiles();
        String content = "";
        assertEquals(6, files.length);
        try {
            for (File f : files) {
                content = FileUtils.readFileToString(f);
            }

        } catch (Exception ex) {
            fail("Write data to institution folder failed. " + ex.getMessage());
        }
        //assertEquals(data, content);
        //assertEquals("test@example.com", institutionDataWriter.getEmail());
        //assertEquals("verypublickey", institutionDataWriter.getKey());
         deleteFolder(contentDir);
    }

    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }

        }
        folder.delete();
    }

}
