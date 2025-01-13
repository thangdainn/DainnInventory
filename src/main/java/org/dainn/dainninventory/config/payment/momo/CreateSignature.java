package org.dainn.dainninventory.config.payment.momo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CreateSignature {
        private String rawData;

        private String secretKey;

        public String computeHmacSha256(String rawData, String secretKey) throws Exception {
                Mac hmac = Mac.getInstance("HmacSHA256");
                SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
                hmac.init(keySpec);
                byte[] hash = hmac.doFinal(rawData.getBytes(StandardCharsets.UTF_8));
                return bytesToHex(hash);
        }

        private String bytesToHex(byte[] bytes) {
                StringBuilder hexString = new StringBuilder();
                for (byte b : bytes) {
                        String hex = Integer.toHexString(0xFF & b);
                        if (hex.length() == 1) hexString.append('0');
                        hexString.append(hex);
                }
                return hexString.toString();
        }
}
