package co.com.pragma.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchRequest {
    @NotBlank(message = "The name of the branch is mandatory")
    private String name;
    private List<ProductRequest> products;
}



