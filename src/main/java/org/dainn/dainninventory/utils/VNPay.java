package org.dainn.dainninventory.utils;

public class VNPay {
    public static final String VN_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String VN_PAY_RETURN_URL = "http://localhost:8090/api/payment/vnp-callback";
    public static final String VN_PAY_TMN_CODE = "7BQAKMSR";
    public static final String VN_PAY_SECRET_KEY = "TEIU0WUGNE2I2IJ0GW2NRJZAK6O1NDGB";
    public static final String VN_PAY_HASH_ALGORITHM = "SHA256";
    public static final String VN_PAY_VERSION = "2.1.0";
    public static final String VN_PAY_COMMAND = "pay";
    public static final String VN_PAY_ORDER_TYPE = "other";
}
