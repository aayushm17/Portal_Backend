package com.example.demo3.repository;

import com.example.demo3.entity.certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface certificateRepository extends JpaRepository<certificate, Long>, JpaSpecificationExecutor<certificate> {

}
