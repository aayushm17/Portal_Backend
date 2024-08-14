package com.example.Server.repository;

import com.example.Server.entity.RevokedCerts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevokedCertRepository extends JpaRepository<RevokedCerts, Long> {
}
