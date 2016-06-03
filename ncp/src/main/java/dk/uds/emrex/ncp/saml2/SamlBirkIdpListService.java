package dk.uds.emrex.ncp.saml2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by sj on 23-05-16.
 */

/**
 * A Service that periodically fetches the IDP list from an url and filters it to only contain the "selected" IDPs.
 */
@Service
public class SamlBirkIdpListService {
    private static Logger LOG = LoggerFactory.getLogger(SamlBirkIdpListService.class);

    @Value("${stads.configPath}")
    private String idpConfigPath;

    @Value("${stads.configPath.fallback}")
    private String idpConfigPathFallback;

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
        final InputStream idpConfigStream = getIdpConfigResource().getInputStream();
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

    private Resource getIdpConfigResource() {
        Resource resource = this.resourceLoader.getResource(this.idpConfigPath);

        if (!resource.exists()) {
            resource = this.resourceLoader.getResource(this.idpConfigPathFallback);
        }

        return resource;
    }
}
