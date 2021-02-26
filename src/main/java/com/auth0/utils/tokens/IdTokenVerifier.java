package com.auth0.utils.tokens;

import com.auth0.exception.IdTokenValidationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.utils.Asserts;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Provides utility methods for validating an OIDC-compliant ID token.
 * See the <a href="https://openid.net/specs/openid-connect-core-1_0-final.html#IDTokenValidation">OIDC Specification</a> for more information.
 * <p>
 * This class is not thread-safe:
 * It makes use of {@link Date} and {@link Calendar} classes to verify time sensitive claims.
 */
public final class IdTokenVerifier {

    private static final Integer DEFAULT_LEEWAY = 60; // 1 min = 60 sec

    private static final String NONCE_CLAIM = "nonce";
    private static final String AZP_CLAIM = "azp";
    private static final String AUTH_TIME_CLAIM = "auth_time";

    private final String issuer;
    private final String audience;
    private final Integer leeway;
    private final Date clock;
    private final SignatureVerifier signatureVerifier;
    private final String organization;

    private IdTokenVerifier(Builder builder) {
        this.issuer = builder.issuer;
        this.audience = builder.audience;
        this.leeway = builder.leeway;
        this.signatureVerifier = builder.signatureVerifier;
        this.clock = builder.clock;
        this.organization = builder.organization;
    }

    /**
     * Initialize an instance of {@code IdTokenVerifier}.
     *
     * @param issuer            the expected issuer of the token. Must not be null.
     * @param audience          the expected audience of the token. Must not be null.
     * @param signatureVerifier the {@code SignatureVerifier} to use when verifying the token. Must not be null.
     * @return a {@linkplain Builder} for further configuration.
     */
    public static Builder init(String issuer, String audience, SignatureVerifier signatureVerifier) {
        return new Builder(issuer, audience, signatureVerifier);
    }

    /**
     * Verifies a provided ID Token follows the <a href="https://openid.net/specs/openid-connect-core-1_0-final.html#IDTokenValidation">OIDC specification.</a>
     *
     * @param token the ID Token to verify. Must not be null or empty.
     * @throws IdTokenValidationException if:
     *                                    <ul>
     *                                        <li>The ID token is null</li>
     *                                        <li>The ID token's signing algorithm is not supported</li>
     *                                        <li>The ID token's signature is invalid</li>
     *                                        <li>Any of the ID token's claims are invalid</li>
     *                                    </ul>
     * @see IdTokenVerifier#verify(String, String)
     * @see IdTokenVerifier#verify(String, String, Integer)
     */
    public void verify(String token) throws IdTokenValidationException {
        verify(token, null);
    }

    /**
     * Verifies a provided ID Token follows the <a href="https://openid.net/specs/openid-connect-core-1_0-final.html#IDTokenValidation">OIDC specification.</a>
     *
     * @param token the ID Token to verify.
     * @param nonce the nonce expected on the ID token, which must match the nonce specified on the authorization request.
     *              If null, no validation of the nonce will occur.
     * @throws IdTokenValidationException if:
     *                                    <ul>
     *                                        <li>The ID token is null</li>
     *                                        <li>The ID token's signing algorithm is not supported</li>
     *                                        <li>The ID token's signature is invalid</li>
     *                                        <li>Any of the ID token's claims are invalid</li>
     *                                    </ul>
     * @see IdTokenVerifier#verify(String)
     * @see IdTokenVerifier#verify(String, String, Integer)
     */
    public void verify(String token, String nonce) throws IdTokenValidationException {
        verify(token, nonce, null);
    }

    /**
     * Verifies a provided ID Token follows the <a href="https://openid.net/specs/openid-connect-core-1_0-final.html#IDTokenValidation">OIDC specification.</a>
     *
     * @param token                the ID Token to verify. Must not be null or empty.
     * @param nonce                the nonce expected on the ID token, which must match the nonce specified on the authorization request.
     *                             If null, no validation of the nonce will occur.
     * @param maxAuthenticationAge The maximum authentication age allowed, which specifies the allowable elapsed time in seconds
     *                             since the last time the end-user was actively authenticated. This must match the specified
     *                             {@code max_age} parameter specified on the authorization request. If null, no validation
     *                             of the {@code auth_time} claim will occur.
     * @throws IdTokenValidationException if:
     *                                    <ul>
     *                                        <li>The ID token is null</li>
     *                                        <li>The ID token's signing algorithm is not supported</li>
     *                                        <li>The ID token's signature is invalid</li>
     *                                        <li>Any of the ID token's claims are invalid</li>
     *                                    </ul>
     * @see IdTokenVerifier#verify(String)
     * @see IdTokenVerifier#verify(String, String)
     */
    public void verify(String token, String nonce, Integer maxAuthenticationAge) throws IdTokenValidationException {

        if (isEmpty(token)) {
            throw new IdTokenValidationException("ID token is required but missing");
        }

        DecodedJWT decoded = this.signatureVerifier.verifySignature(token);

        if (isEmpty(decoded.getIssuer())) {
            throw new IdTokenValidationException("Issuer (iss) claim must be a string present in the ID token");
        }
        if (!decoded.getIssuer().equals(this.issuer)) {
            throw new IdTokenValidationException(String.format("Issuer (iss) claim mismatch in the ID token, expected \"%s\", found \"%s\"", this.issuer, decoded.getIssuer()));
        }

        if (isEmpty(decoded.getSubject())) {
            throw new IdTokenValidationException("Subject (sub) claim must be a string present in the ID token");
        }

        final List<String> audience = decoded.getAudience();
        if (audience == null) {
            throw new IdTokenValidationException("Audience (aud) claim must be a string or array of strings present in the ID token");
        }
        if (!audience.contains(this.audience)) {
            throw new IdTokenValidationException(String.format("Audience (aud) claim mismatch in the ID token; expected \"%s\" but found \"%s\"", this.audience, decoded.getAudience()));
        }

        // Org verification
        if (this.organization != null) {
            String orgClaim = decoded.getClaim("org_id").asString();
            if (!this.organization.equals(orgClaim)) {
                throw new IdTokenValidationException(String.format("Organization (org_id) claim mismatch in the ID token; expected \"%s\" but found \"%s\"", this.organization, decoded.getClaim("org_id").asString()));
            }

        }

        final Calendar cal = Calendar.getInstance();
        final Date now = this.clock != null ? this.clock : cal.getTime();
        final int clockSkew = this.leeway != null ? this.leeway : DEFAULT_LEEWAY;

        if (decoded.getExpiresAt() == null) {
            throw new IdTokenValidationException("Expiration Time (exp) claim must be a number present in the ID token");
        }

        cal.setTime(decoded.getExpiresAt());
        cal.add(Calendar.SECOND, clockSkew);
        Date expDate = cal.getTime();

        if (now.after(expDate)) {
            throw new IdTokenValidationException(String.format("Expiration Time (exp) claim error in the ID token; current time (%d) is after expiration time (%d)", now.getTime() / 1000, expDate.getTime() / 1000));
        }

        if (decoded.getIssuedAt() == null) {
            throw new IdTokenValidationException("Issued At (iat) claim must be a number present in the ID token");
        }

        cal.setTime(decoded.getIssuedAt());
        cal.add(Calendar.SECOND, -1 * clockSkew);

        if (nonce != null) {
            String nonceClaim = decoded.getClaim(NONCE_CLAIM).asString();
            if (isEmpty(nonceClaim)) {
                throw new IdTokenValidationException("Nonce (nonce) claim must be a string present in the ID token");
            }
            if (!nonce.equals(nonceClaim)) {
                throw new IdTokenValidationException(String.format("Nonce (nonce) claim mismatch in the ID token; expected \"%s\", found \"%s\"", nonce, nonceClaim));
            }
        }

        if (audience.size() > 1) {
            String azpClaim = decoded.getClaim(AZP_CLAIM).asString();
            if (isEmpty(azpClaim)) {
                throw new IdTokenValidationException("Authorized Party (azp) claim must be a string present in the ID token when Audience (aud) claim has multiple values");
            }
            if (!this.audience.equals(azpClaim)) {
                throw new IdTokenValidationException(String.format("Authorized Party (azp) claim mismatch in the ID token; expected \"%s\", found \"%s\"", this.audience, azpClaim));
            }
        }

        if (maxAuthenticationAge != null) {
            Date authTime = decoded.getClaim(AUTH_TIME_CLAIM).asDate();
            if (authTime == null) {
                throw new IdTokenValidationException("Authentication Time (auth_time) claim must be a number present in the ID token when Max Age (max_age) is specified");
            }

            cal.setTime(authTime);
            cal.add(Calendar.SECOND, maxAuthenticationAge);
            cal.add(Calendar.SECOND, clockSkew);
            Date authTimeDate = cal.getTime();

            if (now.after(authTimeDate)) {
                throw new IdTokenValidationException(String.format("Authentication Time (auth_time) claim in the ID token indicates that too much time has passed since the last end-user authentication. Current time (%d) is after last auth at (%d)", now.getTime() / 1000, authTimeDate.getTime() / 1000));
            }
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /**
     * Builder class to construct a {@linkplain IdTokenVerifier}
     */
    public static class Builder {

        private final String issuer;
        private final String audience;
        private final SignatureVerifier signatureVerifier;

        private Integer leeway;
        private Date clock;
        private String organization;

        /**
         * Create a new Builder instance.
         *
         * @param issuer            the expected issuer of the token. Must not be null.
         * @param audience          the expected audience of the token. Must not be null.
         * @param signatureVerifier the {@code SignatureVerifier} to use when verifying the token. Must not be null.
         */
        private Builder(String issuer, String audience, SignatureVerifier signatureVerifier) {
            Asserts.assertNotNull(issuer, "issuer");
            Asserts.assertNotNull(audience, "audience");
            Asserts.assertNotNull(signatureVerifier, "signatureVerifier");

            this.issuer = issuer;
            this.audience = audience;
            this.signatureVerifier = signatureVerifier;
        }

        /**
         * Specify a custom leeway when validating time-based claims such as {@code exp} and {@code auth_time}.
         * If not specified, a default leeway of 60 seconds will be used.
         *
         * @param leeway the custom leeway to use when validating time-based claims, in seconds.
         * @return this Builder instance.
         */
        public Builder withLeeway(Integer leeway) {
            this.leeway = leeway;
            return this;
        }

        /**
         * Specify a custom clock to use as the current time when validating time-based claims. Exposed for testing
         * purposes only.
         *
         * @param clock the clock to use as the current time.
         * @return this Builder instance.
         */
        Builder withClock(Date clock) {
            this.clock = clock;
            return this;
        }

        Builder withOrganization(String organization) {
            this.organization = organization;
            return this;
        }

        /**
         * Constructs an {@linkplain IdTokenVerifier} instance from this Builder.
         *
         * @return an initialized instance of {@code IdTokenVerifier}.
         */
        public IdTokenVerifier build() {
            return new IdTokenVerifier(this);
        }
    }
}
