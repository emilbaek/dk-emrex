package dk.uds.emrex.smp.saml2;

/**
 * Created by sj on 07-04-16.
 */
public class Saml2Exception extends Exception {
    public Saml2Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public Saml2Exception(Throwable cause) {
        super(cause);
    }

    public Saml2Exception(String message) {
        super(message);
    }
}
