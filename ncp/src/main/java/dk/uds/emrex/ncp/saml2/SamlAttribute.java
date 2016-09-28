package dk.uds.emrex.ncp.saml2;

import java.lang.annotation.*;

/**
 * Created by sj on 20-04-16.
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SamlAttribute {
    String[] value();
}
