package lecture_4.models.petstore.lombok_fluent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true, fluent = true)
public class Category {

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private int id;
}
