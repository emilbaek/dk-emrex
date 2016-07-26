package dk.kmd.emrex.common;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by sj on 19-07-16.
 */
public class UrlBuilder {
    private final String baseUrl;
    private final Map<String, String> params = new TreeMap<>();

    public UrlBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public UrlBuilder setParameter(String key, String value) {
        params.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        final String enc = "UTF-8";
        final StringBuilder url = new StringBuilder(baseUrl);

        int i = 0;

        try {
            for (Map.Entry<String, String> param : params.entrySet()) {
                url.append(i++ == 0 ? "?" : "&");

                url.append(URLEncoder.encode(param.getKey(), enc));
                url.append("=");
                url.append(URLEncoder.encode(param.getValue(), enc));
            }
        } catch (UnsupportedEncodingException e) {
            // Should never happen!
            throw new UnsupportedOperationException(e);
        }

        return url.toString();
    }

    public URL toUrl() throws MalformedURLException {
        return new URL(this.toString());
    }
}
