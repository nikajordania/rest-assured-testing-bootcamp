package lecture_3.models.reqres.User;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponse {

    @JsonProperty("data")
    private Data data;

    @JsonProperty("support")
    private Support support;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setSupport(Support support) {
        this.support = support;
    }

    public Support getSupport() {
        return support;
    }

    @Override
    public String toString() {
        return
                "UserResponse{" +
                        "data = '" + data + '\'' +
                        ",support = '" + support + '\'' +
                        "}";
    }
}