package com.tripy.dto;

import com.tripy.entity.Trip;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TripyDTOs {

    // ================= AUTH =================

    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
        private String phone;

        public RegisterRequest() {}

        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getPhone() { return phone; }

        public void setName(String name) { this.name = name; }
        public void setEmail(String email) { this.email = email; }
        public void setPassword(String password) { this.password = password; }
        public void setPhone(String phone) { this.phone = phone; }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest() {}

        public String getEmail() { return email; }
        public String getPassword() { return password; }

        public void setEmail(String email) { this.email = email; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String name;
        private String email;
        private String role;

        public AuthResponse() {}

        public AuthResponse(String token, Long id, String name, String email, String role) {
            this.token = token;
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role;
        }

        public String getToken() { return token; }
        public String getType() { return type; }
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }

        public void setToken(String token) { this.token = token; }
        public void setType(String type) { this.type = type; }
        public void setId(Long id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setEmail(String email) { this.email = email; }
        public void setRole(String role) { this.role = role; }
    }

    // ================= TRIP =================

    public static class TripRequest {
        private String title;
        private String description;
        private String source;
        private String destination;
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal pricePerPerson;
        private Integer availableSeats;
        private String imageUrl;

        public TripRequest() {}

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getSource() { return source; }
        public String getDestination() { return destination; }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
        public BigDecimal getPricePerPerson() { return pricePerPerson; }
        public Integer getAvailableSeats() { return availableSeats; }
        public String getImageUrl() { return imageUrl; }

        public void setTitle(String title) { this.title = title; }
        public void setDescription(String description) { this.description = description; }
        public void setSource(String source) { this.source = source; }
        public void setDestination(String destination) { this.destination = destination; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        public void setPricePerPerson(BigDecimal pricePerPerson) { this.pricePerPerson = pricePerPerson; }
        public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }

    public static class TripResponse {
        private Long id;
        private String title;
        private int durationDays;

        public TripResponse() {}

        public TripResponse(Long id, String title, int durationDays) {
            this.id = id;
            this.title = title;
            this.durationDays = durationDays;
        }

        public static TripResponse fromEntity(Trip trip) {
            int days = 0;
            if (trip.getStartDate() != null && trip.getEndDate() != null) {
                days = (int)(trip.getEndDate().toEpochDay() - trip.getStartDate().toEpochDay());
            }
            return new TripResponse(trip.getId(), trip.getTitle(), days);
        }

        public Long getId() { return id; }
        public String getTitle() { return title; }
        public int getDurationDays() { return durationDays; }

        public void setId(Long id) { this.id = id; }
        public void setTitle(String title) { this.title = title; }
        public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
    }

    // ================= BOOKING =================

    public static class BookingRequest {
        private Long tripId;
        private Integer numberOfPersons;
        private String specialRequests;

        public BookingRequest() {}

        public Long getTripId() { return tripId; }
        public Integer getNumberOfPersons() { return numberOfPersons; }
        public String getSpecialRequests() { return specialRequests; }

        public void setTripId(Long tripId) { this.tripId = tripId; }
        public void setNumberOfPersons(Integer numberOfPersons) { this.numberOfPersons = numberOfPersons; }
        public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    }

    // ================= COMMON =================

    public static class ApiResponse {

        private boolean success;
        private String message;
        private Object data;

        public ApiResponse() {}

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        // 🔥 REQUIRED METHODS (FIX)

        public static ApiResponse success(String message, Object data) {
            return new ApiResponse(true, message, data);
        }

        public static ApiResponse error(String message) {
            return new ApiResponse(false, message, null);
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }

        public void setSuccess(boolean success) { this.success = success; }
        public void setMessage(String message) { this.message = message; }
        public void setData(Object data) { this.data = data; }
    }

    // ================= BOOKING RESPONSE =================

    public static class BookingResponse {

        private Long id;
        private Long tripId;
        private String tripTitle;
        private Integer numberOfPersons;
        private String status;
        private String bookingDate;
        private String specialRequests;

        public BookingResponse() {}

        public BookingResponse(Long id, Long tripId, String tripTitle,
                               Integer numberOfPersons, String status,
                               String bookingDate, String specialRequests) {
            this.id = id;
            this.tripId = tripId;
            this.tripTitle = tripTitle;
            this.numberOfPersons = numberOfPersons;
            this.status = status;
            this.bookingDate = bookingDate;
            this.specialRequests = specialRequests;
        }

        public static BookingResponse fromEntity(com.tripy.entity.Booking booking) {

            if (booking == null) return null;

            Long tripId = null;
            String tripTitle = null;

            if (booking.getTrip() != null) {
                tripId = booking.getTrip().getId();
                tripTitle = booking.getTrip().getTitle();
            }

            return new BookingResponse(
                    booking.getId(),
                    tripId,
                    tripTitle,
                    booking.getNumberOfPersons(),
                    booking.getStatus().name(),
                    booking.getBookingDate().toString(),
                    booking.getSpecialRequests()
            );
        }

        public Long getId() { return id; }
        public Long getTripId() { return tripId; }
        public String getTripTitle() { return tripTitle; }
        public Integer getNumberOfPersons() { return numberOfPersons; }
        public String getStatus() { return status; }
        public String getBookingDate() { return bookingDate; }
        public String getSpecialRequests() { return specialRequests; }

        public void setId(Long id) { this.id = id; }
        public void setTripId(Long tripId) { this.tripId = tripId; }
        public void setTripTitle(String tripTitle) { this.tripTitle = tripTitle; }
        public void setNumberOfPersons(Integer numberOfPersons) { this.numberOfPersons = numberOfPersons; }
        public void setStatus(String status) { this.status = status; }
        public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
        public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    }
}