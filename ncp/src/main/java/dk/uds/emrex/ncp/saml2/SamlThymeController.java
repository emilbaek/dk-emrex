package dk.uds.emrex.ncp.saml2;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.schema.XSAny;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dk.kmd.emrex.common.idp.IdpConfig;
import dk.kmd.emrex.common.idp.IdpConfigListService;

/**
 * Created by sj on 23-05-16.
 */
@Controller
@RequestMapping("/saml")
public class SamlThymeController {
    private static final Logger LOG = LoggerFactory.getLogger(SamlThymeController.class);

    private static class IdpEntry {
        private String id;
        private String name;

        public IdpEntry(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @SuppressWarnings("unused")
        public String getId() {
            return id;
        }

        @SuppressWarnings("unused")
		public String getName() {
            return name;
        }
    }

    @Autowired
    private IdpConfigListService idpListService;

    @Autowired
    private MetadataManager metadataManager;

    @RequestMapping("/idpSelection")
    public ModelAndView idpSelection(HttpServletRequest request) {

        final String idpDiscoReturnURL = (String) request.getAttribute("idpDiscoReturnURL");
        final String idpDiscoReturnParam = (String) request.getAttribute("idpDiscoReturnParam");

        final ModelAndView modelAndView = new ModelAndView("idpSelection");

        modelAndView.addObject("idpDiscoReturnURL", idpDiscoReturnURL);
        modelAndView.addObject("idpDiscoReturnParam", idpDiscoReturnParam);

        // Get IDP list and filter it
        final List<IdpEntry> idps = new LinkedList<>();
        try {
            for (IdpConfig idpConfig : idpListService.getIdpConfigs()) {

                final String idpEntityId = findEntityIdForIdp(idpConfig.getId());
                final String idpName = idpConfig.getName();

                idps.add(new IdpEntry(idpEntityId, idpName));
            }
        } catch (MetadataProviderException e) {
            LOG.warn("IDP metadata not available", e);
        }

        // Add IDP list to modelAndView
        modelAndView.addObject("idps", idps);

        return modelAndView;
    }

    private String findEntityIdForIdp(String idpId) throws MetadataProviderException {
        // Search through all known IDPs for the one where md:IDPSSODescriptor/Extensions/Scope is equal to idpId

        final String entityId = this.getIdpIdToEntityIdMap().get(idpId);

        if (entityId != null) {
            return entityId;
        } else {
            LOG.warn("Could not resolve BIRK scope for IDP ID {}", idpId);

            return idpId;
        }
    }

    @Cacheable
    private Map<String, String> getIdpIdToEntityIdMap() throws MetadataProviderException {
        final Map<String, String> map = new TreeMap<>();

        for (final String entityId : metadataManager.getIDPEntityNames()) {
            final EntityDescriptor entityDescriptor = metadataManager.getEntityDescriptor(entityId);
            final Extensions idpSsoExtensions = entityDescriptor.getIDPSSODescriptor("urn:oasis:names:tc:SAML:2.0:protocol").getExtensions();
            final XSAny idpScopeObj = (XSAny) idpSsoExtensions.getUnknownXMLObjects(new QName("urn:mace:shibboleth:metadata:1.0", "Scope")).get(0);

            final String idpScope = idpScopeObj.getTextContent();

            map.put(idpScope, entityId);
        }

        return map;
    }
}
