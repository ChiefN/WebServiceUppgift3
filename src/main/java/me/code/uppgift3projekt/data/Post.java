package me.code.uppgift3projekt.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Post {

    private String title;
    private String content;
    private User creator;

}
