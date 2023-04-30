package com.example.springblogapplication.controllers;

import com.example.springblogapplication.models.Account;
import com.example.springblogapplication.models.Comment;
import com.example.springblogapplication.models.Post;
import com.example.springblogapplication.repositories.PostFactory;
import com.example.springblogapplication.services.AccountService;
import com.example.springblogapplication.services.CommentService;
import com.example.springblogapplication.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {


    private final PostService postService;
    private final PostFactory postFactory;
    private final CommentService commentService;

    @Autowired
    public PostController(PostService postService, PostFactory postFactory, CommentService commentService) {
        this.postService = postService;
        this.postFactory = postFactory;
        this.commentService = commentService;
    }


    //Factory
    @GetMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public String createNewPost(Model model, Principal principal) {
        Post post = postFactory.createPost();
        model.addAttribute("post", post);
        return "post_new";
    }

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model,
                          @RequestParam(name = "sort", defaultValue = "createdAt") String sortField,
                          @RequestParam(name = "order", defaultValue = "desc") String sortOrder) {

        // find post by id
        Optional<Post> optionalPost = this.postService.getById(id);

        // if post exists put it in model
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            model.addAttribute("comment", new Comment());

            // get comments for post and sort them
            List<Comment> comments = this.commentService.getByPost(post, sortField, sortOrder);
            model.addAttribute("comments", comments);

            System.out.println("getPost called for post with id " + id);
            return "post";
        } else {
            return "404";
        }
    }



    @PostMapping("/posts/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long id, Post post, BindingResult result, Model model) {

        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());
            existingPost.setImageUrl(post.getImageUrl());

            postService.save(existingPost);
        }

        return "redirect:/posts/" + post.getId();
    }

//    @GetMapping("/posts/new")
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
//            Post post = new Post();
//            post.setAccount(optionalAccount.get());
//            model.addAttribute("post", post);
//            return "post_new";
//        } else {
//            return "redirect:/";
//        }
//    }

    @PostMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public String createNewPost(@ModelAttribute Post post, Principal principal, Model model) {
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }
        if (post.getAccount().getEmail().compareToIgnoreCase(authUsername) < 0) {
            // TODO: some kind of error?
            // our account email on the Post not equal to current logged in account!
        }
        postService.save(post);
        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit(@PathVariable Long id, Model model) {

        // find post by id
        Optional<Post> optionalPost = postService.getById(id);
        // if post exist put it in model
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_edit";
        } else {
            return "404";
        }
    }

    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deletePost(@PathVariable Long id) {

        // find post by id
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            postService.delete(post);
            return "redirect:/";
        } else {
            return "404";
        }
    }

}
