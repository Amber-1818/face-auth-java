package com.example.faceauth.repository;

import com.example.faceauth.model.UserEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserEmbeddingRepository extends JpaRepository<UserEmbedding, Long> {
    Optional<UserEmbedding> findByUserId(String userId);
}