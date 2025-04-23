package com.example.disasterManagement.controller;

import com.example.disasterManagement.model.AssistanceRequest;
import com.example.disasterManagement.model.User;
import com.example.disasterManagement.repository.AssistanceRequestRepository;
import com.example.disasterManagement.repository.UserRepository;
import com.example.disasterManagement.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashboardController {

    @Autowired
    private AssistanceRequestRepository assistanceRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public ModelAndView dashboard(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ModelAndView("redirect:/login");
        }
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        
        ModelAndView modelAndView = new ModelAndView();
        
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            modelAndView.setViewName("redirect:/admin/dashboard");
        } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_DONOR"))) {
            modelAndView.setViewName("redirect:/donor/dashboard");
        } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_VOLUNTEER"))) {
            modelAndView.setViewName("redirect:/volunteer/dashboard");
        } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_AFFECTED_PERSON"))) {
            modelAndView.setViewName("redirect:/affected/dashboard");
        } else {
            modelAndView.setViewName("redirect:/");
        }
        
        return modelAndView;
    }
    
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Model model) {
        List<AssistanceRequest> allRequests = assistanceRequestRepository.findAll();
        List<AssistanceRequest> pendingRequests = assistanceRequestRepository.findByStatus("PENDING");
        model.addAttribute("allRequests", allRequests);
        model.addAttribute("pendingRequestsCount", pendingRequests.size());
        return "admin/dashboard";
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminUsers() {
        return "admin/users";
    }

    @GetMapping("/admin/disasters")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDisasters() {
        return "admin/disasters";
    }

    @GetMapping("/admin/relief-efforts")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminReliefEfforts() {
        return "admin/relief-efforts";
    }

    @GetMapping("/admin/donations")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDonations() {
        return "admin/donations";
    }
    
    @GetMapping("/admin/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminReports() {
        return "admin/reports";
    }
    
    @GetMapping("/admin/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminSettings() {
        return "admin/settings";
    }
    
    @PostMapping("/admin/accept-request")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> acceptRequest(@RequestBody Map<String, Object> payload) {
        Long requestId = Long.parseLong(payload.get("requestId").toString());
        
        AssistanceRequest request = assistanceRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        request.setStatus("ASSIGNED"); // This will make it appear in volunteer tasks
        assistanceRequestRepository.save(request);
        
        return ResponseEntity.ok(Map.of("message", "Request accepted and assigned successfully"));
    }

    @PostMapping("/admin/reject-request")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> rejectRequest(@RequestBody Map<String, Object> payload) {
        Long requestId = Long.parseLong(payload.get("requestId").toString());
        
        AssistanceRequest request = assistanceRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        request.setStatus("REJECTED");
        assistanceRequestRepository.save(request);
        
        return ResponseEntity.ok(Map.of("message", "Request rejected successfully"));
    }
    
    @GetMapping("/donor/dashboard")
    @PreAuthorize("hasRole('DONOR')")
    public String donorDashboard() {
        return "donor/dashboard";
    }

    @GetMapping("/donor/make-donation")
    @PreAuthorize("hasRole('DONOR')")
    public String donorMakeDonation() {
        return "donor/make-donation";
    }
    
    @GetMapping("/donor/donation-history")
    @PreAuthorize("hasRole('DONOR')")
    public String donorDonationHistory() {
        return "donor/donation-history";
    }

    @GetMapping("/donor/active-disasters")
    @PreAuthorize("hasRole('DONOR')")
    public String donorActiveDisasters() {
        return "donor/active-disasters";
    }

    @GetMapping("/donor/impact-reports")
    @PreAuthorize("hasRole('DONOR')")
    public String donorImpactReports() {
        return "donor/impact-reports";
    }

    @GetMapping("/donor/profile")
    @PreAuthorize("hasRole('DONOR')")
    public String donorProfile() {
        return "donor/profile";
    }
    
    @GetMapping("/volunteer/dashboard")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public String volunteerDashboard() {
        return "volunteer/dashboard";
    }

    @GetMapping("/volunteer/my-tasks")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public String volunteerMyTasks(Model model) {
        // Get requests that have been accepted/assigned to volunteers
        List<AssistanceRequest> acceptedRequests = assistanceRequestRepository.findByStatus("ASSIGNED");
        model.addAttribute("acceptedRequests", acceptedRequests);
        return "volunteer/my-tasks";
    }

    @PostMapping("/volunteer/update-task-status")
    @PreAuthorize("hasRole('VOLUNTEER')")
    @ResponseBody
    public ResponseEntity<?> updateTaskStatus(@RequestBody Map<String, Object> payload) {
        Long requestId = Long.parseLong(payload.get("requestId").toString());
        String newStatus = payload.get("status").toString();
        
        AssistanceRequest request = assistanceRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        request.setStatus(newStatus);
        assistanceRequestRepository.save(request);
        
        return ResponseEntity.ok(Map.of("message", "Task status updated successfully"));
    }
    
    @GetMapping("/volunteer/schedule")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public String volunteerSchedule() {
        return "volunteer/schedule";
    }

    @GetMapping("/volunteer/active-disasters")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public String volunteerActiveDisasters() {
        return "volunteer/active-disasters";
    }

    @GetMapping("/volunteer/skills-training")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public String volunteerSkillsTraining() {
        return "volunteer/skills-training";
    }

    @GetMapping("/volunteer/activity-log")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public String volunteerActivityLog() {
        return "volunteer/activity-log";
    }

    @GetMapping("/volunteer/profile")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public String volunteerProfile() {
        return "volunteer/profile";
    }

    @GetMapping("/affected/dashboard")
    @PreAuthorize("hasRole('AFFECTED_PERSON')")
    public String affectedPersonDashboard() {
        return "affected/dashboard";
    }

    @GetMapping("/affected/request-assistance")
    @PreAuthorize("hasRole('AFFECTED_PERSON')")
    public String affectedPersonRequestAssistance() {
        return "affected/request-assistance";
    }

    @GetMapping("/affected/my-requests")
    @PreAuthorize("hasRole('AFFECTED_PERSON')")
    public String affectedPersonMyRequests(Model model, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
        
        List<AssistanceRequest> userRequests = assistanceRequestRepository.findByUser(user);
        
        // Count requests by status
        long pendingCount = userRequests.stream().filter(r -> "PENDING".equals(r.getStatus())).count();
        long inProgressCount = userRequests.stream().filter(r -> "IN_PROGRESS".equals(r.getStatus())).count();
        long completedCount = userRequests.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count();
        long cancelledCount = userRequests.stream().filter(r -> "CANCELLED".equals(r.getStatus())).count();

        model.addAttribute("requests", userRequests);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("inProgressCount", inProgressCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("cancelledCount", cancelledCount);
        
        return "affected/my-requests";
    }

    @GetMapping("/affected/relief-centers")
    @PreAuthorize("hasRole('AFFECTED_PERSON')")
    public String affectedPersonReliefCenters() {
        return "affected/relief-centers";
    }
    
    @GetMapping("/affected/alerts")
    @PreAuthorize("hasRole('AFFECTED_PERSON')")
    public String affectedPersonAlerts() {
        return "affected/alerts";
    }
    
    @GetMapping("/affected/community")
    @PreAuthorize("hasRole('AFFECTED_PERSON')")
    public String affectedPersonCommunity() {
        return "affected/community";
    }

    @GetMapping("/affected/profile")
    @PreAuthorize("hasRole('AFFECTED_PERSON')")
    public String affectedPersonProfile() {
        return "affected/profile";
    }

    public static class RequestData {
        public String requestType;
        public String urgency;
        public String description;
        public String location;
        public String contact;
        public boolean urgent;

        @Override
        public String toString() {
            return "RequestData{" +
                    "requestType='" + requestType + '\'' +
                    ", urgency='" + urgency + '\'' +
                    ", description='" + description + '\'' +
                    ", location='" + location + '\'' +
                    ", contact='" + contact + '\'' +
                    ", urgent=" + urgent +
                    '}';
        }
    }

    @PostMapping("/affected/submit-request")
    @PreAuthorize("hasRole('AFFECTED_PERSON')")
    public ResponseEntity<?> submitRequest(@RequestBody RequestData requestData) {
        return ResponseEntity.ok(Map.of("status", "success", "message", "Request submitted successfully"));

    }

    @PostMapping("/affected/request-assistance-submit")
    @PreAuthorize("hasRole('AFFECTED_PERSON')")
    @ResponseBody
    public ResponseEntity<String> requestAssistance(@RequestBody RequestData data, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        AssistanceRequest request = new AssistanceRequest();
        request.setUser(user);
        request.setRequestType(data.requestType);
        request.setUrgency(data.urgency);
        request.setDescription(data.description);
        request.setLocation(data.location);
        request.setContact(data.contact);
        request.setUrgent(data.urgent);
        request.setStatus("PENDING"); // Set initial status
        
        assistanceRequestRepository.save(request);
        
        return ResponseEntity.ok("Assistance request submitted successfully.");
    }
}
