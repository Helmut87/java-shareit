package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                 @Valid @RequestBody CreateItemRequestDto createDto) {
        return requestService.create(userId, createDto);
    }

    @GetMapping
    public List<ItemRequestDto> findByRequesterId(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return requestService.findByRequesterId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllOther(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        return requestService.findAllOther(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                   @PathVariable @Positive Long requestId) {
        return requestService.findById(userId, requestId);
    }
}
