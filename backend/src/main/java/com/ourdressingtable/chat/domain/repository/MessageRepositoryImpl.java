package com.ourdressingtable.chat.domain.repository;

import com.ourdressingtable.chat.domain.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;


@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public long countUnreadMessages(String chatroomId, String memberId, LocalDateTime lastReadAt) {
        Criteria criteria = Criteria.where("chatroomId").is(chatroomId)
                .and("senderId").ne(memberId)
                .and("lastReadAt").gt(lastReadAt);

        Query query = new Query(criteria);
        return mongoTemplate.count(query, Message.class, "messages");
    }


    @Override
    public List<Message> findRecentMessages(String chatroomId, int size) {
        Criteria criteria = Criteria.where("chatroomId").is(chatroomId);
        Query query = new Query(criteria)
                .with(Sort.by(Sort.Direction.DESC, "lastReadAt"))
                .limit(size);
        return mongoTemplate.find(query, Message.class, "messages");
    }
}
