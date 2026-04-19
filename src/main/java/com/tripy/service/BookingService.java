package com.tripy.service;

import com.tripy.dto.TripyDTOs;
import com.tripy.entity.Booking;
import com.tripy.entity.Trip;
import com.tripy.entity.User;
import com.tripy.repository.BookingRepository;
import com.tripy.repository.TripRepository;
import com.tripy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TripService tripService;

    // ─────────────────────────────────────────
    // CREATE BOOKING
    // ─────────────────────────────────────────

    @Transactional(rollbackFor = Exception.class)
    public TripyDTOs.BookingResponse createBooking(String userEmail, TripyDTOs.BookingRequest request) {

        if (userEmail == null || userEmail.isEmpty()) {
            throw new RuntimeException("User email cannot be null");
        }

        if (request == null) {
            throw new RuntimeException("Request cannot be null");
        }

        if (request.getTripId() == null) {
            throw new RuntimeException("Trip ID cannot be null");
        }

        if (request.getNumberOfPersons() == null || request.getNumberOfPersons() <= 0) {
            throw new RuntimeException("Invalid number of persons");
        }

        // 1. Load user
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        // 2. Load trip
        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + request.getTripId()));

        if (trip.getStatus() != Trip.TripStatus.ACTIVE) {
            throw new RuntimeException("Trip is not available for booking");
        }

        if (trip.getAvailableSeats() < request.getNumberOfPersons()) {
            throw new RuntimeException("Insufficient seats. Available: " + trip.getAvailableSeats());
        }

        // 3. Calculate cost
        BigDecimal totalCost = trip.getPricePerPerson()
                .multiply(BigDecimal.valueOf(request.getNumberOfPersons()));

        // 4. Decrease seats
        tripService.decreaseSeats(trip, request.getNumberOfPersons());

        // 5. Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTrip(trip);
        booking.setNumberOfPersons(request.getNumberOfPersons());
        booking.setTotalCost(totalCost);
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setBookingDate(LocalDateTime.now());
        booking.setSpecialRequests(request.getSpecialRequests());

        Booking saved = bookingRepository.save(booking);
        return TripyDTOs.BookingResponse.fromEntity(saved);
    }

    // ─────────────────────────────────────────
    // CANCEL BOOKING
    // ─────────────────────────────────────────

    @Transactional(rollbackFor = Exception.class)
    public TripyDTOs.BookingResponse cancelBooking(String userEmail, Long bookingId) {

        if (userEmail == null || userEmail.isEmpty()) {
            throw new RuntimeException("User email cannot be null");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Not authorized");
        }

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Already cancelled");
        }

        tripService.increaseSeats(booking.getTrip(), booking.getNumberOfPersons());

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        Booking updated = bookingRepository.save(booking);

        return TripyDTOs.BookingResponse.fromEntity(updated);
    }

    // ─────────────────────────────────────────
    // READ OPERATIONS
    // ─────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<TripyDTOs.BookingResponse> getMyBookings(String userEmail) {

        if (userEmail == null || userEmail.isEmpty()) {
            throw new RuntimeException("User email cannot be null");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUserId(user.getId())
                .stream()
                .map(TripyDTOs.BookingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TripyDTOs.BookingResponse getBookingById(Long bookingId, String userEmail) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Access denied");
        }

        return TripyDTOs.BookingResponse.fromEntity(booking);
    }

    @Transactional(readOnly = true)
    public List<TripyDTOs.BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(TripyDTOs.BookingResponse::fromEntity)
                .collect(Collectors.toList());
    }
}