package dk.uds.emrex.ncp.saml2;

/*
 * Copyright 2016 Vincenzo De Notaris
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.XMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class SamlUserDetailsServiceImpl implements SAMLUserDetailsService {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(SamlUserDetailsServiceImpl.class);

    @Autowired
    @Qualifier("samlUserClass")
    private Class<?> userClass;

    // TODO
    public Object loadUserBySAML(SAMLCredential credential)
            throws UsernameNotFoundException {
        // The method is supposed to identify local account of user referenced by
        // data in the SAML assertion and return UserDetails object describing the user.

        try {
            final String userID = credential.getNameID().getValue();
            final Object user = userClass.getConstructor(String.class).newInstance(userID);

            fillUserFields(credential, user);

            LOG.info("User logged in: " + user.toString());

            return user;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOG.error("Error parsing SAML response.", e);
            throw new UsernameNotFoundException("Error parsing SAML response.", e);
        }
    }

    private void fillUserFields(SAMLCredential samlCredential, Object user) throws IllegalAccessException {

        for (Field field : user.getClass().getDeclaredFields()) {
            final SamlAttribute samlAttributeAnnotation = field.getAnnotation(SamlAttribute.class);
            if (samlAttributeAnnotation != null) {
                final List<String> samlAttributeNames = Arrays.asList(samlAttributeAnnotation.value());

                final Stream<Object> samlAttributeValues =
                        samlAttributeNames.stream()
                                .flatMap(an ->
                                        Optional.ofNullable(samlCredential.getAttribute(an))
                                                .map(attribute -> attribute.getAttributeValues()
                                                        .stream().map(this::getValueOfXmlObject)
                                                )
                                                .orElse(null)
                                );

                setFieldOnObject(user, field, samlAttributeValues);
            }
        }
    }

    private Object getValueOfXmlObject(XMLObject xmlObject) {
        try {
            return xmlObject.getClass().getMethod("getValue").invoke(xmlObject);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOG.error("Failed to read value of xmlObject " + xmlObject.toString());
            throw new RuntimeException(e);
        }
    }

    private static void setFieldOnObject(@NotNull Object obj, @NotNull Field field, @NotNull Stream<Object> values) throws IllegalAccessException {
        field.setAccessible(true);

        final Class<?> fieldType = field.getType();

        if (fieldType == String.class) {

            final Optional<Object> value = values.filter(s -> !"null".equals(s))
                    .findFirst();

            if (value.isPresent()) {
                field.set(obj, value.get());
            }
        } else if (Iterable.class.isAssignableFrom(fieldType)) {

            field.set(obj, values.collect(Collectors.toList()));
        } else if (DateTime.class.isAssignableFrom(fieldType)) {

            final Optional<DateTime> value = values
                    .findFirst()
                    .map(s -> DateTime.parse((String) s, DateTimeFormat.forPattern("yyyyMMdd")));

            if (value.isPresent()) {
                field.set(obj, value.get());
            }
        } else if (fieldType == int.class || fieldType == Integer.class) {

            Optional<Integer> value = values
                    .findFirst()
                    .map(s -> Integer.parseInt((String)s));

            if (value.isPresent()) {
                field.set(obj, value.get());
            }
        } else {
            throw new IllegalArgumentException("Unknown SAML field type: " + fieldType.getName());
        }
    }
}