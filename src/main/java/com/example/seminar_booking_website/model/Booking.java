package com.example.seminar_booking_website.model;
import com.example.seminar_booking_website.model.User;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;
    private String hallName;
    private String venue;
    private LocalDate date;
    private LocalTime time;
    private LocalTime endTime;

    @ManyToOne
    private User user;

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public String getEventName(){
        return eventName;
    }
    public void setEventName(String eventName){
        this.eventName=eventName;
    }
    public String getHallName(){
        return hallName;
    }
    public void setHallName(String hallName){
        this.hallName=hallName;
    }
    public String getVenue(){
        return venue;
    }
    public void setVenue(String venue){
        this.venue=venue;
    }
    public LocalDate getDate(){
        return date;
    }
    public void setDate(LocalDate date){
        this.date=date;
    }
    public LocalTime getTime(){
        return time;
    }
    public void setTime(LocalTime time){
        this.time=time;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime=endTime;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user=user;
    }
}