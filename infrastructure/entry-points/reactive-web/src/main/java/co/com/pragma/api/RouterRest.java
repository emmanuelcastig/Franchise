package co.com.pragma.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
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
