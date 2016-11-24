package dk.uds.emrex.ncp.stads;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.StreamSupport;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceException;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import dk.kmd.emrex.common.idp.IdpConfig;
import dk.kmd.emrex.common.idp.IdpConfigListService;
import dk.uds.emrex.ncp.StudyFetcher;
import dk.uds.emrex.stads.wsdl.Elmo;
import dk.uds.emrex.stads.wsdl.GetStudentsResultInput;
import dk.uds.emrex.stads.wsdl.GetStudentsResults;
import dk.uds.emrex.stads.wsdl.GetStudentsResultsOutput;
import dk.uds.emrex.stads.wsdl.GetStudentsResultsResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by sj on 30-03-16.
 */
@Slf4j
public class StadsStudyFetcher extends WebServiceGatewaySupport implements StudyFetcher {

    @Autowired
    private IdpConfigListService idpConfigListService;

    @Override
    public String fetchStudies(@NotNull String institutionId, @NotNull String ssn) throws IOException {
        for (final IdpConfig idpConfig : idpConfigListService.getIdpConfigs()) {
            if (idpConfig.getId().equalsIgnoreCase(institutionId)) {
                try {
                    final Iterator<String> urlIterator =
                            StreamSupport.stream(idpConfig.getGetStudentsResultWebserviceEndpoints().spliterator(), false)
                            .map((IdpConfig.IdpConfigUrl idpConfigUrl) -> idpConfigUrl.getUrl())
                            .iterator();
                    return fetchStudies(urlIterator, ssn);
                } catch (IOException e) {
                    throw new IOException(String.format("Unable to connect to any STADS servers for IDP %s", institutionId), e);
                }
            }
        }

        throw new IOException(String.format("No STADS servers known for IDP {}", institutionId));
    }

    public String fetchStudies(@NotNull Iterator<String> urls, @NotNull String ssn) throws IOException {
        if (!urls.hasNext()) {
            log.warn("No STADS urls given.");
        }

        while (urls.hasNext()) {
            final String url = urls.next();

           log.info("Opening connection to STADS with URL {}", url);

            try {
                return getStudentsResults(url, ssn);
            } catch (IOException | WebServiceException ex) {
                log.warn(String.format("Error when connecting to STADS web-service at %s.", url), ex);
            }
        }

        // If we get here we were unable to connect and/or read a response from a STADS server.
        // Throw error: Not able to connect to any STADS URL
        throw new IOException(String.format("Unable to successfully get data from any STADS URL in list."));
    }

    private static BigInteger uuidToBigInteger(@NotNull UUID uuid) {
        final ByteBuffer buffer = ByteBuffer.allocate(16);

        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());

        return new BigInteger(buffer.array());
    }
    
	private GetStudentsResultsResponse getStudentResult(@NotNull String url, @NotNull String cpr) {
		final BigInteger requestId = uuidToBigInteger(UUID.randomUUID());

		final GetStudentsResults request = new GetStudentsResults();

		final GetStudentsResultInput input = new GetStudentsResultInput();
		input.setCPR(formatCprToStads(cpr));
		input.setRequestId(requestId);

		request.setInputStruct(input);

		final GetStudentsResultsResponse response = (GetStudentsResultsResponse) this.getWebServiceTemplate()
				.marshalSendAndReceive(url, request, new SoapActionCallback(url));
		return response;
	}
	
    /***
     *
     * @param url
     * @param cpr
     * @return ELMO XML as String
     * @throws IOException
     * @throws SoapFaultClientException
     */
    private String getStudentsResults(String url, String cpr) throws IOException, WebServiceException {
    	GetStudentsResultsResponse response = getStudentResult(url, cpr);
    	
        final GetStudentsResultsOutput.ReceiptStructure receipt = response.getReturn().getReceiptStructure();

        switch (receipt.getReceiptCode()) {
            case 0:
                // Get ELMO document as XML string
            	Elmo stadsElmo = response.getReturn().getElmoDocument();
            	
                //String elmoString = marshall(stadsElmo);
                String elmoString = marshall(StadsToElmoConverter.toElmo(stadsElmo));
                log.debug("Returning ELMO string:\n{}", elmoString);
                return elmoString;
            default:
                throw new IOException(String.format("STADS error: %s - %s", receipt.getReceiptCode(), receipt.getReceiptText()));
        }
    }
    
    private String marshall(https.github_com.emrex_eu.elmo_schemas.tree.v1.Elmo elmo) {
        final StringWriter xmlWriter = new StringWriter();
        final StreamResult marshalResult = new StreamResult(xmlWriter);
        try {
        	Jaxb2Marshaller elmoMarshaller = new Jaxb2Marshaller();
        	
            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            elmoMarshaller.setMarshallerProperties(properties );
            elmoMarshaller.setContextPath("https.github_com.emrex_eu.elmo_schemas.tree.v1");
            
            elmoMarshaller.marshal(elmo, marshalResult);
		} catch (XmlMappingException e) {
            log.error("Error marshalling : " + elmo, e);
		}
        String xml = xmlWriter.toString();
        log.debug("Marshalled to : ", xml);
		return xml;
    }
    
    private String marshall(GetStudentsResultsResponse response) {
        final StringWriter xmlWriter = new StringWriter();
        final StreamResult marshalResult = new StreamResult(xmlWriter);
        try {
			this.getMarshaller().marshal(response, marshalResult);
		} catch (XmlMappingException e) {
            log.error("Error marshalling : " + response, e);
		} catch (IOException e) {
            log.error("Error marshalling : " + response, e);
		}
        String xml = xmlWriter.toString();
        log.debug("Marshalled to : ", xml);
		return xml;
	}

    private String marshall(Elmo elmo) {
        final StringWriter xmlWriter = new StringWriter();
        final StreamResult marshalResult = new StreamResult(xmlWriter);
        final JAXBElement<Elmo> elmoJAXBElement = new JAXBElement<Elmo>(new QName("elmo"), Elmo.class, elmo);
        try {
			this.getMarshaller().marshal(elmoJAXBElement, marshalResult);
		} catch (XmlMappingException e) {
            log.error("Error marshalling : " + elmo, e);
		} catch (IOException e) {
            log.error("Error marshalling : " + elmo, e);
		}
        String xml = xmlWriter.toString();
        log.debug("Marshalled to : ", xml);
		return xml;
	}

	private static String formatCprToStads(@NotNull String cpr) {
        if (cpr.length() == 10) {
            return cpr.substring(0, 6) + "-" + cpr.substring(6);
        } else if (cpr.length() == 11) {
            return cpr;
        } else {
            throw new IllegalArgumentException("cpr must be either length 10 or 11");
        }
    }
}
