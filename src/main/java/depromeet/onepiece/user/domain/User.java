package depromeet.onepiece.user.domain;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import depromeet.onepiece.common.auth.application.dto.AuthAttributes;
import depromeet.onepiece.common.domain.BaseTimeDocument;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Builder
@Document
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@CompoundIndexes(
    value = @CompoundIndex(unique = true, name = "email_1_provider_1", def = "email_1_provider_1"))
@Getter
public class User extends BaseTimeDocument {
  @MongoId private ObjectId id;

  @Field("name")
  @NotBlank(message = "이름은 필수 입력값입니다")
  private String name;

  @Field("email")
  @NotBlank(message = "이메일은 필수 입력값입니다")
  @Email(message = "올바른 이메일 형식이 아닙니다")
  private String email;

  @Field("provider")
  @NotNull(message = "인증 제공자는 필수 입력값입니다") private OAuthProviderType provider;

  @Field("external_id")
  private String externalId;

  public static User save(AuthAttributes authAttributes) {
    return User.builder()
        .email(authAttributes.getEmail())
        .provider(authAttributes.getProvider())
        .externalId(authAttributes.getExternalId())
        .build();
  }

  public boolean hasDifferentProviderWithEmail(String email, String externalId) {
    return Objects.equals(this.email, email) && !Objects.equals(this.externalId, externalId);
  }
}
