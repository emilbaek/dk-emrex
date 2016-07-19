package dk.uds.emrex.ncp.saml2;

import dk.kmd.emrex.common.idp.IdpConfig;
import dk.kmd.emrex.common.idp.IdpConfigListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by sj on 23-05-16.
 */
@Controller
@RequestMapping("/saml")
public class SamlThymeController {
    private static final Logger LOG = LoggerFactory.getLogger(SamlThymeController.class);

    @Autowired
    private IdpConfigListService idpListService;

    @RequestMapping("/idpSelection")
    public ModelAndView idpSelection(HttpServletRequest request) {

        final String idpDiscoReturnURL = (String) request.getAttribute("idpDiscoReturnURL");
        final String idpDiscoReturnParam = (String) request.getAttribute("idpDiscoReturnParam");

        final ModelAndView modelAndView = new ModelAndView("idpSelection");

        modelAndView.addObject("idpDiscoReturnURL", idpDiscoReturnURL);
        modelAndView.addObject("idpDiscoReturnParam", idpDiscoReturnParam);

        // Get IDP list and filter it
        Iterable<IdpConfig> idps = idpListService.getIdpConfigs();

        // Add IDP list to modelAndView
        modelAndView.addObject("idps", idps);

        return modelAndView;
    }
}
