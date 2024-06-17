package com.pepe.miniapp.authentication;

import com.pepe.miniapp.authentication.details.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class SessionService {

    private final MongoTemplate mongoTemplate;
    private final UserDetailsServiceImpl userDetailsService;
    private static final String SESSION_COLLECTION_NAME = "sessions";

    public SessionService(MongoTemplate mongoTemplate, UserDetailsServiceImpl userDetailsService) {
        this.mongoTemplate = mongoTemplate;
        this.userDetailsService = userDetailsService;
    }

    public boolean validateSession(String sessionBase64) {
        String sessionId = decodeBase64ToUUID(sessionBase64);
        Query query = new Query(Criteria.where("_id").is(sessionId).and("expiryDate").gt(LocalDateTime.now()));
        return mongoTemplate.exists(query, Session.class);
    }

    public UserDetails getUserDetails(String sessionBase64) {
        String sessionId = decodeBase64ToUUID(sessionBase64);
        Query query = new Query(Criteria.where("_id").is(sessionId));
        HttpSession session = mongoTemplate.findOne(query, HttpSession.class, SESSION_COLLECTION_NAME);
        assert session != null;
        SecurityContext context = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        String username = (String) context.getAuthentication().getPrincipal();
        return userDetailsService.loadUserByUsername(username);
    }

    public String decodeBase64ToUUID(String base64) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        return new String(decodedBytes);
    }
}
