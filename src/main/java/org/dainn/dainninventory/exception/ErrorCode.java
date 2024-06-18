package org.dainn.dainninventory.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION("Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY("Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED("User existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED("Email existed", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED("User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED("Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB("Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    ROLE_EXISTED("Role existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED("Role not existed", HttpStatus.BAD_REQUEST),
    BRAND_EXISTED("Brand existed", HttpStatus.BAD_REQUEST),
    BRAND_NOT_EXISTED("Brand not existed", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED("Category existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED("Category not existed", HttpStatus.BAD_REQUEST),
    SUPPLIER_EXISTED("Supplier existed", HttpStatus.BAD_REQUEST),
    SUPPLIER_NOT_EXISTED("Supplier not existed", HttpStatus.BAD_REQUEST),
    PRODUCT_EXISTED("Product existed", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED("Product not existed", HttpStatus.BAD_REQUEST),
    UPLOAD_IMAGE_FAILED("Upload image failed", HttpStatus.BAD_REQUEST),

    ;
    ErrorCode(String message, HttpStatusCode statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
    private final String message;
    private final HttpStatusCode statusCode;
}
