package depromeet.onepiece.file.domain;

import static lombok.AccessLevel.PROTECTED;
import static org.springframework.data.mongodb.core.mapping.Field.Write.NON_NULL;

import depromeet.onepiece.common.domain.BaseTimeDocument;
import depromeet.onepiece.common.utils.EncryptionUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Document
@NoArgsConstructor(access = PROTECTED)
public class FileDocument extends BaseTimeDocument {
  @MongoId private ObjectId id;

  @Field(value = "logical_name", write = NON_NULL)
  private String logicalName;

  @Field(value = "physical_path")
  private String physicalPath;

  @Field(value = "file_type", write = NON_NULL)
  private FileType fileType;

  @Builder
  public static FileDocument create(String logicalName, FileType fileType) {
    return FileDocument.builder()
        .logicalName(EncryptionUtil.encrypt(logicalName))
        .fileType(fileType)
        .build();
  }

  public FileDocument setPhysicalPath(String physicalPath) {
    this.physicalPath = EncryptionUtil.encrypt(physicalPath);
    return this;
  }
}
