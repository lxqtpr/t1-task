package dev.lxqtpr.linda.t1task.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Arrays;

@Configuration
public class RestTemplateConfig {
    @Value("${clients.url.t1}")
    private String baseUrl;

    @Bean
    public RestTemplate restTemplate() {
        var template = new RestTemplate();
        template.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));
        template.getMessageConverters().addFirst(converter);
        return template;
    }
}
