package org.dainn.dainninventory.config;

import lombok.Getter;
import org.dainn.dainninventory.utils.VNPay;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Configuration
@Getter
public class VNPayConfig {
    private final String vnp_Url = VNPay.VN_PAY_URL;
    private final String vnp_ReturnUrl = VNPay.VN_PAY_RETURN_URL;
    private final String vnp_TmnCode = VNPay.VN_PAY_TMN_CODE;
    private final String vnp_SecretKey = VNPay.VN_PAY_SECRET_KEY;
    private final String vnp_SecureHashType = VNPay.VN_PAY_HASH_ALGORITHM;
    private final String vnp_Version = VNPay.VN_PAY_VERSION;
    private final String vnp_Command = VNPay.VN_PAY_COMMAND;
    private final String vnp_OrderType = VNPay.VN_PAY_ORDER_TYPE;

    public Map<String, String> getVNPayConfig(Integer orderId) {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnp_Version);
        vnpParamsMap.put("vnp_Command", this.vnp_Command);
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_TxnRef",  orderId.toString());
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" + orderId);
        vnpParamsMap.put("vnp_OrderType", this.vnp_OrderType);
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
        return vnpParamsMap;
    }
}
