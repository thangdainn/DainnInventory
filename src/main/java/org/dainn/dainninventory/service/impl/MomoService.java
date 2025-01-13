package org.dainn.dainninventory.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.config.payment.momo.CreateSignature;
import org.dainn.dainninventory.config.payment.momo.MomoSettings;
import org.dainn.dainninventory.dto.OrderDTO;
import org.dainn.dainninventory.dto.momo.ExtraData;
import org.dainn.dainninventory.dto.momo.MomoCallbackDTO;
import org.dainn.dainninventory.dto.momo.MomoCreatePaymentDTO;
import org.dainn.dainninventory.dto.momo.PaymentRequestData;
import org.dainn.dainninventory.service.IMomoService;
import org.dainn.dainninventory.service.IOrderService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MomoService implements IMomoService {
    private final MomoSettings momoSettings;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CreateSignature createSignature;
    private final IOrderService orderService;


    @Override
    public MomoCreatePaymentDTO createMomoPayment(Integer orderId) throws Exception {
        OrderDTO order = orderService.findById(orderId);
        String billInfo = "Payment for order " + orderId;
        String requestId = UUID.randomUUID().toString();
        String orderIdStr = orderId + "_" + UUID.randomUUID();
        String extraData = Base64.getEncoder().encodeToString(objectMapper.writeValueAsBytes(new ExtraData(orderId)));

        String rawData = String.format("accessKey=%s&amount=%.0f&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s" +
                        "&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                momoSettings.getAccessKey(),
                order.getTotalAmount(),
                extraData,
                momoSettings.getNotifyUrl(),
                orderIdStr,
                billInfo,
                momoSettings.getPartnerCode(),
                momoSettings.getReturnUrl(),
                requestId,
                momoSettings.getRequestType());

        String signature = createSignature.computeHmacSha256(rawData, momoSettings.getSecretKey());

        PaymentRequestData requestData = new PaymentRequestData(
                momoSettings.getPartnerCode(),
                requestId,
                order.getTotalAmount(),
                orderIdStr,
                billInfo,
                momoSettings.getReturnUrl(),
                momoSettings.getNotifyUrl(),
                "vi",
                15,
                extraData,
                momoSettings.getRequestType(),
                signature,
                true
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<PaymentRequestData> httpEntity = new HttpEntity<>(requestData, headers);
        try {
            ResponseEntity<MomoCreatePaymentDTO> response = restTemplate.exchange(
                    momoSettings.getMomoApiUrl(),
                    HttpMethod.POST,
                    httpEntity,
                    MomoCreatePaymentDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new Exception("Failed to create Momo payment: " + Objects.requireNonNull(response.getBody()).getMessage());
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public int handleMomoCallBack(MomoCallbackDTO callbackDto) {
        String rawData = String.format("accessKey=%s&amount=%.0f&extraData=%s&message=%s&orderId=%s" +
                        "&orderInfo=%s&orderType=%s&partnerCode=%s&payType=%s&requestId=%s" +
                        "&responseTime=%d&resultCode=%d&transId=%d",
                momoSettings.getAccessKey(),
                callbackDto.getAmount(),
                callbackDto.getExtraData(),
                callbackDto.getMessage(),
                callbackDto.getOrderId(),
                callbackDto.getOrderInfo(),
                callbackDto.getOrderType(),
                callbackDto.getPartnerCode(),
                callbackDto.getPayType(),
                callbackDto.getRequestId(),
                callbackDto.getResponseTime(),
                callbackDto.getResultCode(),
                callbackDto.getTransId());

        String expectedSignature;
        try {
            expectedSignature = createSignature.computeHmacSha256(rawData, momoSettings.getSecretKey());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Integer donationId = Integer.parseInt(callbackDto.getOrderId().split("_")[0]);

        if (callbackDto.getSignature().equals(expectedSignature) && callbackDto.getResultCode() == 0) {
            orderService.updateIsPaid(donationId);
        } else {
            throw new RuntimeException("Invalid signature or result code");
        }
        return callbackDto.getResultCode();
    }
}
