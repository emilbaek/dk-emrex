package dk.uds.emrex.smp.saml2;

import org.apache.commons.lang.NotImplementedException;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.common.binding.decoding.SAMLMessageDecoder;
import org.opensaml.common.binding.security.MessageReplayRule;
import org.opensaml.saml2.core.Response;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.util.storage.MapBasedStorageService;
import org.opensaml.util.storage.ReplayCache;
import org.opensaml.util.storage.ReplayCacheEntry;
import org.opensaml.util.storage.StorageService;
import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.opensaml.xml.security.SecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sj on 07-04-16.
 */

// Implemented from code at https://wiki.shibboleth.net/confluence/display/OpenSAML/OSTwoUserManualPsedocodeSP
public class Saml2Processor {

    private final SAMLMessageDecoder decoder;

    private final SAMLSignatureProfileValidator validator;

    private final int replayCacheMinutes;

    private final int clockSkewSeconds;

    private final MessageReplayRule messageReplayRule;

    public Saml2Processor(SAMLMessageDecoder decoder, SAMLSignatureProfileValidator validator, int replayCacheMinutes, int clockSkewSeconds) {
        this.decoder = decoder;
        this.validator = validator;
        this.replayCacheMinutes = replayCacheMinutes;
        this.clockSkewSeconds = clockSkewSeconds;

        final ReplayCache replayCache = new ReplayCache(new MapBasedStorageService<>(), 60 * 1000 * replayCacheMinutes);
        this.messageReplayRule = new MessageReplayRule(replayCache);
    }

    public void decode(final HttpServletRequest request) throws SecurityException, MessageDecodingException {
        final MessageContext messageContext = new BasicSAMLMessageContext<>();
        messageContext.setInboundMessageTransport(new HttpServletRequestAdapter(request));

        this.decoder.decode(messageContext);

        this.messageReplayRule.evaluate(messageContext);

        final Response samlResponse = (Response) messageContext.getInboundMessage();
        throw new NotImplementedException("Not done yet");
    }
}
