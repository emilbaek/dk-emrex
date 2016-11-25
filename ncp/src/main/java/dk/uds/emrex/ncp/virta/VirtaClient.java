package dk.uds.emrex.ncp.virta;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Value;

import dk.uds.emrex.ncp.DateConverter;
import dk.uds.emrex.ncp.StudyFetcher;
import fi.csc.tietovaranto.emrex.ELMOOpiskelijavaihtoRequest;
import fi.csc.tietovaranto.emrex.ELMOOpiskelijavaihtoResponse;
import fi.csc.tietovaranto.emrex.ELMOOpiskelijavaihtoService;
import fi.csc.tietovaranto.emrex.Hakuehdot;
import fi.csc.tietovaranto.emrex.Kutsuja;
import fi.csc.tietovaranto.emrex.ObjectFactory;
import https.github_com.emrex_eu.elmo_schemas.tree.v1.Elmo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by marko.hollanti on 28/09/15.
 */
@Slf4j
@Setter
//@Component
public class VirtaClient implements StudyFetcher {

    /**
     *
     */
    @Value("${ncp.virta.secret}")
    private  String AVAIN;
    @Value("${ncp.virta.system}")
    private  String JARJESTELMA  ;
    @Value("${ncp.virta.identifier}")
    private String TUNNUS;
    @Value("${ncp.virta.url}")
    private String virtaUrl;
    
    private ELMOOpiskelijavaihtoService elmoOpiskelijavaihtoService;

/*    
    @Override
    public String fetchStudies(String institutionId, String ssn) {
        return fetchStudies(new VirtaUser(institutionId, ssn));
    }
*/
    public String fetchStudies(VirtaUser virtaUser) {
        try {
            String marshal = VirtaMarshaller.marshal(sendRequest(virtaUser));
            log.error("fetch Studies marshalled");
            log.error(marshal);
            return marshal;
        } catch (Exception e) {
            log.error("FetchStudies failed. StudentID: {} PersonalID: {}", virtaUser.getOid(), virtaUser.getSsn(), e);
            return null;
        }
    }

    private ELMOOpiskelijavaihtoResponse sendRequest(VirtaUser virtaUser) throws MalformedURLException {
        ELMOOpiskelijavaihtoRequest request = createRequest(virtaUser);
        ELMOOpiskelijavaihtoResponse temp = getService().getELMOOpiskelijavaihtoSoap11().elmoOpiskelijavaihto(request);
        log.info(temp.toString());
        log.info(temp.getElmo().toString());
        return temp;
    }

    private ELMOOpiskelijavaihtoService getService() throws MalformedURLException {
        if (elmoOpiskelijavaihtoService == null) {
            elmoOpiskelijavaihtoService = new ELMOOpiskelijavaihtoService(new URL(virtaUrl));
        }
        return elmoOpiskelijavaihtoService;
    }

    private ELMOOpiskelijavaihtoRequest createRequest(VirtaUser virtaUser) {
        ELMOOpiskelijavaihtoRequest request = new ELMOOpiskelijavaihtoRequest();
        request.setKutsuja(getKutsuja());
        request.setHakuehdot(getHakuehdot(virtaUser));
        return request;
    }

    private Hakuehdot getHakuehdot(VirtaUser virtaUser) {
        Hakuehdot hakuehdot = new Hakuehdot();

        if (virtaUser.isOidSet()) {
            hakuehdot.getContent().add(0, new ObjectFactory().createOID(virtaUser.getOid()));
        } else {
            hakuehdot.getContent().add(0, new ObjectFactory().createHeTu(virtaUser.getSsn()));
        }

        return hakuehdot;
    }

    private XMLGregorianCalendar convert(LocalDate date) {
        try {
            return DateConverter.convertLocalDateToXmlGregorianCalendar(date);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private Kutsuja getKutsuja() {
        Kutsuja kutsuja = new Kutsuja();
        kutsuja.setAvain(AVAIN);
        kutsuja.setJarjestelma(JARJESTELMA);
        kutsuja.setTunnus(TUNNUS);
        return kutsuja;
    }

		@Override
		public Optional<Elmo> fetchElmo(String institutionId, String ssn) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

}
