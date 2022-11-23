package me.code.uppgift3projekt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.code.uppgift3projekt.data.Post;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostExcerptDto {
    private String title;
    private String creator;
    private String linkToPost;

    public static List<PostExcerptDto> listOf(Collection<Post> originalPostsCollection){
        return originalPostsCollection.stream().map(PostExcerptDto::postToDto).toList();
    }

    public static PostExcerptDto postToDto(Post post){
        return new PostExcerptDto(post.getTitle(), post.getCreator().getUsername(), "http://localhost:8080/post?postId=" + post.getTitle());
    }

}
