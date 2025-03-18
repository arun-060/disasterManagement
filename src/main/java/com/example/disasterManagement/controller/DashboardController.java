package com.example.disasterManagement.controller;

import com.example.disasterManagement.model.ERole;
import com.example.disasterManagement.security.services.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
// @RequestMapping("/admin")
public class DashboardController {

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
    public String adminDashboard() {
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
    public String affectedPersonMyRequests() {
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
    
} 