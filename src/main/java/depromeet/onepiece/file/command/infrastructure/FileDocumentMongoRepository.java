package depromeet.onepiece.file.command.infrastructure;

import depromeet.onepiece.file.domain.FileDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileDocumentMongoRepository extends MongoRepository<FileDocument, ObjectId> {}
