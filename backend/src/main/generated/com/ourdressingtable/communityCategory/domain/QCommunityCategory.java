package com.ourdressingtable.communityCategory.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommunityCategory is a Querydsl query type for CommunityCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityCategory extends EntityPathBase<CommunityCategory> {

    private static final long serialVersionUID = -468978674L;

    public static final QCommunityCategory communityCategory = new QCommunityCategory("communityCategory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QCommunityCategory(String variable) {
        super(CommunityCategory.class, forVariable(variable));
    }

    public QCommunityCategory(Path<? extends CommunityCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommunityCategory(PathMetadata metadata) {
        super(CommunityCategory.class, metadata);
    }

}

