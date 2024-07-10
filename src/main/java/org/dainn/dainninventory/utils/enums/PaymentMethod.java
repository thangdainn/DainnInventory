package org.dainn.dainninventory.utils.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CASH("Cash"),
    BANK_TRANSFER("Bank Transfer");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

}
