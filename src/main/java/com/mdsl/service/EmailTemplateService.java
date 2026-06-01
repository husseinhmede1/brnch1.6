package com.mdsl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
@Service
public class EmailTemplateService {

    private final ResourceLoader resourceLoader;
    
    @Value("${email.template.base-path:classpath:email-templates/}")
    private String templateBasePath;

    public EmailTemplateService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Load and process an email template with parameter substitution
     * 
     * @param templateName Name of the template file (e.g., "entity-termination-notification.html")
     * @param parameters Map of placeholder names to values (e.g., "ENTITY_ID" -> "12345")
     * @return Processed HTML content
     */
    public String loadTemplate(String templateName, Map<String, String> parameters) throws IOException {
        String templateContent = loadTemplateFile(templateName);
        return substituteParameters(templateContent, parameters);
    }

    /**
     * Load template from classpath or file system
     */
    private String loadTemplateFile(String templateName) throws IOException {
        try {
            // Try loading from classpath first (resources folder)
            Resource resource = resourceLoader.getResource(templateBasePath + templateName);
            if (resource.exists()) {
                return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.warn("Failed to load template from classpath: {}", templateName);
        }

        // If not in classpath, try file system path
        Path filePath = Paths.get(templateBasePath.replace("classpath:", ""), templateName);
        if (Files.exists(filePath)) {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        }

        throw new IOException("Email template not found: " + templateName);
    }

    /**
     * Replace all {{PLACEHOLDER}} markers with actual values
     */
    private String substituteParameters(String template, Map<String, String> parameters) {
        String result = template;
        
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue() : "";
            result = result.replace(placeholder, value);
        }
        
        return result;
    }
}