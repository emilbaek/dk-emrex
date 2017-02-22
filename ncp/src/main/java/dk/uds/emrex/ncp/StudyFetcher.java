package dk.uds.emrex.ncp;

import java.io.IOException;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import https.github_com.emrex_eu.elmo_schemas.tree.v1.Elmo;

/**
 * Created by sj on 30-03-16.
 */
public interface StudyFetcher {
    Optional<Elmo> fetchElmo(@NotNull String institutionId, @NotNull String ssn) throws IOException;
}
