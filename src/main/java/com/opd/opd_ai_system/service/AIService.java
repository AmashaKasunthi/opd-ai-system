package com.opd.opd_ai_system.service;

import com.opd.opd_ai_system.dto.AIPredictionResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    private final RestTemplate restTemplate = new RestTemplate();

    public AIPredictionResponse predict(String symptoms){

        String url = "http://localhost:5000/predict";

        Map<String,Object> request = new HashMap<>();

        request.put("symptoms", symptoms);

        return restTemplate.postForObject(
                url,
                request,
                AIPredictionResponse.class
        );

    }

}
