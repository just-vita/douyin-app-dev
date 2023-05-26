package top.vita.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.vita.mo.MessageMO;

import java.util.List;

/**
 * @Author vita
 * @Date 2023/5/26 22:10
 */
@Repository
public interface MessageRepository extends MongoRepository<MessageMO, String> {
    List<MessageMO> findAllByToUserIdOrderByCreateTimeDesc(String userId, Pageable pageable);
}
