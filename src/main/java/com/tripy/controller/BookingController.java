package com.tripy.controller;

import com.tripy.dto.TripyDTOs;
import com.tripy.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // POST /api/bookings
    @PostMapping
    public ResponseEntity<TripyDTOs.ApiResponse> createBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TripyDTOs.BookingRequest request) {

        TripyDTOs.BookingResponse booking = bookingService.createBooking(
                userDetails.getUsername(), request);
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("Booking confirmed successfully", booking));
    }

    // GET /api/bookings/my
    @GetMapping("/my")
    public ResponseEntity<TripyDTOs.ApiResponse> getMyBookings(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<TripyDTOs.BookingResponse> bookings = bookingService.getMyBookings(
                userDetails.getUsername());
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("Bookings fetched successfully", bookings));
    }

    // GET /api/bookings/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TripyDTOs.ApiResponse> getBookingById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        TripyDTOs.BookingResponse booking = bookingService.getBookingById(id,
                userDetails.getUsername());
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("Booking fetched successfully", booking));
    }

    // PUT /api/bookings/{id}/cancel
    @PutMapping("/{id}/cancel")
    public ResponseEntity<TripyDTOs.ApiResponse> cancelBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        TripyDTOs.BookingResponse booking = bookingService.cancelBooking(
                userDetails.getUsername(), id);
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("Booking cancelled successfully", booking));
    }
}
