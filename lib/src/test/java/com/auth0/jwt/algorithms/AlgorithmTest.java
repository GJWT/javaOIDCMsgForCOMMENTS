// Copyright (c) 2017 The Authors of 'JWTS for Java'
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of
// this software and associated documentation files (the "Software"), to deal in
// the Software without restriction, including without limitation the rights to
// use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
// the Software, and to permit persons to whom the Software is furnished to do so,
// subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
// FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
// IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.auth0.jwt.algorithms;

import com.auth0.jwt.interfaces.ECDSAKeyProvider;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.charset.StandardCharsets;
import java.security.interfaces.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class AlgorithmTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Test
    public void shouldThrowHMAC256InstanceWithNullSecretBytes() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The Secret cannot be null");
        byte[] secret = null;
        Algorithm.HMAC256(secret);
    }

    @Test
    public void shouldThrowHMAC384InstanceWithNullSecretBytes() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The Secret cannot be null");
        byte[] secret = null;
        Algorithm.HMAC384(secret);
    }

    @Test
    public void shouldThrowHMAC512InstanceWithNullSecretBytes() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The Secret cannot be null");
        byte[] secret = null;
        Algorithm.HMAC512(secret);
    }

    @Test
    public void shouldThrowHMAC256InstanceWithNullSecret() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The Secret cannot be null");
        String secret = null;
        Algorithm.HMAC256(secret);
    }

    @Test
    public void shouldThrowHMAC384InstanceWithNullSecret() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The Secret cannot be null");
        String secret = null;
        Algorithm.HMAC384(secret);
    }

    @Test
    public void shouldThrowHMAC512InstanceWithNullSecret() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The Secret cannot be null");
        String secret = null;
        Algorithm.HMAC512(secret);
    }

    @Test
    public void shouldThrowRSA256InstanceWithNullKey() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Both provided Keys cannot be null.");
        RSAKey key = null;
        Algorithm.RSA256(key);
    }

    @Test
    public void shouldThrowRSA256InstanceWithNullKeys() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Both provided Keys cannot be null.");
        Algorithm.RSA256(null, null);
    }

    @Test
    public void shouldThrowRSA256InstanceWithNullKeyProvider() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The Key Provider cannot be null.");
        RSAKeyProvider provider = null;
        Algorithm.RSA256(provider);
    }

    @Test
    public void shouldThrowRSA384InstanceWithNullKey() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Both provided Keys cannot be null.");
        RSAKey key = null;
        Algorithm.RSA384(key);
    }

    @Test
    public void shouldThrowRSA384InstanceWithNullKeys() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Both provided Keys cannot be null.");
        Algorithm.RSA384(null, null);
    }

    @Test
    public void shouldThrowRSA384InstanceWithNullKeyProvider() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The Key Provider cannot be null.");
        RSAKeyProvider provider = null;
        Algorithm.RSA384(provider);
    }

    @Test
    public void shouldThrowRSA512InstanceWithNullKey() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Both provided Keys cannot be null.");
        RSAKey key = null;
        Algorithm.RSA512(key);
    }

    @Test
    public void shouldThrowRSA512InstanceWithNullKeys() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Both provided Keys cannot be null.");
        Algorithm.RSA512(null, null);
    }

    @Test
    public void shouldThrowRSA512InstanceWithNullKeyProvider() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The Key Provider cannot be null.");
        RSAKeyProvider provider = null;
        Algorithm.RSA512(provider);
    }

    @Test
    public void shouldThrowECDSA256InstanceWithNullKey() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Both provided Keys cannot be null.");
        ECKey key = null;
        Algorithm.ECDSA256(key);
    }

    @Test
    public void shouldThrowECDSA256InstanceWithNullKeys() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Both provided Keys cannot be null.");
        Algorithm.ECDSA256(null, null);
    }

    @Test
    public void shouldThrowECDSA256InstanceWithNullKeyProvider() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The Key Provider cannot be null.");
        ECDSAKeyProvider provider = null;
        Algorithm.ECDSA256(provider);
    }

    @Test
    public void shouldThrowECDSA384InstanceWithNullKey() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Both provided Keys cannot be null.");
        ECKey key = null;
        Algorithm.ECDSA384(key);
    }

    @Test
    public void shouldThrowECDSA384InstanceWithNullKeys() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Both provided Keys cannot be null.");
        Algorithm.ECDSA384(null, null);
    }

    @Test
    public void shouldThrowECDSA384InstanceWithNullKeyProvider() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The Key Provider cannot be null.");
        ECDSAKeyProvider provider = null;
        Algorithm.ECDSA384(provider);
    }

    @Test
    public void shouldThrowECDSA512InstanceWithNullKey() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Both provided Keys cannot be null.");
        ECKey key = null;
        Algorithm.ECDSA512(key);
    }

    @Test
    public void shouldThrowECDSA512InstanceWithNullKeys() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Both provided Keys cannot be null.");
        Algorithm.ECDSA512(null, null);
    }

    @Test
    public void shouldThrowECDSA512InstanceWithNullKeyProvider() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The Key Provider cannot be null.");
        ECDSAKeyProvider provider = null;
        Algorithm.ECDSA512(provider);
    }

    @Test
    public void shouldCreateHMAC256AlgorithmWithBytes() throws Exception {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(HMACAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("HmacSHA256"));
        assertThat(algorithm.getName(), is("HS256"));
    }

    @Test
    public void shouldCreateHMAC384AlgorithmWithBytes() throws Exception {
        Algorithm algorithm = Algorithm.HMAC384("secret".getBytes(StandardCharsets.UTF_8));

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(HMACAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("HmacSHA384"));
        assertThat(algorithm.getName(), is("HS384"));
    }

    @Test
    public void shouldCreateHMAC512AlgorithmWithBytes() throws Exception {
        Algorithm algorithm = Algorithm.HMAC512("secret".getBytes(StandardCharsets.UTF_8));

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(HMACAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("HmacSHA512"));
        assertThat(algorithm.getName(), is("HS512"));
    }

    @Test
    public void shouldCreateHMAC256AlgorithmWithString() throws Exception {
        Algorithm algorithm = Algorithm.HMAC256("secret");

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(HMACAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("HmacSHA256"));
        assertThat(algorithm.getName(), is("HS256"));
    }

    @Test
    public void shouldCreateHMAC384AlgorithmWithString() throws Exception {
        Algorithm algorithm = Algorithm.HMAC384("secret");

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(HMACAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("HmacSHA384"));
        assertThat(algorithm.getName(), is("HS384"));
    }

    @Test
    public void shouldCreateHMAC512AlgorithmWithString() throws Exception {
        Algorithm algorithm = Algorithm.HMAC512("secret");

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(HMACAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("HmacSHA512"));
        assertThat(algorithm.getName(), is("HS512"));
    }

    @Test
    public void shouldCreateRSA256AlgorithmWithPublicKey() throws Exception {
        RSAKey key = mock(RSAKey.class, withSettings().extraInterfaces(RSAPublicKey.class));
        Algorithm algorithm = Algorithm.RSA256(key);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(RSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA256withRSA"));
        assertThat(algorithm.getName(), is("RS256"));
    }

    @Test
    public void shouldCreateRSA256AlgorithmWithPrivateKey() throws Exception {
        RSAKey key = mock(RSAKey.class, withSettings().extraInterfaces(RSAPrivateKey.class));
        Algorithm algorithm = Algorithm.RSA256(key);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(RSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA256withRSA"));
        assertThat(algorithm.getName(), is("RS256"));
    }

    @Test
    public void shouldCreateRSA256AlgorithmWithBothKeys() throws Exception {
        RSAPublicKey publicKey = mock(RSAPublicKey.class);
        RSAPrivateKey privateKey = mock(RSAPrivateKey.class);
        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(RSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA256withRSA"));
        assertThat(algorithm.getName(), is("RS256"));
    }

    @Test
    public void shouldCreateRSA256AlgorithmWithProvider() throws Exception {
        RSAKeyProvider provider = mock(RSAKeyProvider.class);
        Algorithm algorithm = Algorithm.RSA256(provider);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(RSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA256withRSA"));
        assertThat(algorithm.getName(), is("RS256"));
    }

    @Test
    public void shouldCreateRSA384AlgorithmWithPublicKey() throws Exception {
        RSAKey key = mock(RSAKey.class, withSettings().extraInterfaces(RSAPublicKey.class));
        Algorithm algorithm = Algorithm.RSA384(key);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(RSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA384withRSA"));
        assertThat(algorithm.getName(), is("RS384"));
    }

    @Test
    public void shouldCreateRSA384AlgorithmWithPrivateKey() throws Exception {
        RSAKey key = mock(RSAKey.class, withSettings().extraInterfaces(RSAPrivateKey.class));
        Algorithm algorithm = Algorithm.RSA384(key);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(RSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA384withRSA"));
        assertThat(algorithm.getName(), is("RS384"));
    }

    @Test
    public void shouldCreateRSA384AlgorithmWithBothKeys() throws Exception {
        RSAPublicKey publicKey = mock(RSAPublicKey.class);
        RSAPrivateKey privateKey = mock(RSAPrivateKey.class);
        Algorithm algorithm = Algorithm.RSA384(publicKey, privateKey);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(RSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA384withRSA"));
        assertThat(algorithm.getName(), is("RS384"));
    }

    @Test
    public void shouldCreateRSA384AlgorithmWithProvider() throws Exception {
        RSAKeyProvider provider = mock(RSAKeyProvider.class);
        Algorithm algorithm = Algorithm.RSA384(provider);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(RSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA384withRSA"));
        assertThat(algorithm.getName(), is("RS384"));
    }

    @Test
    public void shouldCreateRSA512AlgorithmWithPublicKey() throws Exception {
        RSAKey key = mock(RSAKey.class, withSettings().extraInterfaces(RSAPublicKey.class));
        Algorithm algorithm = Algorithm.RSA512(key);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(RSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA512withRSA"));
        assertThat(algorithm.getName(), is("RS512"));
    }

    @Test
    public void shouldCreateRSA512AlgorithmWithPrivateKey() throws Exception {
        RSAKey key = mock(RSAKey.class, withSettings().extraInterfaces(RSAPrivateKey.class));
        Algorithm algorithm = Algorithm.RSA512(key);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(RSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA512withRSA"));
        assertThat(algorithm.getName(), is("RS512"));
    }

    @Test
    public void shouldCreateRSA512AlgorithmWithBothKeys() throws Exception {
        RSAPublicKey publicKey = mock(RSAPublicKey.class);
        RSAPrivateKey privateKey = mock(RSAPrivateKey.class);
        Algorithm algorithm = Algorithm.RSA512(publicKey, privateKey);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(RSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA512withRSA"));
        assertThat(algorithm.getName(), is("RS512"));
    }

    @Test
    public void shouldCreateRSA512AlgorithmWithProvider() throws Exception {
        RSAKeyProvider provider = mock(RSAKeyProvider.class);
        Algorithm algorithm = Algorithm.RSA512(provider);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(RSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA512withRSA"));
        assertThat(algorithm.getName(), is("RS512"));
    }

    @Test
    public void shouldCreateECDSA256AlgorithmWithPublicKey() throws Exception {
        ECKey key = mock(ECKey.class, withSettings().extraInterfaces(ECPublicKey.class));
        Algorithm algorithm = Algorithm.ECDSA256(key);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(ECDSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA256withECDSA"));
        assertThat(algorithm.getName(), is("ES256"));
    }

    @Test
    public void shouldCreateECDSA256AlgorithmWithPrivateKey() throws Exception {
        ECKey key = mock(ECKey.class, withSettings().extraInterfaces(ECPrivateKey.class));
        Algorithm algorithm = Algorithm.ECDSA256(key);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(ECDSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA256withECDSA"));
        assertThat(algorithm.getName(), is("ES256"));
    }

    @Test
    public void shouldCreateECDSA256AlgorithmWithBothKeys() throws Exception {
        ECPublicKey publicKey = mock(ECPublicKey.class);
        ECPrivateKey privateKey = mock(ECPrivateKey.class);
        Algorithm algorithm = Algorithm.ECDSA256(publicKey, privateKey);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(ECDSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA256withECDSA"));
        assertThat(algorithm.getName(), is("ES256"));
    }

    @Test
    public void shouldCreateECDSA256AlgorithmWithProvider() throws Exception {
        ECDSAKeyProvider provider = mock(ECDSAKeyProvider.class);
        Algorithm algorithm = Algorithm.ECDSA256(provider);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(ECDSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA256withECDSA"));
        assertThat(algorithm.getName(), is("ES256"));
    }

    @Test
    public void shouldCreateECDSA384AlgorithmWithPublicKey() throws Exception {
        ECKey key = mock(ECKey.class, withSettings().extraInterfaces(ECPublicKey.class));
        Algorithm algorithm = Algorithm.ECDSA384(key);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(ECDSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA384withECDSA"));
        assertThat(algorithm.getName(), is("ES384"));
    }

    @Test
    public void shouldCreateECDSA384AlgorithmWithPrivateKey() throws Exception {
        ECKey key = mock(ECKey.class, withSettings().extraInterfaces(ECPrivateKey.class));
        Algorithm algorithm = Algorithm.ECDSA384(key);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(ECDSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA384withECDSA"));
        assertThat(algorithm.getName(), is("ES384"));
    }

    @Test
    public void shouldCreateECDSA384AlgorithmWithBothKeys() throws Exception {
        ECPublicKey publicKey = mock(ECPublicKey.class);
        ECPrivateKey privateKey = mock(ECPrivateKey.class);
        Algorithm algorithm = Algorithm.ECDSA384(publicKey, privateKey);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(ECDSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA384withECDSA"));
        assertThat(algorithm.getName(), is("ES384"));
    }

    @Test
    public void shouldCreateECDSA384AlgorithmWithProvider() throws Exception {
        ECDSAKeyProvider provider = mock(ECDSAKeyProvider.class);
        Algorithm algorithm = Algorithm.ECDSA384(provider);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(ECDSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA384withECDSA"));
        assertThat(algorithm.getName(), is("ES384"));
    }

    @Test
    public void shouldCreateECDSA512AlgorithmWithPublicKey() throws Exception {
        ECKey key = mock(ECKey.class, withSettings().extraInterfaces(ECPublicKey.class));
        Algorithm algorithm = Algorithm.ECDSA512(key);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(ECDSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA512withECDSA"));
        assertThat(algorithm.getName(), is("ES512"));
    }

    @Test
    public void shouldCreateECDSA512AlgorithmWithPrivateKey() throws Exception {
        ECKey key = mock(ECKey.class, withSettings().extraInterfaces(ECPrivateKey.class));
        Algorithm algorithm = Algorithm.ECDSA512(key);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(ECDSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA512withECDSA"));
        assertThat(algorithm.getName(), is("ES512"));
    }

    @Test
    public void shouldCreateECDSA512AlgorithmWithBothKeys() throws Exception {
        ECPublicKey publicKey = mock(ECPublicKey.class);
        ECPrivateKey privateKey = mock(ECPrivateKey.class);
        Algorithm algorithm = Algorithm.ECDSA512(publicKey, privateKey);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(ECDSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA512withECDSA"));
        assertThat(algorithm.getName(), is("ES512"));
    }

    @Test
    public void shouldCreateECDSA512AlgorithmWithProvider() throws Exception {
        ECDSAKeyProvider provider = mock(ECDSAKeyProvider.class);
        Algorithm algorithm = Algorithm.ECDSA512(provider);

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(ECDSAAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("SHA512withECDSA"));
        assertThat(algorithm.getName(), is("ES512"));
    }

    @Test
    public void shouldCreateNoneAlgorithm() throws Exception {
        Algorithm algorithm = Algorithm.none();

        assertThat(algorithm, is(notNullValue()));
        assertThat(algorithm, is(instanceOf(NoneAlgorithm.class)));
        assertThat(algorithm.getDescription(), is("none"));
        assertThat(algorithm.getName(), is("none"));
    }

}