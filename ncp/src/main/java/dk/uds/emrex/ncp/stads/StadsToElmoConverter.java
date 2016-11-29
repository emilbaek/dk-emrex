package dk.uds.emrex.ncp.stads;

import java.util.ArrayList;
import java.util.List;

import eu.europa.cedefop.europass.europass.v2.CountryCode;
import https.github_com.emrex_eu.elmo_schemas.tree.v1.Elmo;
import https.github_com.emrex_eu.elmo_schemas.tree.v1.Elmo.Learner;
import https.github_com.emrex_eu.elmo_schemas.tree.v1.Elmo.Report;
import https.github_com.emrex_eu.elmo_schemas.tree.v1.LearningOpportunitySpecification;
import https.github_com.emrex_eu.elmo_schemas.tree.v1.LearningOpportunitySpecification.Specifies;
import https.github_com.emrex_eu.elmo_schemas.tree.v1.LearningOpportunitySpecification.Specifies.LearningOpportunityInstance;
import https.github_com.emrex_eu.elmo_schemas.tree.v1.LearningOpportunitySpecification.Specifies.LearningOpportunityInstance.AcademicTerm;
import https.github_com.emrex_eu.elmo_schemas.tree.v1.LearningOpportunitySpecification.Specifies.LearningOpportunityInstance.Credit;
import https.github_com.emrex_eu.elmo_schemas.tree.v1.TokenWithOptionalLang;

public class StadsToElmoConverter {
	public static Elmo toElmo(dk.uds.emrex.stads.wsdl.Elmo se) {
		final Elmo elmo = new Elmo();

		elmo.setGeneratedDate(se.getGeneratedDate());
		elmo.setSignature(null); // No data
		Learner learner = asElmo(se.getLearner());
		elmo.setLearner(learner);
		List<Report> report = elmo.getReport();
		report.addAll(asElmo(se.getReport()));
		elmo.getAttachment(); // No data

		elmo.setExtension(null); // Local extensions should go here, if any

		return elmo;
	}

	private static List<Report> asElmo(dk.uds.emrex.stads.wsdl.Elmo.Report stadsReport) {
		ArrayList<Report> elmoReports = new ArrayList<Report>();

		Report report = new Report();
		report.setIssueDate(stadsReport.getIssueDate());
		report.setIssuer(null); // TODO QNK mangler i STADS
		report.getLearningOpportunitySpecification().addAll(asElmo(stadsReport.getLearningOpportunitySpecification()));

		elmoReports.add(report);

		return elmoReports;
	}

	private static List<LearningOpportunitySpecification> asElmo(
			List<dk.uds.emrex.stads.wsdl.LearningOpportunitySpecification> stadsLOSs) {
		List<LearningOpportunitySpecification> elmoLOSs = new ArrayList<LearningOpportunitySpecification>();

		for (dk.uds.emrex.stads.wsdl.LearningOpportunitySpecification stadsLOS : stadsLOSs) {
			elmoLOSs.add(asElmo(stadsLOS));
		}

		return elmoLOSs;
	}

	private static LearningOpportunitySpecification asElmo(
			dk.uds.emrex.stads.wsdl.LearningOpportunitySpecification stadsLOS) {
		LearningOpportunitySpecification elmoLOS = new LearningOpportunitySpecification();

		elmoLOS.setIscedCode(null);
		elmoLOS.setSpecifies(asElmo(stadsLOS.getSpecifies()));
		elmoLOS.setSubjectArea(null);
		elmoLOS.setType(stadsLOS.getType());
		elmoLOS.setUrl(null);
		List<LearningOpportunitySpecification.Identifier> identifiers = elmoLOS.getIdentifier();
		LearningOpportunitySpecification.Identifier identifier = new LearningOpportunitySpecification.Identifier();
		identifier.setType("local");
		identifier.setValue(stadsLOS.getIdentifier());
		identifiers.add(identifier);
		TokenWithOptionalLang title = new TokenWithOptionalLang();
		title.setLang("en");
		title.setValue(stadsLOS.getTitle());
		elmoLOS.getTitle().add(title);

		elmoLOS.setExtension(null); // Local extensions could go here

		return elmoLOS;
	}

	private static Specifies asElmo(dk.uds.emrex.stads.wsdl.LearningOpportunitySpecification.Specifies stadsSpecifies) {
		Specifies elmoSpecifies = new Specifies();

		elmoSpecifies.setLearningOpportunityInstance(asElmo(stadsSpecifies.getLearningOpportunityInstance()));

		elmoSpecifies.setExtension(null); // Local extensions go here

		return elmoSpecifies;
	}

	private static LearningOpportunityInstance asElmo(
			dk.uds.emrex.stads.wsdl.LearningOpportunitySpecification.Specifies.LearningOpportunityInstance stadsLOI) {
		LearningOpportunityInstance elmoLOI = new LearningOpportunityInstance();

		elmoLOI.setAcademicTerm(asElmo(stadsLOI.getAcademicTerm()));
		elmoLOI.setDate(stadsLOI.getDate());
		elmoLOI.setEngagementHours(null);
		elmoLOI.setLanguageOfInstruction(null);
		elmoLOI.setResultDistribution(null);
		elmoLOI.setResultLabel(stadsLOI.getResultLabel());
		elmoLOI.setShortenedGrading(null);
		elmoLOI.setStart(stadsLOI.getStart());
		elmoLOI.getCredit().add(asElmo(stadsLOI.getCredit()));

		elmoLOI.setExtension(null); // Local extensions go here

		return elmoLOI;
	}

	private static Credit asElmo(
			dk.uds.emrex.stads.wsdl.LearningOpportunitySpecification.Specifies.LearningOpportunityInstance.Credit stadsCredit) {
		Credit credit = new Credit();

		credit.setLevel(stadsCredit.getLevel());
		credit.setScheme(stadsCredit.getScheme());
		credit.setValue(stadsCredit.getValue());

		return credit;
	}

	private static AcademicTerm asElmo(
			dk.uds.emrex.stads.wsdl.LearningOpportunitySpecification.Specifies.LearningOpportunityInstance.AcademicTerm stadsAT) {
		AcademicTerm elmoAcademicTerm = null;
		if (stadsAT != null) {
			elmoAcademicTerm = new AcademicTerm();

			elmoAcademicTerm.setEnd(stadsAT.getEnd());
			elmoAcademicTerm.setStart(stadsAT.getStart());
			List<TokenWithOptionalLang> titles = elmoAcademicTerm.getTitle();
			TokenWithOptionalLang title = new TokenWithOptionalLang(); //
			title.setValue(stadsAT.getTitle());
			titles.add(title);
		}

		return elmoAcademicTerm;
	}

	private static Learner asElmo(dk.uds.emrex.stads.wsdl.Elmo.Learner stadsLearner) {
		Learner elmoLearner = new Learner();

		elmoLearner.setCitizenship(asCountryCode(stadsLearner.getCitizenship()));
		elmoLearner.setBday(stadsLearner.getBday());
		elmoLearner.setFamilyName(stadsLearner.getFamilyName());
		elmoLearner.setGivenNames(stadsLearner.getGivenNames());
		List<Learner.Identifier> identifiers = elmoLearner.getIdentifier();
		for (dk.uds.emrex.stads.wsdl.Elmo.Learner.Identifier stadsIdentifier : stadsLearner.getIdentifier()) {
			Learner.Identifier identifier = new Learner.Identifier();
			identifier.setType(stadsIdentifier.getType());
			identifier.setValue(stadsIdentifier.getValue());
			identifiers.add(identifier);
		}

		return elmoLearner;
	}

	private static CountryCode asCountryCode(String citizenship) {
		CountryCode countryCode = CountryCode.fromValue(citizenship);

		return countryCode;
	}
}
