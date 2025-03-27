package org.dainn.dainninventory.dto.momo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MomoCallbackDTO {
        String orderType;

        BigDecimal amount;  // Use BigDecimal for currency values

        String partnerCode;

        String orderId;

        private String extraData;

        String signature;

        long transId;  // This can remain as a primitive type

        long responseTime;  // This can remain as a primitive type

        int resultCode;  // This can remain as a primitive type

        String message;

        String payType;

        String requestId;

        private String orderInfo;

        @Override
        public String toString() {
                return "MomoCallbackDTO{" +
                        "orderType='" + orderType + '\'' +
                        ", amount=" + amount +
                        ", partnerCode='" + partnerCode + '\'' +
                        ", orderId='" + orderId + '\'' +
                        ", extraData='" + extraData + '\'' +
                        ", signature='" + signature + '\'' +
                        ", transId=" + transId +
                        ", responseTime=" + responseTime +
                        ", resultCode=" + resultCode +
                        ", message='" + message + '\'' +
                        ", payType='" + payType + '\'' +
                        ", requestId='" + requestId + '\'' +
                        ", orderInfo='" + orderInfo + '\'' +
                        '}';
        }
}
