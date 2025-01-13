package org.dainn.dainninventory.config.endpoint;

public class Endpoint {
    public static final String API_PREFIX = "/api";

    public static final class Auth {
        public static final String BASE = API_PREFIX + "/auth";
        public static final String ME = "/me";
        public static final String LOGIN = "/login";
        public static final String LOGIN_GOOGLE = "/login/oauth2/google";
        public static final String REGISTER = "/register";
        public static final String REFRESH_TOKEN = "/refresh-token";
        public static final String FORGOT_PASSWORD = "/forgot-password";
        public static final String SEND_OTP = "/send-otp";
        public static final String VERIFY_OTP = "/verify-otp";

    }

    public static final class User {
        public static final String BASE = API_PREFIX + "/users";
        public static final String ID = "/{id}";
    }

    public static final class Role {
        public static final String BASE = API_PREFIX + "/roles";
        public static final String ID = "/{id}";
        public static final String NAME = "/{name}";
    }

    public static final class Category {
        public static final String BASE = API_PREFIX + "/categories";
        public static final String ID = "/{id}";
    }

    public static final class Brand {
        public static final String BASE = API_PREFIX + "/brands";
        public static final String ID = "/{id}";


    }

    public static final class Cart {
        public static final String BASE = API_PREFIX + "/carts";
        public static final String USER_ID = "/users/{userId}";
    }

    public static final class Order {
        public static final String BASE = API_PREFIX + "/orders";
        public static final String ID = "/{id}";
        public static final String MY_ORDER = "/me";
    }

    public static final class Payment {
        public static final String BASE = API_PREFIX + "/payment";
        public static final String VN_PAY = "/vnp";
        public static final String MOMO = "/momo";
        public static final String VN_PAY_CALLBACK = "/vnp-callback";
        public static final String MOMO_CALLBACK = "/momo-callback";

    }

    public static final class Product {
        public static final String BASE = API_PREFIX + "/products";
        public static final String ID = "/{id}";
        public static final String CODE = "/{code}";
    }

    public static final class Size {
        public static final String BASE = API_PREFIX + "/sizes";
        public static final String ID = "/{id}";
        public static final String QUANTITY = "/quantity";
        public static final String QUANTITY_CODE = "/quantity-code";


    }
}
