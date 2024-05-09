package lecture_3.models.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookingResponseItem {

    @JsonProperty("bookingid")
    private Integer bookingid;

    public Integer getBookingid() {
        return bookingid;
    }
}