package com.example.disasterManagement.repository;

import com.example.disasterManagement.model.AssistanceRequest;
import com.example.disasterManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssistanceRequestRepository extends JpaRepository<AssistanceRequest, Long> {
    List<AssistanceRequest> findByUser(User user);
    List<AssistanceRequest> findByStatus(String status);
}