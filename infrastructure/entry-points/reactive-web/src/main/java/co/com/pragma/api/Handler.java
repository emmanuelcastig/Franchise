package co.com.pragma.api;
import co.com.pragma.api.dto.*;
import co.com.pragma.api.mapper.FranchiseMapper;
import co.com.pragma.usecase.franchise.FranchiseUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {
    private final FranchiseUseCase franchiseUseCase;
    private final FranchiseMapper franchiseMapper;
    private final Validator validator;

    // Crear franquicia

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseRequest.class)
                .flatMap(this::validateRequest)
                .map(franchiseMapper::toEntity)                // DTO -> Domain
                .flatMap(franchiseUseCase::createFranchise)    // UseCase
                .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED).bodyValue(dto));
    }
    // Agregar sucursal

    public Mono<ServerResponse> addBranch(ServerRequest request) {
        String franchiseId = request.pathVariable("id");
        return request.bodyToMono(BranchRequest.class)
                .flatMap(this::validateRequest)
                .map(franchiseMapper::toEntity)
                .flatMap(branch -> franchiseUseCase.addBranch(franchiseId, branch))
                .map(franchiseMapper::toResponse)
                .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED).bodyValue(dto));
    }
    // Agregar producto

    public Mono<ServerResponse> addProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("id");
        String branchName = request.pathVariable("branch");

        return request.bodyToMono(ProductRequest.class)
                .flatMap(this::validateRequest)
                .map(franchiseMapper::toEntity)
                .flatMap(product -> franchiseUseCase.addProduct(franchiseId, branchName, product))
                .map(franchiseMapper::toResponse)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto));
    }
    // Eliminar producto

    public Mono<ServerResponse> removeProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("id");
        String branchName = request.pathVariable("branch");
        String productName = request.pathVariable("product");

        return franchiseUseCase.removeProduct(franchiseId, branchName, productName)
                .map(franchiseMapper::toResponse)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto));
    }
    // Actualizar stock

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        String franchiseId = request.pathVariable("id");
        String branchName = request.pathVariable("branch");
        String productName = request.pathVariable("product");

        return request.bodyToMono(UpdateStockRequest.class)
                .flatMap(this::validateRequest)
                .flatMap(stockReq -> franchiseUseCase.updateStock(franchiseId, branchName,
                        productName, stockReq.getStock()))
                .map(franchiseMapper::toResponse)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto));
    }
    // Obtener max stock por sucursal

    public Mono<ServerResponse> getMaxStockPerBranch(ServerRequest request) {
        String franchiseId = request.pathVariable("id");
        return ServerResponse.ok()
                .body(
                        franchiseUseCase.getMaxStockPerBranch(franchiseId)
                                .map(franchiseMapper::toMaxStockResponse),
                        ProductWithBranchResponse.class
                );
    }
    // Actualizar nombre de la franquicia

    public Mono<ServerResponse> updateFranchiseName(ServerRequest request) {
        String franchiseId = request.pathVariable("id");
        return request.bodyToMono(UpdateNameRequest.class)
                .flatMap(this::validateRequest)
                .flatMap(req -> franchiseUseCase.updateFranchiseName(franchiseId, req.getName()))
                .map(franchiseMapper::toResponse)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto));
    }
    // Actualizar nombre de una sucursal

    public Mono<ServerResponse> updateBranchName(ServerRequest request) {
        String franchiseId = request.pathVariable("id");
        String oldBranchName = request.pathVariable("branch");
        return request.bodyToMono(UpdateNameRequest.class)
                .flatMap(this::validateRequest)
                .flatMap(req -> franchiseUseCase.updateBranchName(franchiseId, oldBranchName,
                        req.getName()))
                .map(franchiseMapper::toResponse)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto));
    }
    // Actualizar nombre de un producto

    public Mono<ServerResponse> updateProductName(ServerRequest request) {
        String franchiseId = request.pathVariable("id");
        String branchName = request.pathVariable("branch");
        String oldProductName = request.pathVariable("product");

        return request.bodyToMono(UpdateNameRequest.class)
                .flatMap(this::validateRequest)
                .flatMap(req -> franchiseUseCase.updateProductName(franchiseId, branchName,
                        oldProductName, req.getName()))
                .map(franchiseMapper::toResponse)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto));
    }

    public <T> Mono<T> validateRequest(T request) {
        Set<ConstraintViolation<T>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
            return Mono.error(new ValidationException(errorMessage));
        }
        return Mono.just(request);
    }
}