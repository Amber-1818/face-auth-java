package com.example.faceauth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_embeddings")
public class UserEmbedding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;

    // YE LINE CHANGE KARO — VARCHAR(255) → CLOB (UNLIMITED)
    @Lob
    @Column(columnDefinition = "CLOB")
    private String embedding;

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getEmbedding() { return embedding; }
    public void setEmbedding(String embedding) { this.embedding = embedding; }
}