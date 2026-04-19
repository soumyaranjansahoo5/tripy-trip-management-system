package com.tripy.repository;

import com.tripy.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByStatus(Trip.TripStatus status);

    List<Trip> findByDestinationContainingIgnoreCase(String destination);

    @Query("SELECT t FROM Trip t WHERE t.source = :source AND t.destination = :destination AND t.startDate >= :date AND t.status = 'ACTIVE'")
    List<Trip> findAvailableTrips(@Param("source") String source,
                                  @Param("destination") String destination,
                                  @Param("date") LocalDate date);

    @Query("SELECT t FROM Trip t WHERE t.availableSeats >= :persons AND t.status = 'ACTIVE'")
    List<Trip> findTripsWithAvailableSeats(@Param("persons") int persons);
}
