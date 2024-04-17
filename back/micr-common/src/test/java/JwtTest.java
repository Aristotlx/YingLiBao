import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtTest {
    @Test
    public void testCreateJwt(){
        String key  = "3f255f8f3e8b4e9a8b5962aba970be0c";
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        Date curDate = new Date();
        Map<String, Object> data = new HashMap<>();
        data.put("userId",1001);
        data.put("name","李四");
        data.put("role","经理");

        String jwt = Jwts.builder().signWith(secretKey, SignatureAlgorithm.HS256)
                .setExpiration(DateUtils.addMinutes(curDate, 10))
                .setIssuedAt(curDate)
                .setId(UUID.randomUUID().toString())
                .addClaims(data).compact();

        System.out.println("jwt = " + jwt);
//        jwt = eyJhbGciOiJIUzI1NiJ9
//        .eyJleHAiOjE3MDM3NjQ3ODIsImlhdCI6MTcwMzc2NDE4MiwianRpIjoiYmRjYWY1ODQtNjgzMC00MDU1LTgyZGUtNzE4YzMwZjIzMDk2Iiwicm9sZSI6Iue7j-eQhiIsIm5hbWUiOiLmnY7lm5siLCJ1c2VySWQiOjEwMDF9
//        .KalLZy5YSNI_B-zh0SPz-jXC8ZmLs9rfT6FM6xMN7_4

    }

    @Test
    public void testReadJwt(){
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MDM3NjQ3ODIsImlhdCI6MTcwMzc2NDE4MiwianRpIjoiYmRjYWY1ODQtNjgzMC00MDU1LTgyZGUtNzE4YzMwZjIzMDk2Iiwicm9sZSI6Iue7j-eQhiIsIm5hbWUiOiLmnY7lm5siLCJ1c2VySWQiOjEwMDF9.KalLZy5YSNI_B-zh0SPz-jXC8ZmLs9rfT6FM6xMN7_4";
        String key  = "3f255f8f3e8b4e9a8b5962aba970be0c";
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

    }
}
