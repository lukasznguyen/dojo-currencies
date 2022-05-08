package com.example.demo.repositiories;

import com.example.demo.models.ApplicationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationLogRepo extends JpaRepository<ApplicationLog, Long> {

}
