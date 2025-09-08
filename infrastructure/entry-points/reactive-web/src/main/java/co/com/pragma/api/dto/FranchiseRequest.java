package co.com.pragma.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FranchiseRequest {
    @NotBlank(message = "The name of the franchise is mandatory")
    private String name;
    private List<BranchRequest> branches;
}


