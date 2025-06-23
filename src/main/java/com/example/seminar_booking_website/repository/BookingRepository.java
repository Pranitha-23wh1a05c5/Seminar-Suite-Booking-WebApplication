package com.example.seminar_booking_website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.seminar_booking_website.model.Booking;
import com.example.seminar_booking_website.model.User;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByHallNameAndDateBetween(String hallName, LocalDate startDate, LocalDate endDate);
    List<Booking> findByUser(User user);
}