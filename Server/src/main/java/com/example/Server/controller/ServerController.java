package com.example.Server.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Server.services.certificateServices;
import com.example.Server.entity.certificate;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
@RestController
@RequestMapping("/server")
public class ServerController {

    private certificateServices certificateServices;
    private final Logger logger = LoggerFactory.getLogger(ServerController.class);
    @Autowired
    public ServerController(certificateServices certificateServices) {
        this.certificateServices = certificateServices;
    }

    @DeleteMapping("/revokeCerts")
    public ResponseEntity<?> revokeCertificate(@RequestParam String san, @RequestParam String reason) {
        try {
            boolean isRemoved = certificateServices.revokeCertificateBySan(san, reason);
            logger.info("san: {}", san);
            if (isRemoved) {
                return ResponseEntity.status(204).body("Certificate revoked successfully");
            } else {
                return ResponseEntity.status(404).body("Certificate not found");
            }
        } catch (Exception e) {
            logger.info("error: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error revoking certificate: " + e.getMessage());

        }
    }

    @GetMapping("/download-crt")
    public ResponseEntity<InputStreamResource> downloadCrt() throws IOException {
        // Load the .crt file from the static directory in resources
        ClassPathResource crtFile = new ClassPathResource("static/ca.crt");

        // Prepare response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=ca-bundle.crt");

        // Check if the file exists and is readable
        if (crtFile.exists() && crtFile.isReadable()) {
            return ResponseEntity.status(200)
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/x-x509-ca-cert"))
                    .contentLength(crtFile.contentLength())
                    .body(new InputStreamResource(crtFile.getInputStream()));
        } else {
            return ResponseEntity.status(404)
                    .body(null);
        }
    }

    @GetMapping("/download-by-san")
    public ResponseEntity<InputStreamResource> downloadBySan(@RequestParam String san) throws IOException {
        // Load the .crt file from the static directory in resources
        ClassPathResource crtFile = new ClassPathResource("static/bob.crt");

        // Prepare response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=certificate.crt");

        // Check if the file exists and is readable
        if (crtFile.exists() && crtFile.isReadable()) {
            return ResponseEntity.status(200)
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/x-x509-ca-cert"))
                    .contentLength(crtFile.contentLength())
                    .body(new InputStreamResource(crtFile.getInputStream()));
        } else {
            return ResponseEntity.status(404)
                    .body(null);
        }
    }

}