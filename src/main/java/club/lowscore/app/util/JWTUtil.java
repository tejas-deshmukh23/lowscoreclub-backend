package club.lowscore.app.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    private static final String SECRET = "XX#$%()(#*!()!KL<><MQ7LM4NQN2QJQ0K sdkjsrow323454fd>?N<:{LWPW";
    private static final String PAYLOAD = "payload";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public static <T> String sign(T object, long maxAge) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(object);
            return JWT.create()
                    .withClaim(PAYLOAD, jsonString)
                    .withExpiresAt(new Date(System.currentTimeMillis() + maxAge))
                    .sign(ALGORITHM);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object", e);
        }
    }

    public static <T> T unsign(String token, Class<T> classT) {
        try {
            DecodedJWT jwt = JWT.require(ALGORITHM)
                    .build()
                    .verify(token);
            
            String payloadJson = jwt.getClaim(PAYLOAD).asString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(payloadJson, classT);
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid token", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize payload", e);
        }
    }

    public static String generateToken(Map<String, Object> claims, long expiration) {
        return JWT.create()
                .withPayload(claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(ALGORITHM);
    }

    public static Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}
