package depromeet.onepiece.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebMvcConfig {

  @Bean
  public JsonMapper objectMapper() {
    return JsonMapper.builder()
        .addModule(
            new SimpleModule()
                .addDeserializer(ObjectId.class, objectIdJsonDeserializer())
                .addSerializer(ObjectId.class, objectIdJsonSerializer()))
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
}
