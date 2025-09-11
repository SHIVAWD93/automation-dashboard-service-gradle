package com.qa.automation.config;

import java.util.Base64;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Data
public class JiraConfig {

    @Value("${jira.url:}")
    private String jiraUrl;

    @Value("${jira.username:}")
    private String jiraUsername;

    @Value("${jira.token:}")
    private String jiraToken;

    @Value("${jira.project.key:}")
    private String jiraProjectKey;

    @Value("${jira.board.id:}")
    private String jiraBoardId;

    // qTest configuration properties
    @Value("${qtest.url:}")
    private String qtestUrl;

    @Value("${qtest.username:}")
    private String qtestUsername;

    @Value("${qtest.password:}")
    private String qtestPassword;

    @Value("${qtest.token:}")
    private String qtestToken;

    @Value("${qtest.project.id:}")
    private String qtestProjectId;

    @Bean
    public WebClient jiraWebClient() {
        // Increase memory limit for large Jira responses
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)) // 16MB
                .build();

        return WebClient.builder()
                .baseUrl(jiraUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, getJiraBasicAuthHeader())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .exchangeStrategies(strategies)
                .build();
    }

    @Bean
    public WebClient qtestWebClient() {
        // Increase memory limit for large qTest responses
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)) // 16MB
                .build();

        // Create base WebClient without authentication headers (will be added per request)
        String baseUrl = (qtestUrl != null && !qtestUrl.isEmpty()) ? qtestUrl : "http://localhost";

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .exchangeStrategies(strategies)
                .build();
    }

    private String getJiraBasicAuthHeader() {
        if (!StringUtils.hasLength(jiraUsername) || !StringUtils.hasLength(jiraToken)) {
            return "";
        }
        String credentials = jiraUsername + ":" + jiraToken;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }


    // Configuration validation methods
    public boolean isJiraConfigured() {
        return StringUtils.hasLength(jiraUrl) &&
                StringUtils.hasLength(jiraUsername) &&
                StringUtils.hasLength(jiraToken);
    }

    public boolean isQTestConfigured() {
        return StringUtils.hasLength(qtestUrl) &&
                StringUtils.hasLength(qtestUsername) &&
                (StringUtils.hasLength(qtestPassword) || StringUtils.hasLength(qtestToken));
    }

    // Legacy method for backward compatibility
    public boolean isConfigured() {
        return isJiraConfigured();
    }
}
