package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.BaseClient;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final BaseClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                         @Valid @RequestBody CreateItemRequestDto createDto) {
        return client.post("/requests", userId, createDto);
    }

    @GetMapping
    public ResponseEntity<Object> findByRequesterId(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return client.get("/requests", userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllOther(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        return client.get("/requests/all?from=" + from + "&size=" + size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                           @PathVariable @Positive Long requestId) {
        return client.get("/requests/" + requestId, userId);
    }
}
