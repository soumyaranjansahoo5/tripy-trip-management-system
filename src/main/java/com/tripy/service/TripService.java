package com.tripy.service;

import com.tripy.dto.TripyDTOs;
import com.tripy.entity.Trip;
import com.tripy.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    // ─── READ OPERATIONS ───

    @Transactional(readOnly = true)
    public List<TripyDTOs.TripResponse> getAllActiveTrips() {
        return tripRepository.findByStatus(Trip.TripStatus.ACTIVE)
                .stream()
                .map(TripyDTOs.TripResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TripyDTOs.TripResponse getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + id));
        return TripyDTOs.TripResponse.fromEntity(trip);
    }

    @Transactional(readOnly = true)
    public List<TripyDTOs.TripResponse> searchTrips(String source, String destination, LocalDate date) {
        return tripRepository.findAvailableTrips(source, destination, date)
                .stream()
                .map(TripyDTOs.TripResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TripyDTOs.TripResponse> getAllTrips() {
        return tripRepository.findAll()
                .stream()
                .map(TripyDTOs.TripResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // ─── WRITE OPERATIONS ───

    @Transactional
    public TripyDTOs.TripResponse createTrip(TripyDTOs.TripRequest request) {

        if (request == null) {
            throw new RuntimeException("Request cannot be null");
        }

        if (request.getEndDate() != null && request.getStartDate() != null &&
                request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }

        Trip trip = new Trip();
        trip.setTitle(request.getTitle());
        trip.setDescription(request.getDescription());
        trip.setSource(request.getSource());
        trip.setDestination(request.getDestination());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setPricePerPerson(request.getPricePerPerson());
        trip.setAvailableSeats(request.getAvailableSeats());
        trip.setImageUrl(request.getImageUrl());
        trip.setStatus(Trip.TripStatus.ACTIVE);

        Trip saved = tripRepository.save(trip);
        return TripyDTOs.TripResponse.fromEntity(saved);
    }

    @Transactional
    public TripyDTOs.TripResponse updateTrip(Long id, TripyDTOs.TripRequest request) {

        if (request == null) {
            throw new RuntimeException("Request cannot be null");
        }

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + id));

        trip.setTitle(request.getTitle());
        trip.setDescription(request.getDescription());
        trip.setSource(request.getSource());
        trip.setDestination(request.getDestination());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setPricePerPerson(request.getPricePerPerson());
        trip.setAvailableSeats(request.getAvailableSeats());
        trip.setImageUrl(request.getImageUrl());

        Trip updated = tripRepository.save(trip);
        return TripyDTOs.TripResponse.fromEntity(updated);
    }

    @Transactional
    public void deleteTrip(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + id));
        trip.setStatus(Trip.TripStatus.CANCELLED);
        tripRepository.save(trip);
    }

    // ─── SEAT MANAGEMENT ───

    @Transactional
    public void decreaseSeats(Trip trip, int persons) {

        if (trip == null) {
            throw new RuntimeException("Trip cannot be null");
        }

        if (trip.getAvailableSeats() < persons) {
            throw new RuntimeException("Insufficient seats available");
        }

        trip.setAvailableSeats(trip.getAvailableSeats() - persons);
        tripRepository.save(trip);
    }

    @Transactional
    public void increaseSeats(Trip trip, int persons) {

        if (trip == null) {
            throw new RuntimeException("Trip cannot be null");
        }

        trip.setAvailableSeats(trip.getAvailableSeats() + persons);
        tripRepository.save(trip);
    }
}