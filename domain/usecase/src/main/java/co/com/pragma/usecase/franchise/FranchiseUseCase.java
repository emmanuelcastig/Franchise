package co.com.pragma.usecase.franchise;

import co.com.pragma.model.branch.Branch;
import co.com.pragma.model.franchise.Franchise;
import co.com.pragma.model.franchise.gateways.FranchiseRepository;
import co.com.pragma.model.product.Product;
import co.com.pragma.usecase.franchise.enums.TechnicalMessage;
import co.com.pragma.usecase.franchise.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FranchiseUseCase {
    private final FranchiseRepository franchiseRepository;

    // Crear nueva franquicia
    public Mono<Franchise> createFranchise(Franchise franchise) {
        return Mono.justOrEmpty(franchise)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchiseRepository::save);
    }

    // Agregar sucursal
    public Mono<Franchise> addBranch(String franchiseId, Branch newBranch) {
        return Mono.justOrEmpty(newBranch)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.INVALID_REQUEST)))
                .then(franchiseRepository.findById(franchiseId))
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .map(franchise -> {
                    var branches = new ArrayList<>(franchise.getBranches() != null ? franchise.getBranches() : new ArrayList<>());
                    branches.add(newBranch);
                    franchise.setBranches(branches);
                    return franchise;
                })
                .flatMap(franchiseRepository::save);
    }

    // Agregar producto
    public Mono<Franchise> addProduct(String franchiseId, String branchName, Product newProduct) {
        return Mono.justOrEmpty(newProduct)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.INVALID_REQUEST)))
                .then(franchiseRepository.findById(franchiseId))
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise ->
                        Mono.justOrEmpty(franchise.getBranches().stream()
                                        .filter(b -> b.getName().equals(branchName))
                                        .findFirst())
                                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)))
                                .map(branch -> {
                                    var products = new ArrayList<>(branch.getProducts() != null ? branch.getProducts() : new ArrayList<>());
                                    products.add(newProduct);
                                    branch.setProducts(products);
                                    return franchise;
                                })
                )
                .flatMap(franchiseRepository::save);
    }

    // Eliminar producto
    public Mono<Franchise> removeProduct(String franchiseId, String branchName, String productName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise ->
                        Mono.justOrEmpty(franchise.getBranches().stream()
                                        .filter(b -> b.getName().equals(branchName))
                                        .findFirst())
                                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)))
                                .map(branch -> {
                                    var products = Mono.justOrEmpty(branch.getProducts())
                                            .defaultIfEmpty(new ArrayList<>())
                                            .block();
                                    branch.setProducts(products.stream()
                                            .filter(p -> !p.getName().equals(productName))
                                            .collect(Collectors.toList()));
                                    return franchise;
                                })
                )
                .flatMap(franchiseRepository::save);
    }

    // Actualizar stock
    public Mono<Franchise> updateStock(String franchiseId, String branchName, String productName, int newStock) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise ->
                        Mono.justOrEmpty(franchise.getBranches().stream()
                                        .filter(b -> b.getName().equals(branchName))
                                        .findFirst())
                                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)))
                                .flatMap(branch ->
                                        Mono.justOrEmpty(branch.getProducts().stream()
                                                        .filter(p -> p.getName().equals(productName))
                                                        .findFirst())
                                                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                                                .map(product -> {
                                                    product.setStock(newStock);
                                                    return franchise;
                                                })
                                )
                )
                .flatMap(franchiseRepository::save);
    }

    // Producto con mayor stock por sucursal
    public Flux<ProductWithBranch> getMaxStockPerBranch(String franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()))
                .flatMap(branch ->
                        Mono.justOrEmpty(branch.getProducts().stream()
                                        .max(Comparator.comparingInt(Product::getStock)))
                                .map(product -> new ProductWithBranch(branch.getName(),
                                        product.getName(), product.getStock()))
                );
    }

    public record ProductWithBranch(String branchName, String productName, int stock) {}

    // Update franchise name
    public Mono<Franchise> updateFranchiseName(String franchiseId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .map(franchise -> {
                    franchise.setName(newName);
                    return franchise;
                })
                .flatMap(franchiseRepository::save);
    }

    // Update branch name
    public Mono<Franchise> updateBranchName(String franchiseId, String oldBranchName, String newBranchName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise ->
                        Mono.justOrEmpty(franchise.getBranches().stream()
                                        .filter(b -> b.getName().equals(oldBranchName))
                                        .findFirst())
                                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)))
                                .map(branch -> {
                                    branch.setName(newBranchName);
                                    return franchise;
                                })
                )
                .flatMap(franchiseRepository::save);
    }

    // Update product name
    public Mono<Franchise> updateProductName(String franchiseId, String branchName, String oldProductName, String newProductName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise ->
                        Mono.justOrEmpty(franchise.getBranches().stream()
                                        .filter(b -> b.getName().equals(branchName))
                                        .findFirst())
                                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)))
                                .flatMap(branch ->
                                        Mono.justOrEmpty(branch.getProducts().stream()
                                                        .filter(p -> p.getName().equals(oldProductName))
                                                        .findFirst())
                                                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                                                .map(product -> {
                                                    product.setName(newProductName);
                                                    return franchise;
                                                })
                                )
                )
                .flatMap(franchiseRepository::save);
    }
}
