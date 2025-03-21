package depromeet.onepiece.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import depromeet.onepiece.common.auth.resolver.CurrentUserArgumentResolver;
import depromeet.onepiece.common.eventsourcing.interceptor.GPTEventInterceptor;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
  private final CurrentUserArgumentResolver currentUserArgumentResolver;
  private final GPTEventInterceptor gptEventInterceptor;

  @Bean
  public JsonMapper objectMapper() {
    return JsonMapper.builder()
        .addModule(
            new SimpleModule()
                .addDeserializer(ObjectId.class, objectIdJsonDeserializer())
                .addSerializer(ObjectId.class, objectIdJsonSerializer()))
        .addModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build();
  }

  @Bean
  public JsonDeserializer<ObjectId> objectIdJsonDeserializer() {
    return new JsonDeserializer<>() {
      @Override
      public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new ObjectId(p.getText());
      }
    };
  }

  @Bean
  public JsonSerializer<ObjectId> objectIdJsonSerializer() {
    return new JsonSerializer<>() {
      @Override
      public void serialize(ObjectId value, JsonGenerator gen, SerializerProvider serializers)
          throws IOException {
        gen.writeString(value.toString());
      }
    };
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(currentUserArgumentResolver);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(gptEventInterceptor);
  }
}
