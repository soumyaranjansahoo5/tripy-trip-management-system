package com.tripy.controller;

import com.tripy.dto.TripyDTOs;
import com.tripy.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "*")
public class TripController {

    @Autowired
    private TripService tripService;

    // ─── PUBLIC ENDPOINTS ─────────────────────────────────────

    // GET /api/trips/search?source=Delhi&destination=Goa&date=2024-12-01
    @GetMapping("/search")
    public ResponseEntity<TripyDTOs.ApiResponse> searchTrips(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<TripyDTOs.TripResponse> trips = tripService.searchTrips(source, destination, date);
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("Trips fetched successfully", trips));
    }

    // GET /api/trips/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TripyDTOs.ApiResponse> getTripById(@PathVariable Long id) {
        TripyDTOs.TripResponse trip = tripService.getTripById(id);
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("Trip fetched successfully", trip));
    }

    // ─── AUTHENTICATED ENDPOINTS ──────────────────────────────

    // GET /api/trips
    @GetMapping
    public ResponseEntity<TripyDTOs.ApiResponse> getAllActiveTrips() {
        List<TripyDTOs.TripResponse> trips = tripService.getAllActiveTrips();
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("Active trips fetched successfully", trips));
    }

    // ─── ADMIN ONLY ENDPOINTS ─────────────────────────────────

    // POST /api/trips
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TripyDTOs.ApiResponse> createTrip(@Valid @RequestBody TripyDTOs.TripRequest request) {
        TripyDTOs.TripResponse trip = tripService.createTrip(request);
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("Trip created successfully", trip));
    }

    // PUT /api/trips/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TripyDTOs.ApiResponse> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripyDTOs.TripRequest request) {
        TripyDTOs.TripResponse trip = tripService.updateTrip(id, request);
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("Trip updated successfully", trip));
    }

    // DELETE /api/trips/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TripyDTOs.ApiResponse> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.ok(TripyDTOs.ApiResponse.success("Trip cancelled successfully", null));
    }
}
