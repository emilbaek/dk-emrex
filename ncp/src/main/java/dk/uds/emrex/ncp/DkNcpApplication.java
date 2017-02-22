package dk.uds.emrex.ncp;

import dk.kmd.emrex.common.idp.IdpConfigListService;
import dk.uds.emrex.ncp.stads.StadsStudyFetcher;
import https.github_com.emrex_eu.elmo_schemas.tree.v1.Elmo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class DkNcpApplication {

	public static void main(String[] args) {
		SpringApplication.run(DkNcpApplication.class, args);
	}

	@Autowired
	private ResourceLoader resourceLoader;

	@Value("${idp.configPath}")
	private String idpConfigPath;

	@Value("${idp.configPath.fallback}")
	private String idpConfigPathFallback;

	@Value("${stads.testURL}")
	private String testStudyFetcherURL;

	@Value("${stads.testCPR}")
	private String testCPR;

	@Value("${stads.useMock}")
	private boolean useTestStudyFetcher;

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("dk.uds.emrex.stads.wsdl");
		return marshaller;
	}

	@Bean
	public StudyFetcher studyFetcher(Jaxb2Marshaller marshaller) {
		final StadsStudyFetcher studyFetcher = new StadsStudyFetcher();
		studyFetcher.setMarshaller(marshaller);
		studyFetcher.setUnmarshaller(marshaller);

		if (useTestStudyFetcher) {
			return new StudyFetcher() {
				@Override
				public Optional<Elmo> fetchElmo(String institutionId, String ssn) throws IOException {
					return studyFetcher.fetchElmo(Collections.singleton(testStudyFetcherURL).iterator(), testCPR);
				}
			};
		}

		return studyFetcher;
	}

	@Bean
	public IdpConfigListService idpConfigListService() throws IOException {
		try (final InputStream jsonStream = getIdpConfigResource().getInputStream()) {
			return IdpConfigListService.fromJson(jsonStream);
		}
	}

	private Resource getIdpConfigResource() {
		Resource resource = this.resourceLoader.getResource(this.idpConfigPath);

		if (!resource.exists()) {
			resource = this.resourceLoader.getResource(this.idpConfigPathFallback);
		}

		return resource;
	}
}
