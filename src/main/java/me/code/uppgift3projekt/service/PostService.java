package me.code.uppgift3projekt.service;

import me.code.uppgift3projekt.data.Post;
import me.code.uppgift3projekt.data.User;
import me.code.uppgift3projekt.exception.NotOwnerException;
import me.code.uppgift3projekt.exception.PostAlreadyExistsException;
import me.code.uppgift3projekt.exception.PostDoesNotExistException;
import me.code.uppgift3projekt.repository.PostRepository;
import me.code.uppgift3projekt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class PostService {

    private final PostRepository repository;

    @Autowired
    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Post create(User user, String title, String content)
            throws PostAlreadyExistsException
    {
        var existing = repository.getByTitle(title);
        if (existing.isPresent())
            throw new PostAlreadyExistsException();

        var post = new Post(title, content, user);
        repository.save(post);

        return post;
    }

    public Post delete(User user, String title)
            throws PostDoesNotExistException, NotOwnerException
    {
        var post = repository
                .getByTitle(title)
                .orElseThrow(PostDoesNotExistException::new);

        if (!post.getCreator().equals(user))
            throw new NotOwnerException();

        repository.delete(post);

        return post;
    }

    public Post edit(User user, String title, String updatedContent)
            throws PostDoesNotExistException, NotOwnerException
    {
        var post = repository
                .getByTitle(title)
                .orElseThrow(PostDoesNotExistException::new);

        if (!post.getCreator().equals(user))
            throw new NotOwnerException();

        post.setContent(updatedContent);
        repository.save(post);

        return post;
    }

    public Collection<Post> getAll() {
        return repository.getAll();
    }
}
