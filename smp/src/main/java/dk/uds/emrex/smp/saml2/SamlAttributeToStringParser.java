package dk.uds.emrex.smp.saml2;

import java.util.Iterator;
import java.util.function.Function;

/**
 * Created by sj on 20-04-16.
 */

public class SamlAttributeToStringParser implements Function<Iterable<String>, String> {
    public static final SamlAttributeToStringParser INSTANCE = new SamlAttributeToStringParser();

    @Override
    public String apply(Iterable<String> in) {
        if (in != null) {
            final Iterator<String> it = in.iterator();
            if (it.hasNext()) {
                return it.next();
            }
        }

        return null;
    }
}
