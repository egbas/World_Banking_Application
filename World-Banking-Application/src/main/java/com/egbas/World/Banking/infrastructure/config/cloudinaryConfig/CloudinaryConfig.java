package com.egbas.World.Banking.infrastructure.config.cloudinaryConfig;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    //get the cloudinary details from application.properties
    private final String CLOUD_NAME = "ddkkrlobj";
    private final String API_KEY = "993777699996285";
    private final String API_SECRET = "LizYajFB2p-sRpvhYHXUxr2Y98c";

    @Bean
    public Cloudinary cloudinary(){

        Map<String, String> config = new HashMap<>();

        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);

        return new Cloudinary(config);
    }
}
