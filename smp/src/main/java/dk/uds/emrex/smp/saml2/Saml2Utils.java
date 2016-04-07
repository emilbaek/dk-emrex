package dk.uds.emrex.smp.saml2;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.apache.commons.io.input.ReaderInputStream;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by sj on 07-04-16.
 *
 * OpenSAML2 docs at https://wiki.shibboleth.net/confluence/display/OpenSAML/OSTwoUserManual
 */

public class Saml2Utils {
    private static Logger log = LoggerFactory.getLogger(Saml2Utils.class);

    private static BasicParserPool BASIC_PARSER_POOL;

    static {
        BASIC_PARSER_POOL = new BasicParserPool();
        BASIC_PARSER_POOL.setNamespaceAware(true);

        // "It should be noted that many products out there seem to produce invalid XML and so the usage of XML Schema validation is not recommended."
        // See https://wiki.shibboleth.net/confluence/display/OpenSAML/OSTwoUserManJavaValidation
//        final Schema saml11Schema = SAMLSchemaBuilder.getSAML11Schema();
//        BASIC_PARSER_POOL.setSchema(saml11Schema);
    }

    private Saml2Utils() {

    }

    public static SAMLObject parse(final String xml) throws Saml2Exception {
        try {
            final CharSequenceInputStream inputStream = new CharSequenceInputStream(xml, "UTF-8");
            try {
                return Saml2Utils.parse(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new Saml2Exception(e);
        }
    }

    public static SAMLObject parse(final InputStream xml) throws Saml2Exception {
        try {
            final Document doc = Saml2Utils.parseXmlDocument(xml);
            final Element root = doc.getDocumentElement();

            SAMLObject samlObject = Saml2Utils.unmarshall(root);
            Saml2Utils.validate(samlObject);
            return samlObject;
        } catch (XMLParserException | UnmarshallingException | ValidationException e) {
            throw new Saml2Exception("Unable to parse SAML2 XML.", e);
        }
    }

    private static Document parseXmlDocument(final InputStream xml) throws XMLParserException {
        return BASIC_PARSER_POOL.parse(xml);
    }

    private static SAMLObject unmarshall(final Element e) throws UnmarshallingException {
        return (SAMLObject) Configuration.getUnmarshallerFactory().getUnmarshaller(e).unmarshall(e);
    }

    private static void validate(SAMLObject samlObject) throws ValidationException {
        // See https://wiki.shibboleth.net/confluence/display/OpenSAML/OSTwoUserManJavaValidation
        Configuration.getValidatorSuite("saml2-core-spec-validator").validate(samlObject);
    }
}
