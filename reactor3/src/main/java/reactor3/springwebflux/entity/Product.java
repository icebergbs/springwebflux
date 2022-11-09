package reactor3.springwebflux.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collation = "product")
public class Product {

    @Id
    private String id;

    private String productCode;

    private String productName;

    private String description;

    private Float price;

}
