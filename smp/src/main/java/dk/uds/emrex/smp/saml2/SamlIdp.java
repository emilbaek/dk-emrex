package dk.uds.emrex.smp.saml2;

import lombok.ToString;

/**
 * Created by sj on 23-05-16.
 */
@ToString
public class SamlIdp {
    private String id;
    private String name;

    public SamlIdp(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
