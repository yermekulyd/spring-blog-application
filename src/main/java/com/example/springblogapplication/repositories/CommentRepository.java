package com.example.springblogapplication.repositories;

import com.example.springblogapplication.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
