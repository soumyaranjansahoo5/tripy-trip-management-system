package com.tripy.controller;

import com.tripy.dto.TripyDTOs;
import com.tripy.service.BookingService;
import com.tripy.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private TripService tripService;

    @Autowired
    private BookingService bookingService;

    // GET /api/admin/trips — all trips including inactive/cancelled
    @GetMapping("/trips")
    public ResponseEntity<TripyDTOs.ApiResponse> getAllTrips() {
        List<TripyDTOs.TripResponse> trips = tripService.getAllTrips();
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("All trips fetched", trips));
    }

    // GET /api/admin/bookings — all bookings across all users
    @GetMapping("/bookings")
    public ResponseEntity<TripyDTOs.ApiResponse> getAllBookings() {
        List<TripyDTOs.BookingResponse> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("All bookings fetched", bookings));
    }
}
