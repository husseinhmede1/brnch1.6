package com.mdsl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties
@Getter
@Setter
public class AppProperties {

	private String appVersion;
	private int appId; 
}