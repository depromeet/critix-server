package depromeet.onepiece.file.query.infrastructure;

import depromeet.onepiece.file.domain.FileDocument;
import java.util.List;
import org.bson.types.ObjectId;

public interface FileQueryRepository {
  List<FileDocument> findAllByIds(List<ObjectId> idList);
}
