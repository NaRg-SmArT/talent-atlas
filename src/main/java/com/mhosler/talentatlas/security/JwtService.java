package com.mhosler.TalentAtlas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final long ACCESS_TOKEN_EXPIRATION_MILLIS = 1000L * 60 * 15;
    private static final long REFRESH_TOKEN_EXPIRATION_MILLIS = 1000L * 60 * 60 * 24 * 7;

    private static final String TOKEN_TYPE_CLAIM = "tokenType";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtService(
            @Value("${jwt.private-key:}") String privateKeyPem,
            @Value("${jwt.public-key:}") String publicKeyPem
    ) throws Exception {
        this.privateKey = loadPrivateKey(privateKeyPem, "jwt-private.pem");
        this.publicKey = loadPublicKey(publicKeyPem, "jwt-public.pem");
    }

    public String generateAccessToken(String subject) {
        return generateToken(
                subject,
                Map.of(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE),
                ACCESS_TOKEN_EXPIRATION_MILLIS
        );
    }

    public String generateRefreshToken(String subject) {
        return generateToken(
                subject,
                Map.of(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE),
                REFRESH_TOKEN_EXPIRATION_MILLIS
        );
    }

    private String generateToken(String subject, Map<String, Object> claims, long expirationMillis) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMillis))
                .signWith(privateKey)
                .compact();
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get(TOKEN_TYPE_CLAIM, String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsResolver.apply(claims);
    }

    public boolean isAccessTokenValid(String token, String expectedSubject) {
        return isTokenValid(token, expectedSubject, ACCESS_TOKEN_TYPE);
    }

    public boolean isRefreshTokenValid(String token, String expectedSubject) {
        return isTokenValid(token, expectedSubject, REFRESH_TOKEN_TYPE);
    }

    private boolean isTokenValid(String token, String expectedSubject, String expectedTokenType) {
        String subject = extractSubject(token);
        String tokenType = extractTokenType(token);
        Date expiration = extractClaim(token, Claims::getExpiration);

        return subject.equals(expectedSubject)
                && expectedTokenType.equals(tokenType)
                && expiration.after(new Date());
    }

    private PrivateKey loadPrivateKey(String pemFromEnv, String fallbackFilename) throws Exception {
        String key = readPem(pemFromEnv, fallbackFilename)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\\n", "\n")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String pemFromEnv, String fallbackFilename) throws Exception {
        String key = readPem(pemFromEnv, fallbackFilename)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\\n", "\n")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private String readPem(String pemFromEnv, String fallbackFilename) throws Exception {
        if (pemFromEnv != null && !pemFromEnv.isBlank()) {
            return pemFromEnv;
        }
        return readResourceFile(fallbackFilename);
    }

    private String readResourceFile(String filename) throws Exception {
        ClassPathResource resource = new ClassPathResource(filename);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}

