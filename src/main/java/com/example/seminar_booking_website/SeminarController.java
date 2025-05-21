package com.example.seminar_booking_website;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.ui.Model;

import com.example.seminar_booking_website.model.User; // 🚨 this tells Spring what "User" is
import com.example.seminar_booking_website.repository.UserRepository; // 🚨 same for repo
import com.example.seminar_booking_website.model.Booking;
import com.example.seminar_booking_website.repository.BookingRepository;

import java.time.LocalTime;
import java.time.LocalDate;

@Controller
public class SeminarController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        model.addAttribute("isLoggedIn", session.getAttribute("user")!=null);
        return "home"; // this will load home.html from templates
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // loads login.html from templates
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // destroy the session
        return "redirect:/"; // go back to home
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               Model model,
                               HttpSession session) {
    User user = userRepository.findByEmail(email);

    if (user == null) {
        model.addAttribute("error", "Email not found.");
        return "login";
    }

    if (user.getPassword() == null || user.getPassword().isEmpty()) {
        // First-time password set 🚀
        user.setPassword(password);
        userRepository.save(user);
        session.setAttribute("user", user); // store user in session
        model.addAttribute("message", "Password set successfully! You're now logged in.");
        return "redirect:/";
    } 
    else {
        // Password already exists, validate it
        if (user.getPassword().equals(password)) {
            session.setAttribute("user",user);
            model.addAttribute("message", "Login successful!");
            return "redirect:/";
        } else {
            model.addAttribute("error", "Incorrect password");
            return "login";
        }
    }
}


    @GetMapping("/event")
    public String showEventsPage(Model model) {
    List<Booking> allBookings = bookingRepository.findAll();

    LocalDate today = LocalDate.now();
    LocalTime now = LocalTime.now();

    List<Booking> ongoingEvents = new ArrayList<>();
    List<Booking> upcomingEvents = new ArrayList<>();

    for (Booking booking : allBookings) {
        if (booking.getDate().equals(today)) {
            // Compare times too!
            if (now.isAfter(booking.getTime()) && now.isBefore(booking.getEndTime())) {
                ongoingEvents.add(booking);
            } else if (now.isBefore(booking.getTime())) {
                upcomingEvents.add(booking);
            }
        } else if (booking.getDate().isAfter(today)) {
            upcomingEvents.add(booking);
        }
    }

    model.addAttribute("ongoingEvents", ongoingEvents);
    model.addAttribute("upcomingEvents", upcomingEvents);

    return "event";
    }


    @GetMapping("/apj")
    public String apj(HttpSession session, Model model) {
        model.addAttribute("isLoggedIn", session.getAttribute("user")!=null);
        return "apj"; // loads apj.html
    }

    @GetMapping("/db")
    public String db(HttpSession session, Model model) {
        model.addAttribute("isLoggedIn", session.getAttribute("user")!=null);
        return "db"; // loads db.html
    }

    @GetMapping("/sapphire")
    public String sapphire(HttpSession session, Model model) {
        model.addAttribute("isLoggedIn", session.getAttribute("user")!=null);
        return "sapphire"; // loads sapphire.html
    }

    @GetMapping("/smb")
    public String smb(HttpSession session, Model model) {
        model.addAttribute("isLoggedIn", session.getAttribute("user")!=null);
        return "smb"; // loads smb.html
    }

    @PostMapping("/bookHall")
    @ResponseBody
    public String bookHall(@RequestParam String eventName,
                        @RequestParam String eventDate,
                        @RequestParam String eventTime,
                        @RequestParam String endTime,
                        @RequestParam String hallName,
                        @RequestParam String venue,
                        HttpSession session) {

    LocalDate date = LocalDate.parse(eventDate);
    LocalTime start = LocalTime.parse(eventTime);
    LocalTime end  = LocalTime.parse(endTime);

        User user=(User) session.getAttribute("user");
        if(user==null) return "login";

    List<Booking> existingBookings = bookingRepository.findByHallNameAndDate(hallName, date);
    for (Booking booking : existingBookings) {
        if (start.isBefore(booking.getEndTime()) && end.isAfter(booking.getTime())) {
            return "❌ Time slot overlaps with an existing booking!";
        }
    }


    Booking b = new Booking();
    b.setEventName(eventName);
    b.setHallName(hallName);
    b.setVenue(venue);
    b.setDate(date);
    b.setTime(start); // set start time
    b.setEndTime(end); // set end time
    b.setUser(user);  // store user's email
    bookingRepository.save(b);

    return "✅ Booking Confirmed!";
    }

    @GetMapping("/mybooking")
    public String viewBookings(HttpSession session, Model model) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
        model.addAttribute("isLoggedIn", false);
        return "redirect:/login";
    }
    model.addAttribute("isLoggedIn", true);
    List<Booking> bookings = bookingRepository.findByUser(user);
    model.addAttribute("bookings", bookings);
    return "mybooking";
    }

    @PostMapping("/deleteBooking")
    public String deleteBooking(@RequestParam Long id, HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
        return "redirect:/login";
    }
    Booking booking = bookingRepository.findById(id).orElse(null);
    if (booking != null && booking.getUser().getId().equals(user.getId())) {
        bookingRepository.delete(booking);
    }
    return "redirect:/mybooking"; // refresh the page after deletion
    }

    @GetMapping("/bookings")
    public String viewAllBookings(Model model, HttpSession session) {
    model.addAttribute("isLoggedIn", session.getAttribute("user") != null);
    List<Booking> bookings = bookingRepository.findAll();
    model.addAttribute("bookings", bookings);
    return "bookings";  // 👈 Make sure this matches your new HTML file name
    }



    // add more if you have more HTML files
}
