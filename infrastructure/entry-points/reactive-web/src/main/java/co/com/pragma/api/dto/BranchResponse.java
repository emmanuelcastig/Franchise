package co.com.pragma.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponse {
    private String name;
    private List<ProductResponse> products;
}