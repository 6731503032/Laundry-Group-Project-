package controller;

import java.time.LocalDate; // (Import เพิ่ม)
import java.time.LocalTime; // (Import เพิ่ม)
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional; // (เพิ่ม)
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import model.AppConstants;
import model.Booking;
import model.BookingStatus; // (เพิ่ม)
import model.Machine;
import model.Role;
import model.User;
import service.BookingService;
import service.MachineService;
import service.UserService;

@Controller
public class DashboardController {
    private final UserService userService;
    private final MachineService machineService;
    private final BookingService bookingService;

    public DashboardController(UserService userService, MachineService machineService, BookingService bookingService) {
        this.userService = userService;
        this.machineService = machineService;
        this.bookingService = bookingService;
    }

    // ==================== Student Dashboard (แก้ไขส่วนนี้) ====================
    @GetMapping("/student/{studentId}/dashboard")
    public String studentDashboard(@PathVariable String studentId, Model model) {
        System.out.println("========== STUDENT DASHBOARD CALLED ==========");
        User currentUser = userService.findByStudentId(studentId);
        
        if (currentUser == null) {
            System.out.println("ERROR: User is null!");
            return "redirect:/login";
        }
        if (currentUser.getRole() != Role.STUDENT) {
            System.out.println("ERROR: User is not a student!");
            return "redirect:/error";
        }
        if (!currentUser.getIsActive()) {
            System.out.println("ERROR: User is not active!");
            return "redirect:/login";
        }
        model.addAttribute("currentUser", currentUser);

        // --- (นี่คือส่วนที่แก้ไข) ---
        // 1. ดึงการจองทั้งหมดของนักเรียนมาก่อน
        List<Booking> allStudentBookings = bookingService.getBookingsByUserId(currentUser.getId());

        // 2. ค้นหาการจองที่ "กำลังใช้งาน" (IN_PROGRESS)
        Optional<Booking> activeBookingOpt = allStudentBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.IN_PROGRESS)
                .findFirst();
        
        // 3. ส่งการจองที่ "กำลังใช้งาน" (ถ้ามี) ไปให้หน้าเว็บ
        model.addAttribute("activeBooking", activeBookingOpt.orElse(null));

        // 4. ส่งการจองที่เหลือ (เช่น PENDING) ไปให้หน้าเว็บ
        List<Booking> upcomingBookings = allStudentBookings.stream()
                .filter(b -> b.getStatus() != BookingStatus.IN_PROGRESS) // กรองอันที่กำลังใช้งานออก
                .collect(Collectors.toList());
        
        model.addAttribute("studentBookings", upcomingBookings);
        // --- (จบส่วนที่แก้ไข) ---


        List<Machine> allMachines = machineService.getAllMachines();
        model.addAttribute("machines", allMachines != null ? allMachines : List.of());
        
        Map<String, Object> studentStats = new HashMap<>();
        studentStats.put("totalMachines", allMachines != null ? allMachines.size() : 0);
        
        long availableCount = allMachines != null ? 
                allMachines.stream().filter(m -> AppConstants.STATUS_AVAILABLE.equals(m.getStatus())).count() : 0;
        studentStats.put("availableMachines", availableCount);
        
        long inUseCount = allMachines != null ? 
                allMachines.stream().filter(m -> AppConstants.STATUS_IN_USE.equals(m.getStatus())).count() : 0;
        studentStats.put("inUseMachines", inUseCount);
        
        model.addAttribute("studentStats", studentStats);
        
        List<Booking> completedBookings = 
                bookingService.getCompletedBookingsForRating(currentUser.getId());
        model.addAttribute("completedBookings", completedBookings != null ? completedBookings : List.of());
        
        // (ส่งข้อมูลเปล่าของ Manager)
        model.addAttribute("managerStats", new HashMap<>());
        model.addAttribute("recentActivities", List.of());
        model.addAttribute("allMachines", allMachines != null ? allMachines : List.of());
        model.addAttribute("allUsers", List.of());
        model.addAttribute("allStatuses", List.of());
        model.addAttribute("todaysBookings", List.of()); // (เพิ่ม)

        System.out.println("========== RETURNING DASHBOARD VIEW FOR STUDENT ==========");
        return "dashboard";
    }

    // ==================== Manager Dashboard (เหมือนเดิม) ====================
    @GetMapping("/manager/{studentId}/dashboard")
    public String managerDashboard(@PathVariable String studentId, Model model) {
        System.out.println("========== MANAGER DASHBOARD CALLED ==========");
        User currentUser = userService.findByStudentId(studentId);
        
        if (currentUser == null) {
            System.out.println("ERROR: User is null!");
            return "redirect:/login";
        }
        if (currentUser.getRole() != Role.MANAGER) {
            System.out.println("ERROR: User is not a manager!");
            return "redirect:/error";
        }
        if (!currentUser.getIsActive()) {
            System.out.println("ERROR: User is not active!");
            return "redirect:/login";
        }
        model.addAttribute("currentUser", currentUser);
        
        //--- (ส่วนนี้เหมือนเดิมจาก PDF ของคุณ)
        List<Machine> allMachines = machineService.getAllMachines();
        List<User> allUsers = userService.findAllUsers();
        List<Booking> recentActivities = bookingService.getAllBookings().stream()
                .sorted((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()))
                .limit(5)
                .collect(Collectors.toList());

        // --- (ดึงตารางจองของวันนี้ - เหมือนเดิม)
        LocalDate today = LocalDate.now();
        List<Booking> todaysBookings = bookingService.getBookingsByDateRange(
                today.atStartOfDay(), // (ตั้งแต่เที่ยงคืน)
                today.atTime(LocalTime.MAX) // (ถึง 23:59:59)
        );

        // --- (คำนวณ Stats ใหม่ - เหมือนเดิม)
        Map<String, Object> managerStats = new HashMap<>();
        long totalMachines = allMachines.size();
        long availableMachines = allMachines.stream().filter(m -> 
                AppConstants.STATUS_AVAILABLE.equals(m.getStatus())).count();
        long inUseMachines = allMachines.stream().filter(m -> 
                AppConstants.STATUS_IN_USE.equals(m.getStatus())).count();
        long maintenanceMachines = allMachines.stream().filter(m -> 
                AppConstants.STATUS_MAINTENANCE.equals(m.getStatus()) || 
                AppConstants.STATUS_OUT_OF_SERVICE.equals(m.getStatus())).count();
        long totalUsers = allUsers.size();
        
        managerStats.put("totalMachines", totalMachines);
        managerStats.put("availableMachines", availableMachines);
        managerStats.put("inUseMachines", inUseMachines);
        managerStats.put("maintenanceMachines", maintenanceMachines);
        managerStats.put("totalUsers", totalUsers);

        model.addAttribute("managerStats", managerStats);
        model.addAttribute("recentActivities", recentActivities);
        model.addAttribute("allMachines", allMachines != null ? allMachines : List.of());
        model.addAttribute("allUsers", allUsers != null ? allUsers : List.of());
        
        // (ส่งตารางจองของวันนี้ไปหน้าเว็บ)
        model.addAttribute("todaysBookings", todaysBookings != null ? todaysBookings : List.of());
        
        // (แก้ไข Statuses - เหมือนเดิม)
        model.addAttribute("allStatuses", List.of(
                AppConstants.STATUS_AVAILABLE,
                AppConstants.STATUS_MAINTENANCE,
                AppConstants.STATUS_OUT_OF_SERVICE
        ));
        
        // (ส่งข้อมูลเปล่าของ Student)
        model.addAttribute("studentStats", new HashMap<>());
        model.addAttribute("studentBookings", List.of());
        model.addAttribute("completedBookings", List.of());
        
        System.out.println("========== RETURNING DASHBOARD VIEW FOR MANAGER ==========");
        return "dashboard";
    }

    // (เมธอด testUser ไม่ได้แก้ไข)
    @GetMapping("/student/{studentId}/test")
    @ResponseBody
    public String testUser(@PathVariable String studentId) {
        User user = userService.findByStudentId(studentId);
        if (user == null) {
            return "User NOT found with studentId: " + studentId;
        }
        return "User found: " + user.getName() + ", Role: " + user.getRole() + ", Active: " + user.getIsActive();
    }
}