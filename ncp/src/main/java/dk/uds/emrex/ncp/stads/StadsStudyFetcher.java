package dk.uds.emrex.ncp.stads;

import dk.kmd.emrex.common.UrlBuilder;
import dk.kmd.emrex.common.idp.IdpConfig;
import dk.kmd.emrex.common.idp.IdpConfigListService;
import dk.uds.emrex.ncp.StudyFetcher;
import org.apache.commons.collections.BufferUnderflowException;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.validation.constraints.NotNull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.UUID;

/**
 * Created by sj on 30-03-16.
 */
@Service
@Profile("!dev")
public class StadsStudyFetcher implements StudyFetcher {
    private static class StadsResponse {
        private UUID requestId;
        private String elmoDocument;

        public StadsResponse(UUID requestId, String elmoDocument) {
            this.requestId = requestId;
            this.elmoDocument = elmoDocument;
        }

        public UUID getRequestId() {
            return requestId;
        }

        public String getElmoDocument() {
            return elmoDocument;
        }
    }

    private static Logger LOG = LoggerFactory.getLogger(StadsStudyFetcher.class);

    @Autowired
    private IdpConfigListService idpConfigListService;

    @Value("${stads.timeout}")
    private int connectionTimeout;

    @Override
    public String fetchStudies(String institutionId, String ssn) throws IOException {
        for (final IdpConfig idpConfig : idpConfigListService.getIdpConfigs()) {
            if (idpConfig.getId().equalsIgnoreCase(institutionId)) {
                try {
                    return fetchStudies(idpConfig.getGetStudentsResultWebserviceEndpoints(), ssn);
                } catch (IOException e) {
                    throw new IOException(String.format("Unable to connect to any STADS servers for IDP {}", institutionId), e);
                }
            }
        }

        throw new IOException(String.format("No STADS servers known for IDP {}", institutionId));
    }

    private String fetchStudies(Iterable<IdpConfig.IdpConfigUrl> urls, String ssn) throws IOException {
        for (IdpConfig.IdpConfigUrl idpConfigUrl : urls) {
            final UUID requestId = createRequestId();

            final URL url = new UrlBuilder(idpConfigUrl.getUrl())
                    .setParameter("requestID", uuidToString(requestId))
                    .setParameter("cpr", ssn)
                    .toUrl();

            LOG.info("Opening connection to STADS with URL {}", url);

            final URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(this.connectionTimeout);

            try {
                urlConnection.connect();
            } catch (IOException e) {
                // Unable to connect, try next server
                continue;
            }

            try (final InputStream resStream = urlConnection.getInputStream()) {
                // TODO Read actual XML response and validate it
                final StadsResponse stadsResponse = parseResponse(resStream);
                if (!stadsResponse.getRequestId().equals(requestId)) {
                    throw new IOException(String.format("Invalid requestId in response! Got {}, expected {}", stadsResponse.getRequestId(), requestId));
                }

                return stadsResponse.getElmoDocument();
            }
        }

        // If we get here we were unable to connect and/or read a response from a STADS server.
        // Throw error: Not able to connect to any STADS URL
        throw new IOException("Unable to connect to any URL in list.");
    }

    private static StadsResponse parseResponse(InputStream resStream) throws IOException {
        try {
            final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resStream);
            final XPath xPath = XPathFactory.newInstance().newXPath();
            final XPathExpression requestIdXPath = xPath.compile("");
            final XPathExpression elmoXPath = xPath.compile("");

            final UUID requestId = uuidFromString(requestIdXPath.evaluate(doc));
            final String elmo = elmoXPath.evaluate(doc);

            return new StadsResponse(requestId, elmo);
        } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
            throw new IOException(e);
        }
    }

    private static UUID createRequestId() {
        return UUID.randomUUID();
    }

    private static String uuidToString(@NotNull UUID uuid) {
        final ByteBuffer buffer = ByteBuffer.allocate(16);

        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());

        return Base64.getEncoder().encodeToString(buffer.array());
    }

    private static UUID uuidFromString(@NotNull String string) throws IOException {
        try {
            final byte[] bytes = Base64.getDecoder().decode(string);
            final ByteBuffer buffer = ByteBuffer.wrap(bytes);

            return new UUID(buffer.getLong(), buffer.getLong());
        } catch (IllegalArgumentException | BufferUnderflowException e) {
            throw new IOException(e);
        }

    }
}
