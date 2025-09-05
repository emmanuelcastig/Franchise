package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.*;
import co.com.pragma.model.branch.Branch;
import co.com.pragma.model.franchise.Franchise;
import co.com.pragma.model.product.Product;
import co.com.pragma.usecase.franchise.FranchiseUseCase;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FranchiseMapper {


    Franchise toEntity(FranchiseRequest request);
    FranchiseResponse toResponse(Franchise franchise);

    List<FranchiseResponse> toFranchiseResponseList(List<Franchise> franchises);


    Branch toEntity(BranchRequest request);
    BranchResponse toResponse(Branch branch);

    List<BranchResponse> toBranchResponseList(List<Branch> branches);


    Product toEntity(ProductRequest request);
    ProductResponse toResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> products);


    default MaxStockResponse toMaxStockResponse(FranchiseUseCase.ProductWithBranch productWithBranch) {
        if (productWithBranch == null) {
            return null;
        }

        ProductResponse productResponse = new ProductResponse();
        productResponse.setName(productWithBranch.productName());
        productResponse.setStock(productWithBranch.stock());

        MaxStockResponse maxStockResponse = new MaxStockResponse();
        maxStockResponse.setBranchName(productWithBranch.branchName());
        maxStockResponse.setProductWithMaxStock(productResponse);

        return maxStockResponse;
    }
}