package com.example.faceauth.service;

import com.example.faceauth.model.UserEmbedding;
import com.example.faceauth.repository.UserEmbeddingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class FaceAuthService {

    @Autowired
    private UserEmbeddingRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper mapper = new ObjectMapper();
    @Value("${python.url}")
    private String PYTHON_URL;

    public double[] getEmbeddingFromPython(byte[] imageBytes) throws Exception {
        String base64 = java.util.Base64.getEncoder().encodeToString(imageBytes);
        Map<String, String> request = new HashMap<>();
        request.put("image", "data:image/jpeg;base64," + base64);

        Map<String, Object> response = restTemplate.postForObject(PYTHON_URL, request, Map.class);
        return mapper.convertValue(response.get("embedding"), double[].class);
    }

    public boolean verifyUser(String userId, double[] liveEmbedding) {
        return repository.findByUserId(userId).map(saved -> {
            try {
                double[] savedEmb = mapper.readValue(saved.getEmbedding(), double[].class);
                int len = Math.min(liveEmbedding.length, savedEmb.length);
                double dot = 0, n1 = 0, n2 = 0;
                for (int i = 0; i < len; i++) {
                    dot += liveEmbedding[i] * savedEmb[i];
                    n1 += liveEmbedding[i] * liveEmbedding[i];
                    n2 += savedEmb[i] * savedEmb[i];
                }
                double sim = dot / (Math.sqrt(n1) * Math.sqrt(n2) + 1e-8);
                return sim >= 0.85;
            } catch (Exception e) {
                return false;
            }
        }).orElse(false);
    }

    public void saveEmbedding(String userId, double[] embedding) throws Exception {
        if (repository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("User ID already registered!");
        }
        UserEmbedding entity = new UserEmbedding();
        entity.setUserId(userId);
        entity.setEmbedding(mapper.writeValueAsString(embedding));
        repository.save(entity);
    }
}