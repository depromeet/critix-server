package depromeet.onepiece;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import depromeet.onepiece.feedback.domain.OverallEvaluation;
import org.junit.jupiter.api.Test;

// @SpringBootTest
class OnepieceApplicationTests {

  @Test
  void contextLoads() {}

  @Test
  void json() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(objectMapper);
    JsonSchema jsonSchema = jsonSchemaGenerator.generateSchema(OverallEvaluation.class);
    jsonSchema.String schemaJson =
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema);
    System.out.println(schemaJson);
  }
}
