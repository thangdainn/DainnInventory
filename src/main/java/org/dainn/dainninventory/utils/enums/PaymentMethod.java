package org.dainn.dainninventory.utils.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    Cash("Cash"),
    VNPay("VNPay"),
    Momo("Momo");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

}
