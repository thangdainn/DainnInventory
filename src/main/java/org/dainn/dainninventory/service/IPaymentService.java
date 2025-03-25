package org.dainn.dainninventory.service;

import jakarta.servlet.http.HttpServletRequest;
import org.dainn.dainninventory.dto.payment.PaymentDTO;

public interface IPaymentService {
    PaymentDTO.VNPAYResponse createVNPayPayment(HttpServletRequest request, Integer orderId);
}
