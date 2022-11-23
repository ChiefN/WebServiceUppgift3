package me.code.uppgift3projekt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.code.uppgift3projekt.data.Post;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class AdminPostDto {

    private String title;
    private String content;
    private String creator;
    private String deletePost;
    private String editPost;

    public static List<AdminPostDto> listOf(Collection<Post> originalPostsCollection){
        return originalPostsCollection.stream().map(AdminPostDto::postToDto).toList();
    }

    public static AdminPostDto postToDto(Post post){
        return new AdminPostDto(post.getTitle(), post.getContent(), post.getCreator().getUsername(), "http://localhost:8080/admin/delete?postId=" + post.getTitle(), "http://localhost:8080/admin/update?postId=" + post.getTitle());
    }

}
