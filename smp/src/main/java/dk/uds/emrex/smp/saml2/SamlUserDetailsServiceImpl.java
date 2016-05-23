package dk.uds.emrex.smp.saml2;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

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
                final String samlAttributeName = samlAttributeAnnotation.value();

                final String[] samlAttributeValues = samlCredential.getAttributeAsStringArray(samlAttributeName);

                setFieldOnObject(user, field, samlAttributeValues);
            }
        }
    }

    private static void setFieldOnObject(Object obj, Field field, String[] values) throws IllegalAccessException {
        if (values == null || values.length == 0) {
            return;
        }

        field.setAccessible(true);

        final Class<?> fieldType = field.getType();

        if (fieldType == String.class && !"null".equals(values[0])) {
            field.set(obj, values[0]);
        } else if (Iterable.class.isAssignableFrom(fieldType)) {
            field.set(obj, Arrays.asList(values));
        } else if (DateTime.class.isAssignableFrom(fieldType)) {
            field.set(obj, DateTime.parse(values[0], DateTimeFormat.forPattern("yyyyMMdd")));
        } else if (fieldType == int.class || fieldType == Integer.class) {
            field.set(obj, Integer.parseInt(values[0]));
        } else {
            throw new IllegalArgumentException("Unknown SAML field type: " + fieldType.getName());
        }
    }
}