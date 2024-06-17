package com.pepe.miniapp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pepe.miniapp.authentication.CustomAuthenticationToken;
import com.pepe.miniapp.models.User;
import com.pepe.miniapp.payload.response.AuthResponse;
import com.pepe.miniapp.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final String botToken;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, @Value("${telegram-bot.token}") String botToken) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.botToken = botToken;
    }

    public ResponseEntity<?> authenticate(String payload, HttpServletRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(payload);

        String initDataString = jsonNode.get("initData").asText();
        System.out.println(initDataString);
        Map<String, String> params = urlParseQueryString(initDataString);
        boolean isValidHash = isValidHash(params);
        AuthResponse response = new AuthResponse();
        response.setNewUser(false);
        if (isValidHash) {
            Map<String, Object> user = getUser(params.get("user"));
            if (!userRepository.existsByTelegramId(Long.parseLong(String.valueOf(user.get("id"))))) {
                User userEntity = new User((Long) user.get("id"));
                userEntity.setName((String) user.get("first_name"));
                userRepository.save(userEntity);
                response.setNewUser(true);
            }
            response.setLeader(1);
            response.setAuthenticated(true);
            CustomAuthenticationToken authenticationToken = new CustomAuthenticationToken(user.get("id"), params.get("hash"));
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        } else {
            response.setAuthenticated(false);
            response.setLeader(0);
            response.setNewUser(false);
        }
        return ResponseEntity.ok().body(response);
    }

    private Map<String, Object> getUser(String userData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(userData, Map.class);
    }

    private boolean isValidHash(Map<String, String> params) {
        try {
            String hash = params.remove("hash");
            String dataCheckString = params.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("\n"));
            byte[] secretKey = HMAC_SHA256(botToken, "WebAppData".getBytes(StandardCharsets.UTF_8));
            String computedHash = bytesToHex(HMAC_SHA256(dataCheckString, secretKey));
            return computedHash.equals(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException exception) {
            return false;
        }
    }

    public static byte[] HMAC_SHA256(String value, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
        sha256Hmac.init(secretKey);
        return sha256Hmac.doFinal(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static Map<String, String> urlParseQueryString(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (queryString == null || queryString.isEmpty()) {
            return params;
        }

        for (String param : queryString.split("&")) {
            String[] paramParts = param.split("=", 2);
            String paramName = urlSafeDecode(paramParts[0]);
            String paramValue = paramParts.length > 1 ? urlSafeDecode(paramParts[1]) : null;
            params.put(paramName, paramValue);
        }
        return params;
    }

    private static String urlSafeDecode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
