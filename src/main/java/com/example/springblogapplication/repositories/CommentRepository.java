package com.example.springblogapplication.repositories;

import com.example.springblogapplication.models.Comment;
import com.example.springblogapplication.models.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post, Sort sort);

}

