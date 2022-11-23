package me.code.uppgift3projekt.controller;

import me.code.uppgift3projekt.data.Post;
import me.code.uppgift3projekt.data.User;
import me.code.uppgift3projekt.dto.AdminPostDto;
import me.code.uppgift3projekt.dto.PostCreationDto;
import me.code.uppgift3projekt.dto.PostDto;
import me.code.uppgift3projekt.dto.PostExcerptDto;
import me.code.uppgift3projekt.exception.NotOwnerException;
import me.code.uppgift3projekt.exception.PostAlreadyExistsException;
import me.code.uppgift3projekt.exception.PostDoesNotExistException;
import me.code.uppgift3projekt.service.PostService;
import me.code.uppgift3projekt.service.TokenService;
import me.code.uppgift3projekt.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
public class PostController {
    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping ("/all")
    public List<PostDto> allPosts(){
        return PostDto.listOf(postService.getAll());
    }

    @GetMapping ("/alltitle")
    public List<PostExcerptDto> allPostsTitle(){
        return PostExcerptDto.listOf(postService.getAllTitle());
    }

    @GetMapping ("/all/{userId}")
    public List<PostDto> allPostsSpecific(@PathVariable String userId){
        return PostDto.listOf(postService.getAllSpecific(userId));
    }

    @GetMapping ("/alltitle/{userId}")
    public List<PostExcerptDto> allPostsTitleSpecific(@PathVariable String userId){
        return PostExcerptDto.listOf(postService.getAllTitleSpecific(userId));
    }

    @GetMapping("/post")
    public ResponseEntity<?> singlePost(@RequestParam String postId){
        try{
            return new ResponseEntity<>(PostDto.postToDto(postService.getPost(postId)), HttpStatus.NOT_FOUND);
        }catch (PostDoesNotExistException e){
            return new ResponseEntity<>("No post with title: " + postId + " was found.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> deletePost(@RequestParam String postId){
        User authenticatedUser;
        try{
            authenticatedUser = (User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }

        try{
            postService.delete(authenticatedUser, postId);
        }catch (PostDoesNotExistException e){
            return new ResponseEntity<>("Post does not exist", HttpStatus.NOT_FOUND);
        } catch (NotOwnerException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //https://auth0.com/blog/forbidden-unauthorized-http-status-codes/
        }
        return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/admin/update")
    public ResponseEntity<?> updatePost(@RequestParam String postId, @RequestBody String content){
        User authenticatedUser;
        try{
            authenticatedUser = (User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }

        if(content.isBlank()){
            return new ResponseEntity<>("Can not change the content to nothing. Please include new content in body as a string", HttpStatus.BAD_REQUEST);
        }

        Post updatedPost;
        try{
            updatedPost = postService.edit(authenticatedUser, postId, content);
        }catch (PostDoesNotExistException e){
            return new ResponseEntity<>("Post does not exist", HttpStatus.NOT_FOUND);
        } catch (NotOwnerException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //https://auth0.com/blog/forbidden-unauthorized-http-status-codes/
        }
        return new ResponseEntity<>(PostDto.postToDto(updatedPost), HttpStatus.OK);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> createPost(@RequestBody PostCreationDto post){
        User authenticatedUser;
        try{
            authenticatedUser = (User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }

        Post createdPost;
        try{
            createdPost = postService.create(authenticatedUser, post.getTitle(), post.getContent());
        }catch (PostAlreadyExistsException e){
            return new ResponseEntity<>("Post already exists", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(PostDto.postToDto(createdPost), HttpStatus.CREATED);
    }

    @GetMapping ("/admin/all")
    public ResponseEntity<?> adminPosts(){
        User authenticatedUser;
        try{
            authenticatedUser = (User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(AdminPostDto.listOf(postService.getAllTitleSpecific(authenticatedUser.getUsername())), HttpStatus.OK);
    }
}
