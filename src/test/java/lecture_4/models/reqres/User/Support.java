package lecture_4.models.reqres.User;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Support(
        @JsonProperty("text") String text,
        @JsonProperty("url") String url) {
}