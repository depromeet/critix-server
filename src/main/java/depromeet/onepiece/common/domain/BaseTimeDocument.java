package depromeet.onepiece.common.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public abstract class BaseTimeDocument {
  @CreatedDate
  @Field("created_at")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Field("updated_at")
  private LocalDateTime updatedAt;

  @Field("deleted_at")
  private LocalDateTime deletedAt;

  public void delete() {
    this.deletedAt = LocalDateTime.now();
  }
}
