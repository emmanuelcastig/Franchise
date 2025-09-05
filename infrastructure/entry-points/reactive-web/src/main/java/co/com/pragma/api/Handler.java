package co.com.pragma.api;
import co.com.pragma.api.dto.*;
import co.com.pragma.api.mapper.FranchiseMapper;
import co.com.pragma.usecase.franchise.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final FranchiseUseCase franchiseUseCase;
    private final FranchiseMapper franchiseMapper;

    // 2. Crear franquicia
    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseRequest.class)
                .map(franchiseMapper::toEntity)                // DTO -> Domain
                .flatMap(franchiseUseCase::createFranchise)    // UseCase
                .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED).bodyValue(dto));
    }

    // 3. Agregar sucursal
    public Mono<ServerResponse> addBranch(ServerRequest request) {
        String franchiseId = request.pathVariable("id");
        return request.bodyToMono(BranchRequest.class)
                .map(franchiseMapper::toEntity)
                .flatMap(branch -> franchiseUseCase.addBranch(franchiseId, branch))
                .map(franchiseMapper::toResponse)
                .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED).build());
    }

    // 4. Agregar producto
    public Mono<ServerResponse> addProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("id");
        String branchName = request.pathVariable("branch");

        return request.bodyToMono(ProductRequest.class)
                .map(franchiseMapper::toEntity)
                .flatMap(product -> franchiseUseCase.addProduct(franchiseId, branchName, product))
                .map(franchiseMapper::toResponse)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto));
    }

    // 5. Eliminar producto
    public Mono<ServerResponse> removeProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("id");
        String branchName = request.pathVariable("branch");
        String productName = request.pathVariable("product");

        return franchiseUseCase.removeProduct(franchiseId, branchName, productName)
                .map(franchiseMapper::toResponse)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto));
    }

    // 6. Actualizar stock
    public Mono<ServerResponse> updateStock(ServerRequest request) {
        String franchiseId = request.pathVariable("id");
        String branchName = request.pathVariable("branch");
        String productName = request.pathVariable("product");

        return request.bodyToMono(UpdateStockRequest.class)
                .flatMap(stockReq -> franchiseUseCase.updateStock(franchiseId, branchName,
                        productName, stockReq.getStock()))
                .map(franchiseMapper::toResponse)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto));
    }

    // 7. Obtener max stock por sucursal
    public Mono<ServerResponse> getMaxStockPerBranch(ServerRequest request) {
        String franchiseId = request.pathVariable("id");

        return franchiseUseCase.getMaxStockPerBranch(franchiseId)
                .map(list -> list.stream()
                        .map(franchiseMapper::toMaxStockResponse)
                        .toList()
                )
                .flatMap(dtoList -> ServerResponse.ok().bodyValue(dtoList));
    }
}