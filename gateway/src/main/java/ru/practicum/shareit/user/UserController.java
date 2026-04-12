package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.BaseClient;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final BaseClient client;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return client.get("/users");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable @Positive Long id) {
        return client.get("/users/" + id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        return client.post("/users", null, userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable @Positive Long id, @RequestBody UserDto userDto) {
        return client.patch("/users/" + id, null, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable @Positive Long id) {
        return client.delete("/users/" + id, null);
    }
}
