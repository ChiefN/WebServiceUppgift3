package me.code.uppgift3projekt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.code.uppgift3projekt.data.Post;
import me.code.uppgift3projekt.data.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostDto {

    private String title;
    private String content;
    private String creator;

    public static List<PostDto> listOf(Collection<Post> originalPostsCollection){
        return originalPostsCollection.stream().map(PostDto::postToDto).toList();
    }

    public static PostDto postToDto(Post post){
        return new PostDto(post.getTitle(), post.getContent(), post.getCreator().getUsername());
    }
}
