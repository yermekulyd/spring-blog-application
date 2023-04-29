package com.example.springblogapplication.controllers;

import com.example.springblogapplication.models.Account;
import com.example.springblogapplication.models.Comment;
import com.example.springblogapplication.models.Post;
import com.example.springblogapplication.services.AccountService;
import com.example.springblogapplication.services.CommentService;
import com.example.springblogapplication.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
public class CommentController {

    private final CommentService commentService;
    private final AccountService accountService;
    private final PostService postService;

    @Autowired
    public CommentController(CommentService commentService, AccountService accountService, PostService postService) {
        this.commentService = commentService;
        this.accountService = accountService;
        this.postService = postService;
    }

    @PostMapping("/posts/{id}/comments")
    @PreAuthorize("isAuthenticated()")
    public String createNewComment(@PathVariable Long id, @ModelAttribute Comment comment, Principal principal) {
        Optional<Post> optionalPost = postService.getById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            String authUsername = "anonymousUser";

            if (principal != null) {
                authUsername = principal.getName();
            }

            Optional<Account> optionalAccount = accountService.findOneByEmail(authUsername);

            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();

                comment.setPost(post);
                comment.setAccount(account);

                commentService.save(comment);
                System.out.println("Creating 1 comment");
            }
        }

        return "redirect:/posts/" + id;
    }

}
//    @GetMapping("/comments/{id}")
//    public String getComment(@PathVariable Long id, Model model) {
//
//        // find comment by id
//        Optional<Comment> optionalComment = this.commentService.getById(id);
//
//        // if comment exists put it in model
//        if (optionalComment.isPresent()) {
//            Comment comment = optionalComment.get();
//            model.addAttribute("comment", comment);
//            return "comment";
//        } else {
//            return "404";
//        }
//    }
//
//
//
//    @GetMapping("/comments/new")
//    @PreAuthorize("isAuthenticated()")
//    public String createNewPost(Model model, Principal principal) {
//
//        String authUsername = "anonymousUser";
//        if (principal != null) {
//            authUsername = principal.getName();
//        }
//
//        Optional<Account> optionalAccount = accountService.findOneByEmail(authUsername);
//        if (optionalAccount.isPresent()) {
//            Comment comment = new Comment();
//            comment.setAccount(optionalAccount.get());
//            model.addAttribute("comment", comment);
//            return "comment_new";
//        } else {
//            return "redirect:/";
//        }
//    }


//    @GetMapping("/comments/{id}/edit")
//    @PreAuthorize("isAuthenticated()")
//    public String getCommentForEdit(@PathVariable Long id, Model model) {
//
//        // find comment by id
//        Optional<Comment> optionalComment = commentService.getById(id);
//        // if comment exist put it in model
//        if (optionalComment.isPresent()) {
//            Comment comment = optionalComment.get();
//            model.addAttribute("comment", comment);
//            return "comment_edit";
//        } else {
//            return "404";
//        }
//    }

//    @PostMapping("/comments/{id}")
//    @PreAuthorize("isAuthenticated()")
//    public String updateComment(@PathVariable Long id, Comment comment, BindingResult result, Model model) {
//
//        Optional<Comment> optionalComment = commentService.getById(id);
//        if (optionalComment.isPresent()) {
//            Comment existingComment = optionalComment.get();
//
//            existingComment.setBody(comment.getBody());
//
//            commentService.save(existingComment);
//        }
//
//        return "redirect:/comments/" + comment.getId();
//    }

//    @GetMapping("/comments/{id}/delete")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public String deleteComment(@PathVariable Long id) {
//
//        // find comment by id
//        Optional<Comment> optionalComment = commentService.getById(id);
//        if (optionalComment.isPresent()) {
//            Comment comment = optionalComment.get();
//
//            commentService.delete(comment);
//            return "redirect:/";
//        } else {
//            return "404";
//        }
//    }


