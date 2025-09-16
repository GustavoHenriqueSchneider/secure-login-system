package com.securelogin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "roles")
public class Role {
    
    @Id
    private String id;
    
    private String name;
    
    private String description;
    
    /**
     * Construtor simplificado para criação de roles
     */
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
