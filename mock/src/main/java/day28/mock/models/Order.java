package day28.mock.models;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @NotEmpty(message = "Name is required")
    private String name;
    @Email(message = "Please enter a valid email")
    private String email;
    @NotEmpty(message = "Please enter a delivery date")
    private String deliveryDate;
    @Size(min = 1, message = "Please add at least one item before checking out")
    private List<LineItem> lineItems;
    
}
