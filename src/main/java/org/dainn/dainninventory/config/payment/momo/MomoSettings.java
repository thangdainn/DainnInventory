package org.dainn.dainninventory.config.payment.momo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class MomoSettings {
        @Value("${momo.api-url}")
        private String momoApiUrl;

        @Value("${momo.secret-key}")
        private String secretKey;

        @Value("${momo.access-key}")
        private String accessKey;

        @Value("${momo.return-url}")
        private String returnUrl;

        @Value("${momo.notify-url}")
        private String notifyUrl;

        @Value("${momo.partner-code}")
        private String partnerCode;

        @Value("${momo.request-type}")
        private String requestType;
}
