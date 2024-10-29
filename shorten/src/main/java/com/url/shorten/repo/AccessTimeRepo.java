package com.url.shorten.repo;

import com.url.shorten.dto.AccessTime;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AccessTimeRepo extends JpaRepository<AccessTime, Long> {
}
