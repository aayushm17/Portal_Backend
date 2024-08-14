package com.example.demo3.services;

import com.example.demo3.entity.certificate;
import com.example.demo3.repository.certificateRepository;
import com.example.demo3.specification.certificateSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class certificateServices {
    @Autowired
    private certificateRepository certificateRepository;

    public certificateServices() {
    }

    public List<certificate> getcerts() {
        return certificateRepository.findAll();
    }

    public Page<certificate> getCertsByParams(LocalDateTime startDate, LocalDateTime endDate, String cn, String certname, String certtype, String reqname, String san, String validity, int page, int size) {
        Specification<certificate> spec = Specification
                .where(certificateSpecification.hasStartDate(startDate))
                .and(certificateSpecification.hasEndDate(endDate))
                .and(certificateSpecification.hasCn(cn))
                .and(certificateSpecification.hasCertname(certname))
                .and(certificateSpecification.hasCerttype(certtype))
                .and(certificateSpecification.hasReqname(reqname))
                .and(certificateSpecification.hasSan(san))
                .and(certificateSpecification.hasValidity(validity));

        Pageable pageable = PageRequest.of(page, size);
        return certificateRepository.findAll(spec, pageable);
    }
    public List<certificate> getCertsByParams(LocalDateTime startDate, LocalDateTime endDate, String cn, String certname, String certtype, String reqname, String san, String validity) {
        Specification<certificate> spec = Specification
                .where(certificateSpecification.hasStartDate(startDate))
                .and(certificateSpecification.hasEndDate(endDate))
                .and(certificateSpecification.hasCn(cn))
                .and(certificateSpecification.hasCertname(certname))
                .and(certificateSpecification.hasCerttype(certtype))
                .and(certificateSpecification.hasReqname(reqname))
                .and(certificateSpecification.hasSan(san))
                .and(certificateSpecification.hasValidity(validity));

        return certificateRepository.findAll(spec);
    }

}
