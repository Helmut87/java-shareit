package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.BaseClient;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final BaseClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                         @Valid @RequestBody ItemDto itemDto) {
        return client.post("/items", userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                         @PathVariable @Positive Long itemId,
                                         @RequestBody ItemDto itemDto) {
        return client.patch("/items/" + itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                           @PathVariable @Positive Long itemId) {
        return client.get("/items/" + itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByOwnerId(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return client.get("/items", userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam @NotBlank(message = "Текст для поиска не может быть пустым") String text) {
        return client.get("/items/search?text=" + text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                             @PathVariable @Positive Long itemId,
                                             @Valid @RequestBody CreateCommentDto createCommentDto) {
        return client.post("/items/" + itemId + "/comment", userId, createCommentDto);
    }
}
