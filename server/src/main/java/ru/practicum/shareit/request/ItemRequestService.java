package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, CreateItemRequestDto createDto);

    List<ItemRequestDto> findByRequesterId(Long userId);

    List<ItemRequestDto> findAllOther(Long userId, int from, int size);

    ItemRequestDto findById(Long userId, Long requestId);
}
