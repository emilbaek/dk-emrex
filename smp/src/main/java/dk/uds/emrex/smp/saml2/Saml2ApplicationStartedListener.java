package dk.uds.emrex.smp.saml2;

import org.opensaml.xml.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by sj on 07-04-16.
 */

@Component
public class Saml2ApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {
    private static Logger log = LoggerFactory.getLogger(Saml2ApplicationStartedListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        try {
            // Initialize the OpenSAML2 library
            org.opensaml.DefaultBootstrap.bootstrap();
        } catch (ConfigurationException e) {
            log.error("Failed to initialize the OpenSAML2 library!", e);
        }
    }
}
