package dk.uds.emrex.smp.saml2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSAny;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.List;

/**
 * Created by sj on 26-05-16.
 */
@Service
public class ObjectNodeToSamlIdpConverter/* implements Converter<ObjectNode, SamlIdp>*/ {
    private static Logger LOG = LoggerFactory.getLogger(ObjectNodeToSamlIdpConverter.class);

    @Autowired
    private MetadataManager metadataManager;

//    @Override
    public SamlIdp convert(ObjectNode source) {
        final JsonNode id = source.get("institutionId");
        final JsonNode name = source.get("institutionName");

        try {
            if (id == null || name == null) {
                final String errMsg = String.format("Missing ID or Name: (ID: \"{}\", Name: \"{}\")",
                        id != null ? id.toString() : "",
                        name != null ? name.toString() : "");

                LOG.warn(errMsg);

                throw new RuntimeException(errMsg);
            }

            final String entityId = (id != null) ? findEntityIdForIdp(id.asText()) : "INVALID";

            return new SamlIdp(entityId, name != null ? name.asText() : "Institution");
        } catch (MetadataProviderException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String findEntityIdForIdp(String idpId) throws MetadataProviderException {
        // Search through all known IDPs for the one where md:IDPSSODescriptor/Extensions/Scope is equal to idpId

        for (final String entityId : metadataManager.getIDPEntityNames()) {
            final EntityDescriptor entityDescriptor = metadataManager.getEntityDescriptor(entityId);
            final Extensions idpSsoExtensions = entityDescriptor.getIDPSSODescriptor("urn:oasis:names:tc:SAML:2.0:protocol").getExtensions();
            final XSAny idpScopeObj = (XSAny) idpSsoExtensions.getUnknownXMLObjects(new QName("urn:mace:shibboleth:metadata:1.0", "Scope")).get(0);

            final String idpScope = idpScopeObj.getTextContent();

            if (idpId.equalsIgnoreCase(idpScope)) {
                return entityId;
            }
        }

        return idpId;
    }
}
