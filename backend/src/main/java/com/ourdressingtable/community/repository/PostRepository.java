package com.ourdressingtable.community.repository;

import com.ourdressingtable.community.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
