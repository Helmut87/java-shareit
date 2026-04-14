package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentDto {
    @NotBlank(message = "Текст комментария не может быть пустым")
    private String text;
}
