package com.example.springblogapplication.services;

import com.example.springblogapplication.models.Comment;
import com.example.springblogapplication.models.Post;
import com.example.springblogapplication.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            comment.setCreatedAt(LocalDateTime.now());
        }
        return commentRepository.save(comment);
    }

    public List<Comment> getByPost(Post post) {
        return commentRepository.findByPost(post);
    }


}
