package dk.uds.emrex.ncp;

import java.io.IOException;

import https.github_com.emrex_eu.elmo_schemas.tree.v1.Elmo;

/**
 * Created by sj on 30-03-16.
 */
public interface StudyFetcher {
    Elmo fetchElmo(String institutionId, String ssn) throws IOException;
    @Deprecated
    String fetchStudies(String institutionId, String ssn) throws IOException;
}
