package jdlf.compass.timetableIntegration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "custom.security.cors")
@Data
public class CorsProperties {
    private Boolean allowCredentials;
    private List<String> allowedOrigins;
    private List<String> allowedHeaders;
    private List<String> allowedMethods;
    private Long maxAge;
}
