package depromeet.onepiece.file.query.infrastructure;

import depromeet.onepiece.file.domain.FileDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileQueryMongoRepository extends MongoRepository<FileDocument, ObjectId> {
  Iterable<ObjectId> id(ObjectId id);
}
