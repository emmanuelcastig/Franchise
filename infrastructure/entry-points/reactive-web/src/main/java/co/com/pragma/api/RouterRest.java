package co.com.pragma.api;

import co.com.pragma.api.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name = "Franchise API", description = "Endpoints for managing franchises, branches, and products")
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/franchises",
                    produces = {"application/json"},
                    method = {RequestMethod.POST},
                    beanClass = Handler.class,
                    beanMethod = "createFranchise",
                    operation = @Operation(
                            operationId = "createFranchise",
                            summary = "Create a new franchise",
                            description = "Create a franchise with its name",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = FranchiseRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Franchise created",
                                            content = @Content(schema = @Schema(implementation = FranchiseResponse.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid input")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/franchises/{id}/branch",
                    produces = {"application/json"},
                    method = {RequestMethod.POST},
                    beanClass = Handler.class,
                    beanMethod = "addBranch",
                    operation = @Operation(
                            operationId = "addBranch",
                            summary = "Add a branch to a franchise",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id", description = "Franchise ID")
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = BranchRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Branch added",
                                            content = @Content(schema = @Schema(implementation = FranchiseResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Franchise not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/franchises/{id}/branch/{branch}/product",
                    produces = {"application/json"},
                    method = {RequestMethod.POST},
                    beanClass = Handler.class,
                    beanMethod = "addProduct",
                    operation = @Operation(
                            operationId = "addProduct",
                            summary = "Add a product to a branch",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id"),
                                    @Parameter(in = ParameterIn.PATH, name = "branch")
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = ProductRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Product added",
                                            content = @Content(schema = @Schema(implementation = FranchiseResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Branch or franchise not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/franchises/{id}/branch/{branch}/product/{product}/stock",
                    produces = {"application/json"},
                    method = {RequestMethod.PUT},
                    beanClass = Handler.class,
                    beanMethod = "updateStock",
                    operation = @Operation(
                            operationId = "updateStock",
                            summary = "Update stock of a product",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id"),
                                    @Parameter(in = ParameterIn.PATH, name = "branch"),
                                    @Parameter(in = ParameterIn.PATH, name = "product")
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = UpdateStockRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Stock updated",
                                            content = @Content(schema = @Schema(implementation = FranchiseResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Product or branch not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/franchises/{id}/branch/max-stock",
                    produces = {"application/json"},
                    method = {RequestMethod.GET},
                    beanClass = Handler.class,
                    beanMethod = "getMaxStockPerBranch",
                    operation = @Operation(
                            operationId = "getMaxStockPerBranch",
                            summary = "Get product with max stock per branch",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id")
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "List of branches with product max stock",
                                            content = @Content(schema = @Schema(implementation = MaxStockResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Franchise not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/franchises/{id}/name",
                    produces = {"application/json"},
                    method = {RequestMethod.PUT},
                    beanClass = Handler.class,
                    beanMethod = "updateFranchiseName",
                    operation = @Operation(
                            operationId = "updateFranchiseName",
                            summary = "Update franchise name",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id")
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = UpdateNameRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Franchise name updated",
                                            content = @Content(schema = @Schema(implementation = FranchiseResponse.class)))
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/franchises/{id}/branch/{branch}/name",
                    produces = {"application/json"},
                    method = {RequestMethod.PUT},
                    beanClass = Handler.class,
                    beanMethod = "updateBranchName",
                    operation = @Operation(
                            operationId = "updateBranchName",
                            summary = "Update branch name",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id"),
                                    @Parameter(in = ParameterIn.PATH, name = "branch")
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = UpdateNameRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Branch name updated",
                                            content = @Content(schema = @Schema(implementation = FranchiseResponse.class)))
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/franchises/{id}/branch/{branch}/product/{product}/name",
                    produces = {"application/json"},
                    method = {RequestMethod.PUT},
                    beanClass = Handler.class,
                    beanMethod = "updateProductName",
                    operation = @Operation(
                            operationId = "updateProductName",
                            summary = "Update product name",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id"),
                                    @Parameter(in = ParameterIn.PATH, name = "branch"),
                                    @Parameter(in = ParameterIn.PATH, name = "product")
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = UpdateNameRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Product name updated",
                                            content = @Content(schema = @Schema(implementation = FranchiseResponse.class)))
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/franchises"), handler::createFranchise)
                .andRoute(POST("/api/franchises/{id}/branch"), handler::addBranch)
                .andRoute(POST("/api/franchises/{id}/branch/{branch}/product"), handler::addProduct)
                .andRoute(DELETE("/api/franchises/{id}/branch/{branch}/product/{product}"), handler::removeProduct)
                .andRoute(PUT("/api/franchises/{id}/branch/{branch}/product/{product}/stock"), handler::updateStock)
                .andRoute(GET("/api/franchises/{id}/branch/max-stock"), handler::getMaxStockPerBranch)
                .andRoute(PUT("/api/franchises/{id}/name"), handler::updateFranchiseName)
                .andRoute(PUT("/api/franchises/{id}/branch/{branch}/name"), handler::updateBranchName)
                .andRoute(PUT("/api/franchises/{id}/branch/{branch}/product/{product}/name"), handler::updateProductName);
    }
}
