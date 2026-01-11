package com.ourdressingtable.common.util;

import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class MongoCreatedAtEntity {

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;
}
