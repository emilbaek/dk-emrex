package dk.uds.emrex.ncp.stads;

import dk.kmd.emrex.common.idp.IdpConfig;
import dk.kmd.emrex.common.idp.IdpConfigListService;
import dk.uds.emrex.ncp.StudyFetcher;
import dk.uds.emrex.stads.wsdl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.WebServiceException;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * Created by sj on 30-03-16.
 */
public class StadsStudyFetcher extends WebServiceGatewaySupport implements StudyFetcher {
    private static Logger LOG = LoggerFactory.getLogger(StadsStudyFetcher.class);

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
            LOG.warn("No STADS urls given.");
        }

        while (urls.hasNext()) {
            final String url = urls.next();

            LOG.info("Opening connection to STADS with URL {}", url);

            try {
                return getStudentsResults(url, ssn);
            } catch (IOException | WebServiceException ex) {
                LOG.warn(String.format("Error when connecting to STADS web-service at %s.", url), ex);
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

    /***
     *
     * @param url
     * @param cpr
     * @return ELMO XML as String
     * @throws IOException
     * @throws SoapFaultClientException
     */
    private String getStudentsResults(String url, String cpr) throws IOException, WebServiceException {
        final BigInteger requestId = uuidToBigInteger(UUID.randomUUID());

        final GetStudentsResults request = new GetStudentsResults();

        final GetStudentsResultInput input = new GetStudentsResultInput();
        input.setCPR(formatCprToStads(cpr));
        input.setRequestId(requestId);

        request.setInputStruct(input);

        final GetStudentsResultsResponse response = (GetStudentsResultsResponse) this.getWebServiceTemplate()
                .marshalSendAndReceive(
                        url,
                        request,
                        new SoapActionCallback(url)
                );

        final GetStudentsResultsOutput.ReceiptStructure receipt = response.getReturn().getReceiptStructure();

        switch (receipt.getReceiptCode()) {
            case 0:
                // Get ELMO document as XML string
                final StringWriter xmlWriter = new StringWriter();
                final StreamResult marshalResult = new StreamResult(xmlWriter);
                final Elmo elmoDocument = response.getReturn().getElmoDocument();

                final JAXBElement<Elmo> elmoJAXBElement = new JAXBElement<>(new QName("elmo"), Elmo.class, elmoDocument);

                this.getMarshaller().marshal(elmoJAXBElement, marshalResult);
//                this.getMarshaller().marshal(response.getReturn().getElmoDocument(), marshalResult);

                final String elmoString = xmlWriter.toString();
                LOG.debug("Returning ELMO string:\n{}", elmoString);
                return elmoString;
            default:
                throw new IOException(String.format("STADS error: %s - %s", receipt.getReceiptCode(), receipt.getReceiptText()));
        }
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
