package dk.uds.emrex.smp.saml2;

import org.opensaml.common.SAMLObject;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.xml.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sj on 07-04-16.
 */

@Controller
public class Saml2Controller {
    private static Logger log = LoggerFactory.getLogger(Saml2Controller.class);

    @Value("${saml2.loginUrl}")
    private String loginUrl;

    @Autowired
    private Saml2UserSession userSession;

    @Autowired
    private Saml2Processor processor;

    @RequestMapping(value = "/saml2/login", method = RequestMethod.GET)
    public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String redirectUrl;

        if (this.userSession.isValid()) {
            redirectUrl = "/";
        } else {
            redirectUrl = this.loginUrl;
        }

        resp.sendRedirect(redirectUrl);
    }

    @RequestMapping(value="/saml2/sso", method=RequestMethod.POST)
    public void loginReturn(HttpServletRequest req, HttpServletResponse resp, @RequestBody String body) throws IOException, Saml2Exception, MessageDecodingException, SecurityException {
        try {
            final SAMLObject samlObject = Saml2Utils.parse(body);

            this.processor.decode(req);

            resp.sendRedirect("/");
        } catch (IOException | Saml2Exception | SecurityException | MessageDecodingException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
