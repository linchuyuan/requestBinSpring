/*----------------------------------------
Request Application entry
Spring
----------------------------------------*/
package com.revinate.requestbin;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@SpringBootApplication
public class RequestbinApplication {

    public static final ObjectMapper MAPPER = new ObjectMapper()
        .setSerializationInclusion(NON_NULL)
        .registerModule(new JSR310Module())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static void main(String[] args) {
        SpringApplication.run(RequestbinApplication.class, args);
    }

    @Bean
    @Primary
    public ObjectMapper mapper() {
        return MAPPER;
    }
}
