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
//    private final PostService postService;

    @Autowired
    public CommentController(CommentService commentService, AccountService accountService) {
        this.commentService = commentService;
        this.accountService = accountService;
    }

    @GetMapping("/comments/{id}")
    public String getComment(@PathVariable Long id, Model model) {

        // find comment by id
        Optional<Comment> optionalComment = this.commentService.getById(id);

        // if comment exists put it in model
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            model.addAttribute("comment", comment);
            return "comment";
        } else {
            return "404";
        }
    }



    @GetMapping("/comments/new")
    @PreAuthorize("isAuthenticated()")
    public String createNewPost(Model model, Principal principal) {

        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Optional<Account> optionalAccount = accountService.findOneByEmail(authUsername);
        if (optionalAccount.isPresent()) {
            Comment comment = new Comment();
            comment.setAccount(optionalAccount.get());
            model.addAttribute("comment", comment);
            return "comment_new";
        } else {
            return "redirect:/";
        }
    }


    @PostMapping("/comments/new")
    @PreAuthorize("isAuthenticated()")
    public String createNewComment(@ModelAttribute Comment comment, Principal principal) {
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }
        if (comment.getAccount().getEmail().compareToIgnoreCase(authUsername) < 0) {
            // TODO: some kind of error?
            // our account email on the Comment not equal to current logged in account!
        }
        commentService.save(comment);
//        return "redirect:/posts/" + comment.getPost().getId();
        return "redirect:/";
    }

    @GetMapping("/comments/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getCommentForEdit(@PathVariable Long id, Model model) {

        // find comment by id
        Optional<Comment> optionalComment = commentService.getById(id);
        // if comment exist put it in model
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            model.addAttribute("comment", comment);
            return "comment_edit";
        } else {
            return "404";
        }
    }

    @PostMapping("/comments/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updateComment(@PathVariable Long id, Comment comment, BindingResult result, Model model) {

        Optional<Comment> optionalComment = commentService.getById(id);
        if (optionalComment.isPresent()) {
            Comment existingComment = optionalComment.get();

            existingComment.setBody(comment.getBody());

            commentService.save(existingComment);
        }

        return "redirect:/comments/" + comment.getId();
    }

    @GetMapping("/comments/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteComment(@PathVariable Long id) {

        // find comment by id
        Optional<Comment> optionalComment = commentService.getById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();

            commentService.delete(comment);
            return "redirect:/";
        } else {
            return "404";
        }
    }

}
