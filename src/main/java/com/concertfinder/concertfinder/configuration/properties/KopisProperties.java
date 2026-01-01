package com.concertfinder.concertfinder.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kopis.api")
public class KopisProperties {

    private String key;
    private String baseUrl;
}
