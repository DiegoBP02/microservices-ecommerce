package com.programming.paymentservice.services;

import com.programming.paymentservice.dtos.OrderResponse;
import com.programming.paymentservice.dtos.PaymentRequest;
import com.programming.paymentservice.exceptions.ResourceNotFoundException;
import com.programming.paymentservice.models.Payment;
import com.programming.paymentservice.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {
    private final String ORDER_SERVICE_URL = "http://ORDER-SERVICE/api/v1/orders/";

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public void create(PaymentRequest orderRequest) {
        String url = UriComponentsBuilder.fromHttpUrl(ORDER_SERVICE_URL + "/{orderId}")
                .buildAndExpand(orderRequest.getOrderId())
                .toUriString();

        OrderResponse orderResponse = webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(OrderResponse.class)
                .block();

        boolean hasSufficientBalanceForPayment =
                canMakePayment(orderResponse.getTotalAmount(), orderRequest.getBalance());

        if(!hasSufficientBalanceForPayment){
            log.error("The user does not have enough balance to make the payment.");
            return;
        }

        log.info("Successful!!!!!!!!!!!!!!!");
    }

    private boolean canMakePayment(BigDecimal totalAmount, BigDecimal balance) {
        return balance.compareTo(totalAmount) >= 0;
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment findById(UUID id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public void delete(UUID id) {
        paymentRepository.deleteById(id);
    }

}
