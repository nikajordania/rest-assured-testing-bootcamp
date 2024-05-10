package lecture_4.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Vehicle.ElectricVehicle.class, name = "ELECTRIC_VEHICLE"),
        @JsonSubTypes.Type(value = Vehicle.FuelVehicle.class, name = "FUEL_VEHICLE")
})
@Data
public class Vehicle {

    public String type;

    @Data
    public static class ElectricVehicle extends Vehicle {
        String autonomy;
        String chargingTime;
    }

    @Data
    public static class FuelVehicle extends Vehicle {
        String fuelType;
        String transmissionType;
    }
}
