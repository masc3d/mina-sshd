/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sshd.common.util.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.sshd.common.cipher.BuiltinCiphers;
import org.apache.sshd.common.cipher.CipherInformation;
import org.apache.sshd.util.test.BaseTestSupport;
import org.apache.sshd.util.test.JUnit4ClassRunnerWithParametersFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

/**
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)   // see https://github.com/junit-team/junit/wiki/Parameterized-tests
@UseParametersRunnerFactory(JUnit4ClassRunnerWithParametersFactory.class)
public class SecurityProviderRegistrarCipherNameTest extends BaseTestSupport {
    private final CipherInformation cipherInfo;

    public SecurityProviderRegistrarCipherNameTest(CipherInformation cipherInfo) {
        this.cipherInfo = cipherInfo;
    }

    @Parameters(name = "{0}")
    public static List<Object[]> parameters() {
        return Collections.unmodifiableList(
                new ArrayList<Object[]>(BuiltinCiphers.VALUES.size()) {
                    // Not serializing it
                    private static final long serialVersionUID = 1L;

                    {
                        for (CipherInformation cipherInfo : BuiltinCiphers.VALUES) {
                            String algorithm = cipherInfo.getAlgorithm();
                            String xform = cipherInfo.getTransformation();
                            if (!xform.startsWith(algorithm)) {
                                continue;
                            }

                            add(new Object[]{cipherInfo});
                        }
                    }
        });
    }

    @Test
    public void testGetEffectiveSecurityEntityName() {
        String expected = cipherInfo.getAlgorithm();
        String actual = SecurityProviderRegistrar.getEffectiveSecurityEntityName(Cipher.class, cipherInfo.getTransformation());
        assertEquals("Mismatched pure cipher name", expected, actual);
    }
}
