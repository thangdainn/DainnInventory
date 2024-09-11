package org.dainn.dainninventory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.service.IOrderService;
import org.dainn.dainninventory.service.impl.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final IOrderService orderService;

    @GetMapping("/vnp")
    public ResponseEntity<?> createVNPayPayment(HttpServletRequest request, Integer orderId) {
        return ResponseEntity.ok(paymentService.createVNPayPayment(request, orderId));
    }

    @GetMapping("/vnp-callback")
    public void vnpCallback(@RequestParam String vnp_ResponseCode,
                            @RequestParam Integer vnp_TxnRef,
                            HttpServletResponse response) throws IOException {
        if (vnp_ResponseCode.equals("00")) {
            orderService.updatePaid(vnp_TxnRef);
        }
        response.sendRedirect("http://localhost:4200/order-status?vnp_TxnRef=" + vnp_TxnRef + "&vnp_ResponseCode=" + vnp_ResponseCode);

    }
}
