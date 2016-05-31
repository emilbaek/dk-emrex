package dk.uds.emrex.smp.saml2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sj on 23-05-16.
 */

/**
 * A Service that periodically fetches the IDP list from an url and filters it to only contain the "selected" IDPs.
 */
@Service
public class SamlBirkIdpListService {
    private static Logger LOG = LoggerFactory.getLogger(SamlBirkIdpListService.class);

    @Value("${stadsConfigPath}")
    private String idpConfigPath;

    @Autowired
    private Jackson2ObjectMapperBuilder objectMapperBuilder;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectNodeToSamlIdpConverter objectNodeToSamlIdpConverter;

    public Iterable<SamlIdp> getIdps() throws IOException {
        try {
            return getIdpFromConfig();
        } catch (IOException e) {
            LOG.error("Error reading IDP config file.", e);
            throw e;
        }
    }

    private Iterable<SamlIdp> getIdpFromConfig() throws IOException {
        final InputStream idpConfigStream = this.resourceLoader.getResource(this.idpConfigPath).getInputStream();
        try {
            final ObjectMapper objectMapper = this.objectMapperBuilder.build();
            final JsonNode jsonNode = objectMapper.readTree(idpConfigStream);

            final Iterator<JsonNode> jsonIterator = jsonNode.iterator();

            return () -> new Iterator<SamlIdp>() {
                @Override
                public boolean hasNext() {
                    return jsonIterator.hasNext();
                }

                @Override
                public SamlIdp next() {
                    final JsonNode idp = jsonIterator.next();
                    return objectNodeToSamlIdpConverter.convert((ObjectNode) idp);
                }
            };
        } finally {
            IOUtils.closeQuietly(idpConfigStream);
        }
    }

}
