package depromeet.onepiece.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import depromeet.onepiece.common.error.GlobalErrorCode;
import depromeet.onepiece.common.error.GlobalException;
import org.springframework.stereotype.Component;

@Component
public class ConvertService {

  private static ObjectMapper objectMapper;

  public ConvertService(ObjectMapper objectMapper) {
    ConvertService.objectMapper = objectMapper;
  }

  public static <T> T readValue(String json, Class<T> clazz) {

    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new GlobalException(e.getMessage(), GlobalErrorCode.JSON_PARSING_ERROR);
    }
  }

  public static JsonNode readTree(String json, String field) {
    try {
      return objectMapper.readTree(json).findPath(field);
    } catch (JsonProcessingException e) {
      throw new GlobalException(e.getMessage(), GlobalErrorCode.JSON_PARSING_ERROR);
    }
  }

  public static <T> T convertValue(JsonNode json, TypeReference<T> typeReference) {
    return objectMapper.convertValue(json, typeReference);
  }
}
