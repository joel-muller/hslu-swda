package ch.hslu.swda.business;

import ch.hslu.swda.model.auth.ClaimValidation;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * Authenticates JWTs on the available claims.
 */
public class TokenAuthenticator {

    private static final Logger LOG = LoggerFactory.getLogger(TokenAuthenticator.class);

    /**
     * Validates if the specified claim is in the JWT.
     * @param jwt
     * @param claim
     * @return ClaimValidation with the necessary userId
     */
    public static ClaimValidation validateClaims(String jwt, String claim) {
        Claims claims = retrieveClaims(jwt);
        if (claims == null) {
            return new ClaimValidation(false, null);
        }
        List<String> permissions = (List<String>) claims.get("permissions");
        UUID userId = UUID.fromString((String) claims.get("userId"));
        if (permissions.contains(claim)) {
            return new ClaimValidation(true, userId);
        } else {
            return new ClaimValidation(false, null);
        }
    }

    /**
     * Validates if one of the provided claims is included.
     * @param jwt
     * @param claimList
     * @return ClaimValidation with the necessary userId
     */
    public static ClaimValidation validateClaims(String jwt, List<String> claimList) {
        Claims claims = retrieveClaims(jwt);
        if (claims == null) {
            return new ClaimValidation(false, null);
        }
        List<String> permissions = (List<String>) claims.get("permissions");
        UUID userId = UUID.fromString((String) claims.get("userId"));
        for (String claim : claimList) {
            if (permissions.contains(claim)) {
                return new ClaimValidation(true, userId);
            }
        }
        return new ClaimValidation(false, null);
    }

    /**
     * Retrieves the claims from the provided JWT.
     * @param jwt
     * @return Claims of the JWT
     */
    private static Claims retrieveClaims(String jwt) {
        try {
            String publicKey = System.getenv("PUBLIC_KEY");
            publicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] keyBytes = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey key = KeyFactory.getInstance("RSA").generatePublic(keySpec);
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims;
        } catch (InvalidKeySpecException e) {
            LOG.error("Public Key could not be loaded");
            return null;
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Algorithm is invalid, could not get key");
            return null;
        } catch (Exception e) {
            LOG.error("Error occurred parsing JWT \"[{}]\": [{}]", jwt, e.getMessage());
            return null;
        }
    }
}
