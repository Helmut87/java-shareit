package booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItServer.class)
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2024, 1, 1, 12, 0, 0));
        bookingDto.setItemId(1L);
        bookingDto.setBookerId(2L);
        bookingDto.setStatus(BookingStatus.WAITING);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).hasJsonPathNumberValue("@.id");
        assertThat(result).hasJsonPathStringValue("@.start");
        assertThat(result).hasJsonPathStringValue("@.end");
        assertThat(result).hasJsonPathNumberValue("@.itemId");
        assertThat(result).hasJsonPathNumberValue("@.bookerId");
        assertThat(result).hasJsonPathStringValue("@.status");

        assertThat(result).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("@.status").isEqualTo("WAITING");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"start\":\"2024-01-01T10:00:00\",\"end\":\"2024-01-01T12:00:00\",\"itemId\":1,\"bookerId\":2,\"status\":\"WAITING\"}";

        BookingDto result = json.parse(content).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStart()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
        assertThat(result.getEnd()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0, 0));
        assertThat(result.getItemId()).isEqualTo(1L);
        assertThat(result.getBookerId()).isEqualTo(2L);
        assertThat(result.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void testSerializeWithNullFields() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(null);
        bookingDto.setEnd(null);
        bookingDto.setItemId(null);
        bookingDto.setBookerId(null);
        bookingDto.setStatus(null);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).hasJsonPathNumberValue("@.id");
        assertThat(result).extractingJsonPathNumberValue("@.id").isEqualTo(1);
    }

    @Test
    void testSerializeWithPartialNullFields() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
        bookingDto.setEnd(null);
        bookingDto.setItemId(1L);
        bookingDto.setBookerId(null);
        bookingDto.setStatus(BookingStatus.WAITING);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).hasJsonPathNumberValue("@.id");
        assertThat(result).hasJsonPathStringValue("@.start");
        assertThat(result).hasJsonPathNumberValue("@.itemId");
        assertThat(result).hasJsonPathStringValue("@.status");

        assertThat(result).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("@.status").isEqualTo("WAITING");
    }

    @Test
    void testDeserializeWithMissingFields() throws Exception {
        String content = "{\"id\":1,\"start\":\"2024-01-01T10:00:00\",\"end\":\"2024-01-01T12:00:00\",\"itemId\":1}";

        BookingDto result = json.parse(content).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStart()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
        assertThat(result.getEnd()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0, 0));
        assertThat(result.getItemId()).isEqualTo(1L);
        assertThat(result.getBookerId()).isNull();
        assertThat(result.getStatus()).isNull();
    }

    @Test
    void testSerializeWithDifferentStatuses() throws Exception {

        BookingDto approvedBooking = new BookingDto();
        approvedBooking.setId(1L);
        approvedBooking.setStatus(BookingStatus.APPROVED);

        JsonContent<BookingDto> approvedResult = json.write(approvedBooking);
        assertThat(approvedResult).extractingJsonPathStringValue("@.status").isEqualTo("APPROVED");

        BookingDto rejectedBooking = new BookingDto();
        rejectedBooking.setId(2L);
        rejectedBooking.setStatus(BookingStatus.REJECTED);

        JsonContent<BookingDto> rejectedResult = json.write(rejectedBooking);
        assertThat(rejectedResult).extractingJsonPathStringValue("@.status").isEqualTo("REJECTED");

        BookingDto cancelledBooking = new BookingDto();
        cancelledBooking.setId(3L);
        cancelledBooking.setStatus(BookingStatus.CANCELLED);

        JsonContent<BookingDto> cancelledResult = json.write(cancelledBooking);
        assertThat(cancelledResult).extractingJsonPathStringValue("@.status").isEqualTo("CANCELLED");
    }

    @Test
    void testDeserializeWithDifferentStatuses() throws Exception {
        String approvedContent = "{\"id\":1,\"status\":\"APPROVED\"}";
        BookingDto approvedResult = json.parse(approvedContent).getObject();
        assertThat(approvedResult.getStatus()).isEqualTo(BookingStatus.APPROVED);

        String rejectedContent = "{\"id\":2,\"status\":\"REJECTED\"}";
        BookingDto rejectedResult = json.parse(rejectedContent).getObject();
        assertThat(rejectedResult.getStatus()).isEqualTo(BookingStatus.REJECTED);

        String cancelledContent = "{\"id\":3,\"status\":\"CANCELLED\"}";
        BookingDto cancelledResult = json.parse(cancelledContent).getObject();
        assertThat(cancelledResult.getStatus()).isEqualTo(BookingStatus.CANCELLED);

        String waitingContent = "{\"id\":4,\"status\":\"WAITING\"}";
        BookingDto waitingResult = json.parse(waitingContent).getObject();
        assertThat(waitingResult.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void testSerializeWithTimePrecision() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.of(2024, 1, 1, 10, 30, 45));
        bookingDto.setEnd(LocalDateTime.of(2024, 1, 1, 12, 30, 45));

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathStringValue("@.start").isEqualTo("2024-01-01T10:30:45");
        assertThat(result).extractingJsonPathStringValue("@.end").isEqualTo("2024-01-01T12:30:45");
    }

    @Test
    void testDeserializeWithTimePrecision() throws Exception {
        String content = "{\"start\":\"2024-01-01T10:30:45\",\"end\":\"2024-01-01T12:30:45\"}";

        BookingDto result = json.parse(content).getObject();

        assertThat(result.getStart()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 30, 45));
        assertThat(result.getEnd()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 30, 45));
    }

    @Test
    void testJsonIgnoreUnknownFields() throws Exception {
        String content = "{\"id\":1,\"start\":\"2024-01-01T10:00:00\",\"end\":\"2024-01-01T12:00:00\",\"itemId\":1,\"bookerId\":2,\"status\":\"WAITING\",\"extraField\":\"ignored\"}";

        BookingDto result = json.parse(content).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStart()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
        assertThat(result.getEnd()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0, 0));
        assertThat(result.getItemId()).isEqualTo(1L);
        assertThat(result.getBookerId()).isEqualTo(2L);
        assertThat(result.getStatus()).isEqualTo(BookingStatus.WAITING);
    }
}