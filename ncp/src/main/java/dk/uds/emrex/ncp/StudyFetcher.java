package dk.uds.emrex.ncp;

/**
 * Created by sj on 30-03-16.
 */
public interface StudyFetcher {
    String fetchStudies(String oid, String ssn);
}
