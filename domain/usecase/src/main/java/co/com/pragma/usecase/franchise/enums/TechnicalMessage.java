package co.com.pragma.usecase.franchise.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    INVALID_REQUEST("400", "Bad Request, please verify data", ""),
    FRANCHISE_NOT_FOUND("404", "Franchise not found", "franchiseId"),
    BRANCH_NOT_FOUND("404", "Branch not found", "branchName"),
    PRODUCT_NOT_FOUND("404", "Product not found", "productName");

    private final String code;
    private final String message;
    private final String param;
}