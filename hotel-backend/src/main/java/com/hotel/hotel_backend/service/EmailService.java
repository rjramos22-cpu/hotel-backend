package com.hotel.hotel_backend.service;

import com.hotel.hotel_backend.entity.ReservaEntity;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarConfirmacionReserva(ReservaEntity reserva){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("onboarding@resend.dev");
        message.setTo(reserva.getUser().getEmail());
        message.setSubject("Confirmacion de reserva - Hotel");
        message.setText(
                "Hola " + reserva.getUser().getName() + ",\n\n" +
                "Tu reserva ha sido confirmada.\n" +
                "habitacion: " + reserva.getRoom().getName()  + "\n" +
                "Check-in: " + reserva.getCheckIn() + "\n" +
                "Check-out: " + reserva.getCheckOut() + "\n" +
                "Total: $" + reserva.getPrecioTotal() + "\n" +
                 "Gracias por elegirnos."
        );
        try {
            mailSender.send(message);
        } catch (Exception e){
            System.err.println("Error al enviar email: " + e.getMessage());
        }

    }

}
