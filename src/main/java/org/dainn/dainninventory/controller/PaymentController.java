package org.dainn.dainninventory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.config.endpoint.Endpoint;
import org.dainn.dainninventory.dto.momo.MomoCallbackDTO;
import org.dainn.dainninventory.service.IMomoService;
import org.dainn.dainninventory.service.IOrderService;
import org.dainn.dainninventory.service.IPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(Endpoint.Payment.BASE)
@RequiredArgsConstructor
public class PaymentController {
    private final IPaymentService paymentService;
    private final IOrderService orderService;
    private final IMomoService momoService;

    @GetMapping(Endpoint.Payment.VN_PAY)
    public ResponseEntity<?> createVNPayPayment(HttpServletRequest request, Integer orderId) {
        return ResponseEntity.ok(paymentService.createVNPayPayment(request, orderId));
    }

    @GetMapping(Endpoint.Payment.VN_PAY_CALLBACK)
    public void vnpCallback(@RequestParam String vnp_ResponseCode,
                            @RequestParam Integer vnp_TxnRef,
                            HttpServletResponse response) throws IOException {
        if (vnp_ResponseCode.equals("00")) {
            orderService.updateIsPaid(vnp_TxnRef);
        }
        response.sendRedirect("http://localhost:4200/order-status?orderOd=" + vnp_TxnRef + "&ResponseCode=" + vnp_ResponseCode);

    }

    @GetMapping(Endpoint.Payment.MOMO)
    public ResponseEntity<?> createPaymentMomo(Integer orderId) throws Exception {
        return ResponseEntity.ok(momoService.createMomoPayment(orderId));
    }

    @GetMapping(Endpoint.Payment.MOMO_CALLBACK)
    public void handleMomoCallBack(MomoCallbackDTO callbackDto,
                                   HttpServletResponse response) throws IOException {
        int resultCode = momoService.handleMomoCallBack(callbackDto);
        int orderId = Integer.parseInt(callbackDto.getOrderId().substring(0, callbackDto.getOrderId().indexOf("_")));
        response.sendRedirect("http://localhost:4200/donation-status?orderId=" + orderId + "responseCode=" + resultCode);
    }
}
