package com.ourdressingtable.community.comment.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.common.util.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
@Where(clause = "is_deleted = false")
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    private Comment parent;

    @Column(name="is_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    @Builder
    public Comment(Long id, String content, int depth, Member member, Post post, Comment parent, boolean isDeleted) {
        this.id = id;
        this.content = content;
        this.depth = depth;
        this.member = member;
        this.post = post;
        this.parent = parent;
        this.isDeleted = isDeleted;
        this.deletedAt = null;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }
}
