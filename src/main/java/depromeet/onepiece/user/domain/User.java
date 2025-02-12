package depromeet.onepiece.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@CompoundIndexes(
    value = @CompoundIndex(unique = true, name = "email_1_provider_1", def = "email_1_provider_1"))
@Getter
public class User {
  @MongoId private ObjectId id;

  @Field("name")
  private String name;

  @Field("email")
  private String email;

  @Field("provider")
  private OAuthProviderType provider;
}
