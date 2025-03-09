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

import java.util.Collection;

@Controller
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
    
    @GetMapping("/donor/dashboard")
    @PreAuthorize("hasRole('DONOR')")
    public String donorDashboard() {
        return "donor/dashboard";
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
} 