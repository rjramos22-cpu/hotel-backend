package com.hotel.hotel_backend.service;

import com.hotel.hotel_backend.DTO.PagoResponseDTO;
import com.hotel.hotel_backend.Exception.ResourceNotFoundException;
import com.hotel.hotel_backend.entity.EstadoPago;
import com.hotel.hotel_backend.entity.EstadoReserva;
import com.hotel.hotel_backend.entity.PagoEntity;
import com.hotel.hotel_backend.entity.ReservaEntity;
import com.hotel.hotel_backend.repository.PagoRepository;
import com.hotel.hotel_backend.repository.ReservaRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PagoService {
    private final ReservaRepository reservaRepository;
    private  final PagoRepository pagoRepository ;
    private final EmailService emailService;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    public String crearPaymentIntent(Long reservaId) throws StripeException {

        Stripe.apiKey = stripeSecretKey;

        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(reserva.getPrecioTotal().multiply(BigDecimal.valueOf(100)).longValue())
                .setCurrency("mxn")
                .putMetadata("reservaId", reserva.getId().toString())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        PagoEntity pago = PagoEntity.builder()
                .stripePaymentId(paymentIntent.getId())
                .monto(reserva.getPrecioTotal())
                .moneda("mxn")
                .estadoPagos(EstadoPago.PENDIENTE)
                .reserva(reserva)
                .build();
        pagoRepository.save(pago);

        return paymentIntent.getClientSecret();
    }


    public PagoResponseDTO confirmarPago (String paymentIntentId){
        PagoEntity pago = pagoRepository.findByStripePaymentId(paymentIntentId)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));

        pago.setEstadoPagos(EstadoPago.COMPLETADO);
        pagoRepository.save(pago);

        ReservaEntity reserva = pago.getReserva();
        reserva.setEstado(EstadoReserva.CONFIRMADA);
        reservaRepository.save(reserva);

        emailService.enviarConfirmacionReserva(reserva);
        return new PagoResponseDTO(
                pago.getId(),
                pago.getMonto(),
                pago.getMoneda(),
                pago.getCreatedAt(),
                pago.getEstadoPagos()
        );
    }

    public PagoResponseDTO reembolso (Long reservaId) throws StripeException{
        Stripe.apiKey = stripeSecretKey;

        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        PagoEntity pago = pagoRepository.findByReserva(reserva)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));

        RefundCreateParams params = RefundCreateParams.builder()
                .setPaymentIntent(pago.getStripePaymentId())
                .build();

        Refund.create(params);


        pago.setEstadoPagos(EstadoPago.REEMBOLSADO);
        pagoRepository.save(pago);

        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);

        return new PagoResponseDTO(
                pago.getId(),
                pago.getMonto(),
                pago.getMoneda(),
                pago.getCreatedAt(),
                pago.getEstadoPagos()
        );
    }
}
