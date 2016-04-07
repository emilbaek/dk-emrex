package dk.uds.emrex.smp.saml2;

import dk.uds.emrex.smp.UserSession;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by sj on 07-04-16.
 */

@Component()
@Scope("session")
public class Saml2UserSession implements UserSession {
    public Saml2UserSession() {
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String toString() {
        return "WayfSession{}";
    }
}
