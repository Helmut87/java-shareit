package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateItemRequestDto {
    @NotBlank(message = "Описание запроса не может быть пустым")
    private String description;
}
