package com.securelogin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "login_attempts")
public class LoginAttempt {
    
    @Id
    private String id;
    
    @Field("username")
    private String username;
    
    @Field("ip_address")
    private String ipAddress;
    
    @Field("success")
    private boolean success;
    
    @Field("attempt_time")
    private LocalDateTime attemptTime;
    
    @Field("user_agent")
    private String userAgent;
    
    @Field("failure_reason")
    private String failureReason;
    
    public LoginAttempt(String username, String ipAddress, boolean success, String userAgent) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.success = success;
        this.attemptTime = LocalDateTime.now();
        this.userAgent = userAgent;
    }
}

