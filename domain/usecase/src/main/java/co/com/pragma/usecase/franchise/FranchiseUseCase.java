package co.com.pragma.usecase.franchise;

import co.com.pragma.model.branch.Branch;
import co.com.pragma.model.franchise.Franchise;
import co.com.pragma.model.franchise.gateways.FranchiseRepository;
import co.com.pragma.model.product.Product;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FranchiseUseCase {
    private final FranchiseRepository franchiseRepository;
    //Crear nueva franquicia
    public Mono<Franchise> createFranchise(Franchise franchise) {
        if (franchise == null) {
            return Mono.error(new IllegalArgumentException("Franchise cannot be null"));
        }
        return franchiseRepository.save(franchise);
    }

    //Agregar sucursal a una franquicia existente
    public Mono<Franchise> addBranch(String franchiseId, Branch newBranch) {
        if (newBranch == null) {
            return Mono.error(new IllegalArgumentException("Branch cannot be null"));
        }

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(franchise -> {
                    if (franchise.getBranches() == null) {
                        franchise.setBranches(new ArrayList<>());
                    }
                    franchise.getBranches().add(newBranch);
                    return franchiseRepository.save(franchise);
                });
    }

    //Agregar producto a una sucursal específica
    public Mono<Franchise> addProduct(String franchiseId, String branchName, Product newProduct) {
        if (newProduct == null) {
            return Mono.error(new IllegalArgumentException("Product cannot be null"));
        }

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElse(null);

                    if (branch == null) {
                        return Mono.error(new IllegalArgumentException("Branch not found"));
                    }

                    if (branch.getProducts() == null) {
                        branch.setProducts(new java.util.ArrayList<>());
                    }

                    branch.getProducts().add(newProduct);
                    return franchiseRepository.save(franchise);
                });
    }

    //Eliminar un producto de una sucursal
    public Mono<Franchise> removeProduct(String franchiseId, String branchName, String productName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElse(null);

                    if (branch == null) {
                        return Mono.error(new IllegalArgumentException("Branch not found"));
                    }

                    if (branch.getProducts() != null) {
                        branch.setProducts(branch.getProducts().stream()
                                .filter(p -> !p.getName().equals(productName))
                                .collect(Collectors.toList()));
                    }

                    return franchiseRepository.save(franchise);
                });
    }

    //Modificar el stock de un producto
    public Mono<Franchise> updateStock(String franchiseId, String branchName, String productName, int newStock) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElse(null);

                    if (branch == null) {
                        return Mono.error(new IllegalArgumentException("Branch not found"));
                    }

                    Product product = branch.getProducts().stream()
                            .filter(p -> p.getName().equals(productName))
                            .findFirst()
                            .orElse(null);

                    if (product == null) {
                        return Mono.error(new IllegalArgumentException("Product not found"));
                    }

                    product.setStock(newStock);
                    return franchiseRepository.save(franchise);
                });
    }

    //Devolver producto con mayor stock por cada sucursal
    public Mono<List<ProductWithBranch>> getMaxStockPerBranch(String franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .map(franchise -> franchise.getBranches().stream()
                        .map(branch -> branch.getProducts().stream()
                                .max(Comparator.comparingInt(Product::getStock))
                                .map(product -> new ProductWithBranch(branch.getName(),
                                        product.getName(), product.getStock()))
                                .orElse(null)
                        )
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
                );
    }

    // DTO de respuesta para el caso de uso getMaxStockPerBranch
    public record ProductWithBranch(String branchName, String productName, int stock) {}
}
