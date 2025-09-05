package co.com.pragma.model.branch;
import co.com.pragma.model.product.Product;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Branch {
    private String id;
    private String name;
    private List<Product> products;
}
