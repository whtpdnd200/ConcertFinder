package com.concertfinder.concertfinder.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

// properties.yml의 값들을 매핑 시켜주기 위한 클래스
@Data
// yml로 작업 해서 prefix 속성으로 추가로 어디에 있는 정보랑 매핑해야 하는지 알려줘야 함
@ConfigurationProperties(prefix = "kopis.api")
public class KopisProperties {

    private String key;
    private String baseUrl;
}
