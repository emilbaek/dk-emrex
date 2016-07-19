package dk.kmd.emrex.common.idp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sj on 19-07-16.
 */
public class IdpConfig {
    public static class IdpConfigUrl {
        @JsonProperty("url")
        private String url;

        public IdpConfigUrl() {
        }

        public String getUrl() {
            return url;
        }
    }

    @JsonProperty("institutionId")
    private String id;

    @JsonProperty("institutionName")
    private String name;

    @JsonProperty("urls")
//    @JsonDeserialize(contentAs = IdpConfigUrl.class)
    private ArrayList<IdpConfigUrl> getStudentsResultWebserviceEndpoints;

    public IdpConfig() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Iterable<IdpConfigUrl> getGetStudentsResultWebserviceEndpoints() {
        return getStudentsResultWebserviceEndpoints;
    }
}
