package org.dainn.dainninventory.service;


import org.dainn.dainninventory.dto.momo.MomoCallbackDTO;
import org.dainn.dainninventory.dto.momo.MomoCreatePaymentDTO;

public interface IMomoService {
    MomoCreatePaymentDTO createMomoPayment(Integer donationId) throws Exception;
    int handleMomoCallBack(MomoCallbackDTO callbackDto);
}
