package dk.uds.emrex.smp.saml2;

import org.opensaml.common.binding.decoding.SAMLMessageDecoder;
import org.opensaml.saml2.binding.decoding.HTTPPostDecoder;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.ParserPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sj on 07-04-16.
 */

@Configuration
public class Saml2BeanConfiguration {

    @Bean
    public ParserPool parserPool() {
        BasicParserPool parserPool = new BasicParserPool();
        parserPool.setNamespaceAware(true);
        return parserPool;
    }

    @Bean
    public SAMLMessageDecoder decoder() {
        return new HTTPPostDecoder(parserPool());
    }

    @Bean
    public SAMLSignatureProfileValidator validator() {
        return new SAMLSignatureProfileValidator();
    }

    @Bean
    public Saml2Processor processor() {
        return new Saml2Processor(decoder(), validator(), 10, 10);
    }
}
