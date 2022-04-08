package carsharing;

import lombok.Data;

@Data
public class Customer {
    private Integer id;
    private String name;
    private Integer rentedCarId;
}
