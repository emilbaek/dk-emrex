package dk.uds.emrex.ncp.saml2;

import org.joda.time.DateTime;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sj
 */
public class WayfUser {
    private static final Pattern CPR_PATTERN = Pattern.compile("^urn:mace:terena\\.org:schac:personalUniqueID:dk:CPR:(.*)$");

    private String id;

    /**
     * http://www.wayf.dk/da/component/content/article/120
     */
    @SamlAttribute("urn:oid:2.5.4.4")
    private String surName;

    /**
     * http://www.wayf.dk/da/component/content/article/121
     */
    @SamlAttribute("urn:oid:2.5.4.42")
    private String givenName;

    /**
     * http://www.wayf.dk/da/component/content/article/122
     */
    @SamlAttribute("urn:oid:2.5.4.3")
    private String commonName;

    /**
     * Brugerens unikke ID inden for organisationen (identitetsudbyderen) hvor man er logget på
     * http://www.wayf.dk/da/component/content/article/123
     */
    @SamlAttribute("urn:oid:1.3.6.1.4.1.5923.1.1.1.6")
    private String principalName;

    /**
     * http://www.wayf.dk/da/component/content/article/124
     */
    @SamlAttribute("urn:oid:0.9.2342.19200300.100.1.3")
    private Iterable<String> emailAddress;

    /**
     * http://www.wayf.dk/da/component/content/article/125
     */
    @SamlAttribute("urn:oid:1.3.6.1.4.1.5923.1.1.1.5")
    private String primaryAffiliation;

    /**
     * http://www.wayf.dk/da/component/content/article/126
     */
    @SamlAttribute("urn:oid:2.5.4.10")
    private String organizationName;

    /**
     * Beskriver det sikkerhedsniveau hvorpå hjemmeorganisationen (IdP'en) garanterer en brugers identitet.
     * http://www.wayf.dk/da/component/content/article/127
     */
    @SamlAttribute("urn:oid:1.3.6.1.4.1.5923.1.1.1.11")
    private Integer assuranceLevel;

    /**
     * Denne attribut indeholder et nationalt defineret unikt person-ID.
     * http://www.wayf.dk/da/component/content/article/128
     * Example: urn:mace:terena.org:schac:personalUniqueID:dk:CPR:0101801234
     */
    @SamlAttribute("urn:oid:1.3.6.1.4.1.25178.1.2.15")
    private Iterable<String> personalUniqueIDs;

    @SamlAttribute("urn:oid:1.3.6.1.4.1.25178.1.2.5")
    private Iterable<String> countryOfCitizenships;

    @SamlAttribute("urn:oid:1.3.6.1.4.1.5923.1.1.1.9")
    private Iterable<String> scopedAffiliations;

    @SamlAttribute("urn:oid:2.16.840.1.113730.3.1.39")
    private Iterable<String> preferredLanguages;

    @SamlAttribute("urn:oid:1.3.6.1.4.1.5923.1.1.1.7")
    private Iterable<String> entitlements;

    /**
     * Lokalt identifikationsnummer
     * http://www.wayf.dk/da/component/content/article/132
     */
    @SamlAttribute("urn:oid:1.3.6.1.4.1.2428.90.1.4")
    private Iterable<String> localIds;

    /**
     * Hjemmeorganisationens entydige ID
     * http://www.wayf.dk/da/component/content/article/133
     */
    @SamlAttribute("urn:oid:1.3.6.1.4.1.25178.1.2.9")
    private String organizationId;

    /**
     * http://www.wayf.dk/da/component/content/article/134
     */
    @SamlAttribute("urn:oid:1.3.6.1.4.1.5923.1.1.1.10")
    private Iterable<String> wayfUserIds;

    /**
     * http://www.wayf.dk/da/component/content/article/507
     */
    @SamlAttribute("urn:oid:1.3.6.1.4.1.25178.1.2.3")
    private DateTime dateOfBirth;

    @SamlAttribute("urn:oid:1.3.6.1.4.1.25178.1.0.2.3")
    private Integer yearOfBirth;

    /**
     * http://www.wayf.dk/da/component/content/article/567
     */
    @SamlAttribute("urn:oid:1.3.6.1.4.1.1466.115.121.1.15")
    private String organizationType;

    public WayfUser() {
    }

    public WayfUser(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public Iterable<String> getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(Iterable<String> emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPrimaryAffiliation() {
        return primaryAffiliation;
    }

    public void setPrimaryAffiliation(String primaryAffiliation) {
        this.primaryAffiliation = primaryAffiliation;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getAssuranceLevel() {
        return assuranceLevel;
    }

    public void setAssuranceLevel(Integer assuranceLevel) {
        this.assuranceLevel = assuranceLevel;
    }

    public Iterable<String> getPersonalUniqueIDs() {
        return personalUniqueIDs;
    }

    public void setPersonalUniqueIDs(Iterable<String> personalUniqueIDs) {
        this.personalUniqueIDs = personalUniqueIDs;
    }

    public Iterable<String> getCountryOfCitizenships() {
        return countryOfCitizenships;
    }

    public void setCountryOfCitizenships(Iterable<String> countryOfCitizenships) {
        this.countryOfCitizenships = countryOfCitizenships;
    }

    public Iterable<String> getScopedAffiliations() {
        return scopedAffiliations;
    }

    public void setScopedAffiliations(Iterable<String> scopedAffiliations) {
        this.scopedAffiliations = scopedAffiliations;
    }

    public Iterable<String> getPreferredLanguages() {
        return preferredLanguages;
    }

    public void setPreferredLanguages(Iterable<String> preferredLanguages) {
        this.preferredLanguages = preferredLanguages;
    }

    public Iterable<String> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(Iterable<String> entitlements) {
        this.entitlements = entitlements;
    }

    public Iterable<String> getLocalIds() {
        return localIds;
    }

    public void setLocalIds(Iterable<String> localIds) {
        this.localIds = localIds;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Iterable<String> getWayfUserIds() {
        return wayfUserIds;
    }

    public void setWayfUserIds(Iterable<String> wayfUserIds) {
        this.wayfUserIds = wayfUserIds;
    }

    public DateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(DateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getCpr() {
        final Iterable<String> uniqueIdList = this.getPersonalUniqueIDs();

        if (uniqueIdList != null) {
            for (String uniqueId : uniqueIdList) {
                final Matcher m = CPR_PATTERN.matcher(uniqueId);
                if (m.matches()) {
                    final String cpr = m.group(1);
                    if (!cpr.isEmpty()) {
                        return cpr;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WayfUser wayfUser = (WayfUser) o;
        return Objects.equals(id, wayfUser.id) &&
                Objects.equals(surName, wayfUser.surName) &&
                Objects.equals(givenName, wayfUser.givenName) &&
                Objects.equals(commonName, wayfUser.commonName) &&
                Objects.equals(principalName, wayfUser.principalName) &&
                Objects.equals(emailAddress, wayfUser.emailAddress) &&
                Objects.equals(primaryAffiliation, wayfUser.primaryAffiliation) &&
                Objects.equals(organizationName, wayfUser.organizationName) &&
                Objects.equals(assuranceLevel, wayfUser.assuranceLevel) &&
                Objects.equals(personalUniqueIDs, wayfUser.personalUniqueIDs) &&
                Objects.equals(countryOfCitizenships, wayfUser.countryOfCitizenships) &&
                Objects.equals(scopedAffiliations, wayfUser.scopedAffiliations) &&
                Objects.equals(preferredLanguages, wayfUser.preferredLanguages) &&
                Objects.equals(entitlements, wayfUser.entitlements) &&
                Objects.equals(localIds, wayfUser.localIds) &&
                Objects.equals(organizationId, wayfUser.organizationId) &&
                Objects.equals(wayfUserIds, wayfUser.wayfUserIds) &&
                Objects.equals(dateOfBirth, wayfUser.dateOfBirth) &&
                Objects.equals(yearOfBirth, wayfUser.yearOfBirth) &&
                Objects.equals(organizationType, wayfUser.organizationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surName, givenName, commonName, principalName, emailAddress, primaryAffiliation, organizationName, assuranceLevel, personalUniqueIDs, countryOfCitizenships, scopedAffiliations, preferredLanguages, entitlements, localIds, organizationId, wayfUserIds, dateOfBirth, yearOfBirth, organizationType);
    }

    @Override
    public String toString() {
        return "WayfUser{" +
                "id='" + id + '\'' +
                ", surName='" + surName + '\'' +
                ", givenName='" + givenName + '\'' +
                ", commonName='" + commonName + '\'' +
                ", principalName='" + principalName + '\'' +
                ", emailAddress=" + emailAddress +
                ", primaryAffiliation='" + primaryAffiliation + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", assuranceLevel=" + assuranceLevel +
                ", personalUniqueIDs=" + personalUniqueIDs +
                ", countryOfCitizenships=" + countryOfCitizenships +
                ", scopedAffiliations=" + scopedAffiliations +
                ", preferredLanguages=" + preferredLanguages +
                ", entitlements=" + entitlements +
                ", localIds=" + localIds +
                ", organizationId='" + organizationId + '\'' +
                ", wayfUserIds=" + wayfUserIds +
                ", dateOfBirth=" + dateOfBirth +
                ", yearOfBirth=" + yearOfBirth +
                ", organizationType='" + organizationType + '\'' +
                '}';
    }
}
