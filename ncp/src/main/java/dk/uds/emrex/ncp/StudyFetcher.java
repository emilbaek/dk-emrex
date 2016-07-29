package dk.uds.emrex.ncp;

import java.io.IOException;

/**
 * Created by sj on 30-03-16.
 */
public interface StudyFetcher {
    String fetchStudies(String institutionId, String ssn) throws IOException;
}
