package reactor3.springwebflux.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    private String id;

    private String productCode;

    private String productName;

    private String description;

    private Float price;

}
