package com.example.Server.services;

import com.example.Server.entity.certificate;
import com.example.Server.entity.RevokedCerts;
import com.example.Server.repository.certificateRepository;
import com.example.Server.repository.RevokedCertRepository;
import com.example.Server.specification.certificateSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class certificateServices {

    private final Logger logger = LoggerFactory.getLogger(certificateServices.class);
    private final certificateRepository certificateRepository;
    @Autowired
    private RevokedCertRepository revokedCertRepository;
    @Autowired
    public certificateServices(certificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public List<certificate> getcerts() {
        return certificateRepository.findAll();
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


    public boolean revokeCertificateBySan(String san, String reason) {
        certificate certificate = certificateRepository.findBySan(san);
        if (certificate != null) {
            // Create and save the revoked certificate
            RevokedCerts revokedCert = new RevokedCerts();
            revokedCert.setCn(certificate.getCn());
            revokedCert.setCertname(certificate.getCertname());
            revokedCert.setCerttype(certificate.getCerttype());
            revokedCert.setDateissued(certificate.getDateissued());
            revokedCert.setReqname(certificate.getReqname());
            revokedCert.setValidity(certificate.getValidity());
            revokedCert.setReason(reason);
            revokedCert.setCertificate(certificate);

            logger.info("saving deleted entry in db");
            revokedCertRepository.save(revokedCert);


            // Delete the certificate from the original table
            certificateRepository.deleteBySan(san);
            return true;
        } else {
            return false;
        }
    }
    public certificate findBySan(String san) {
        return certificateRepository.findBySan(san);
    }
}

