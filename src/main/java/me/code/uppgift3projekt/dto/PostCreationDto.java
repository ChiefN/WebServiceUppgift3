package me.code.uppgift3projekt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.code.uppgift3projekt.data.Post;
import me.code.uppgift3projekt.data.User;

@AllArgsConstructor
@Getter
@Setter
public class PostCreationDto {

    String title;
    String content;

}
