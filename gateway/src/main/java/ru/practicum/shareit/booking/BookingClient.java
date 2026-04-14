package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient {
    private static final String API_PREFIX = "/bookings";
    private final BaseClient baseClient;

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.baseClient = new BaseClient(serverUrl + API_PREFIX, builder.build());
    }

    public ResponseEntity<Object> create(Long userId, BookingDto bookingDto) {
        return baseClient.post("", userId, bookingDto);
    }

    public ResponseEntity<Object> approve(Long userId, Long bookingId, Boolean approved) {
        return baseClient.patch("/" + bookingId + "?approved=" + approved, userId, null);
    }

    public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
        return baseClient.get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingsByUser(Long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return baseClient.get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsByOwner(Long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return baseClient.get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }
}
