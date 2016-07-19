package dk.kmd.emrex.common.idp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sj on 19-07-16.
 */
public class IdpConfigListService {

    public static IdpConfigListService fromJson(final InputStream jsonStream) throws IOException {
        final ObjectMapper jsonObjectMapper = new ObjectMapper();
        final List<IdpConfig> idpConfigList = Arrays.asList(jsonObjectMapper.readValue(jsonStream, IdpConfig[].class));

        return new IdpConfigListService(idpConfigList);
    }

    private final Iterable<IdpConfig> idpConfigs;

    public IdpConfigListService(final Iterable<IdpConfig> idpConfigs) {
        this.idpConfigs = idpConfigs;
    }

    public Iterable<IdpConfig> getIdpConfigs() {
        return idpConfigs;
    }
}
