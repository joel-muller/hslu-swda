package ch.hslu.swda.business;

import ch.hslu.swda.entities.SystemRights;
import ch.hslu.swda.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

public class JWTGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(JWTGenerator.class);

    public static String generateJWT(User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKey = System.getenv("PRIVATE_KEY");
        privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        List<String> permissions = new ArrayList<>();
        for (SystemRights rights : user.getRole().getRights()) {
            permissions.add(rights.getName());
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("permissions", permissions);

        return Jwts.builder()
                .claims(claims)
                .signWith(key, SignatureAlgorithm.RS512)
                .compact();
    }
}
