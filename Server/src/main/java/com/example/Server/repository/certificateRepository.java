package com.example.Server.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import com.example.Server.entity.certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface certificateRepository extends JpaRepository<certificate, Long>, JpaSpecificationExecutor<certificate> {
    @Transactional
    @Modifying
    @Query("DELETE FROM certificate c WHERE lower(c.san) = lower(:san)")
    int deleteBySan(@Param("san") String san);
    certificate findBySan(String san);
}
