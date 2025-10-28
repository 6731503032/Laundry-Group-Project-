package controller;

import model.BookingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import service.BookingService;

@Controller
public class FrontendController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/student/dashboard")
    public String studentDashboard() {
        return "dashboard";
    }

    @GetMapping("/manager/dashboard")
    public String managerDashboard() {
        return "dashboard";
    }

    @GetMapping("/booking")
    public String booking(Model model) {
        // Add booking data to model
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("totalBookings", bookingService.getTotalBookings());
        model.addAttribute("confirmedBookings", bookingService.getBookingCountByStatus(BookingStatus.CONFIRMED));
        model.addAttribute("pendingBookings", bookingService.getBookingCountByStatus(BookingStatus.PENDING));
        model.addAttribute("cancelledBookings", bookingService.getBookingCountByStatus(BookingStatus.CANCELLED));
        return "booking";
    }

    @GetMapping("/rating")
    public String rating() {
        return "rating";
    }

    @GetMapping("/timer")
    public String timer() {
        return "timer";
    }
}
