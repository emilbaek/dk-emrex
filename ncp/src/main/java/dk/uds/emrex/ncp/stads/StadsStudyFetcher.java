package dk.uds.emrex.ncp.stads;

import dk.kmd.emrex.common.UrlBuilder;
import dk.kmd.emrex.common.idp.IdpConfig;
import dk.kmd.emrex.common.idp.IdpConfigListService;
import dk.uds.emrex.ncp.StudyFetcher;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Created by sj on 30-03-16.
 */
@Service
@Profile("!dev")
public class StadsStudyFetcher implements StudyFetcher {
    @Autowired
    private IdpConfigListService idpConfigListService;

    @Value("${stads.timeout}")
    private int connectionTimeout;

    @Override
    public String fetchStudies(String institutionId, String ssn) {
        for (final IdpConfig idpConfig : idpConfigListService.getIdpConfigs()) {
            if (idpConfig.getId().equalsIgnoreCase(institutionId)) {
                return fetchStudies(idpConfig.getGetStudentsResultWebserviceEndpoints(), ssn);
            }
        }

        // TODO Throw error: No IDP with institutionId found
    }

    private String fetchStudies(Iterable<IdpConfig.IdpConfigUrl> urls, String ssn) {
        for (IdpConfig.IdpConfigUrl idpConfigUrl : urls) {
            try {
                final URL url = new UrlBuilder(idpConfigUrl.getUrl())
                        .setParameter("cpr", ssn)
                        .toUrl();

                final URLConnection urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(this.connectionTimeout);

                urlConnection.connect();

                try (final InputStream resStream = urlConnection.getInputStream()) {
                    return IOUtils.toString(resStream, Charset.forName("UTF-8"));
                }

            } catch (MalformedURLException e) {
                // TODO
            } catch (IOException e) {
                // TODO
            }
        }

        // TODO Throw error: Not able to connect to any STADS URL
    }
}
