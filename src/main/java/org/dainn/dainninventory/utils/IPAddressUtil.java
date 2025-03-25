package org.dainn.dainninventory.utils;

import jakarta.servlet.http.HttpServletRequest;

public class IPAddressUtil {
    public static String getClientIpAddress(HttpServletRequest request) {
        String[] IP_HEADER_CANDIDATES = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_CLIENT_IP",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "X-Real-IP"
        };

        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0]; // Lấy IP đầu tiên nếu có nhiều IP
            }
        }

        return request.getRemoteAddr();
    }
}
