package com.example.demo3.controller;

import com.example.demo3.dto.JwtRequest;
import com.example.demo3.dto.JwtResponse;
import com.itextpdf.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo3.services.certificateServices;
import com.example.demo3.entity.certificate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.*;
import org.springframework.core.io.InputStreamResource;
import com.example.demo3.services.AuthService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.core.EmbeddedWrappers;


import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;
import com.example.demo3.services.JwtUtil;
import com.example.demo3.services.SubscriberService;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/certificates")
@CrossOrigin(origins = "*")
public class CertificateController {
    @Autowired
    private certificateServices certificateServices;
    @Autowired
    private SubscriberService subscriberService;

    private final Logger logger = LoggerFactory.getLogger(CertificateController.class);

    private boolean isValidToken(String token) {
        try {
            ResponseEntity<Map<String, Object>> response = authService.validate(token);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("find")
    public List<certificate> getcerts() {
        return certificateServices.getcerts();
    }

    @GetMapping("/certs")
    public ResponseEntity<PagedModel<EntityModel<certificate>>> searchCerts(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String start_date,
            @RequestParam(required = false) String end_date,
            @RequestParam(required = false) String cn,
            @RequestParam(required = false) String certname,
            @RequestParam(required = false) String certtype,
            @RequestParam(required = false) String reqname,
            @RequestParam(required = false) String san,
            @RequestParam(required = false) String validity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        String jwtToken = token != null && token.startsWith("Bearer ") ? token.substring(7) : null;

        if (jwtToken == null || !isValidToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        LocalDateTime start = start_date != null && !start_date.isEmpty()
                ? ZonedDateTime.parse(start_date, formatter).toLocalDateTime()
                : null;
        LocalDateTime end = end_date != null && !end_date.isEmpty()
                ? ZonedDateTime.parse(end_date, formatter).toLocalDateTime()
                : null;

        Page<certificate> certificatePage = certificateServices.getCertsByParams(start, end, cn, certname, certtype, reqname, san, validity, page, size);

        if (certificatePage == null || !certificatePage.hasContent()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        PagedModel<EntityModel<certificate>> pagedModel = toPagedModel(certificatePage, token, start_date, end_date, cn, certname, certtype, reqname, san, validity);

        return ResponseEntity.ok(pagedModel);
    }




    private PagedModel<EntityModel<certificate>> toPagedModel(Page<certificate> certificatePage, String token, String start_date, String end_date, String cn, String certname, String certtype, String reqname, String san, String validity) {
        List<EntityModel<certificate>> certificateResources = certificatePage.getContent().stream()
                .map(certificate -> EntityModel.of(certificate))
                .collect(Collectors.toList());

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                certificatePage.getSize(),
                certificatePage.getNumber(),
                certificatePage.getTotalElements(),
                certificatePage.getTotalPages()
        );

        PagedModel<EntityModel<certificate>> pagedModel = PagedModel.of(certificateResources, metadata);

        if (certificatePage.hasPrevious()) {
            pagedModel.add(WebMvcLinkBuilder.linkTo(
                            methodOn(this.getClass())
                                    .searchCerts(token, start_date, end_date, cn, certname, certtype, reqname, san, validity, certificatePage.getNumber() - 1, certificatePage.getSize()))
                    .withRel("previousPage"));
        }

        if (certificatePage.hasNext()) {
            pagedModel.add(WebMvcLinkBuilder.linkTo(
                            methodOn(this.getClass())
                                    .searchCerts(token, start_date, end_date, cn, certname, certtype, reqname, san, validity, certificatePage.getNumber() + 1, certificatePage.getSize()))
                    .withRel("nextPage"));
        }

        return pagedModel;
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<?> getSubscriptionsOrSans(
            @RequestParam(required = false) String subscriberId,
            @RequestParam(required = false) String subscriptionId) {

        if (subscriberId == null && subscriptionId == null) {
            return ResponseEntity.badRequest().body("Either subscriberId or subscriptionId must be provided.");
        }

        if (subscriberId != null) {
            Set<String> subscriptionIds = subscriberService.getSubscriptionIdsBySubscriber(subscriberId);
            if (subscriptionIds == null || subscriptionIds.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.ok(subscriptionIds);
        } else {
            List<String> sans = subscriberService.getSansBySubscription(subscriptionId);
            if (sans == null || sans.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.ok(sans);
        }
    }




    @GetMapping("/cert-list-pdf")
    public ResponseEntity<InputStreamResource> downloadPdf(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "start_date", required = false) String start_date,
            @RequestParam(value = "end_date", required = false) String end_date,
            @RequestParam(value = "cn", required = false) String cn,
            @RequestParam(value = "certname", required = false) String certname,
            @RequestParam(value = "certtype", required = false) String certtype,
            @RequestParam(value = "reqname", required = false) String reqname,
            @RequestParam(value = "san", required = false) String san,
            @RequestParam(value = "validity", required = false) String validity) throws IOException, DocumentException {

        String jwtToken = token != null && token.startsWith("Bearer ") ? token.substring(7) : null;

        if (jwtToken == null || !isValidToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new InputStreamResource(new ByteArrayInputStream("Unauthorized".getBytes())));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        LocalDateTime start = parseDate(start_date, formatter);
        LocalDateTime end = parseDate(end_date, formatter);

        List<certificate> certificates = certificateServices.getCertsByParams(start, end, cn, certname, certtype, reqname, san, validity);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable table = createPdfTable(certificates);
        document.add(table);
        document.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificates.pdf")
                .body(new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())));
    }

    private LocalDateTime parseDate(String date, DateTimeFormatter formatter) {
        return date != null && !date.isEmpty() ? ZonedDateTime.parse(date, formatter).toLocalDateTime() : null;
    }

    private PdfPTable createPdfTable(List<certificate> certificates) throws DocumentException {
        //String[] headers = {"CN", "CertName", "CertType", "DateIssued", "ReqName", "SAN", "Validity"};
        String[] headers = {"Certificate Name", "Creation Date Time", "Request Name", "SAN", "Expiration Date Time"};
        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 2, 2, 2, 2});

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        for (String header : headers) {
            table.addCell(createHeaderCell(header, headFont));
        }

        for (certificate cert : certificates) {
            //table.addCell(createBodyCell(cert.getCn()));
            table.addCell(createBodyCell(cert.getCertname()));
            //table.addCell(createBodyCell(cert.getCerttype()));
            table.addCell(createBodyCell(cert.getDateissued().toString()));
            table.addCell(createBodyCell(cert.getReqname()));
            table.addCell(createBodyCell(cert.getSan()));
            table.addCell(createBodyCell(cert.getValidity()));
        }

        return table;
    }

    private PdfPCell createHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private PdfPCell createBodyCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(5);
        return cell;
    }



    @GetMapping("/cert-list-csv")
    public ResponseEntity<InputStreamResource> downloadCsv(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "start_date", required = false) String start_date,
            @RequestParam(value = "end_date", required = false) String end_date,
            @RequestParam(value = "cn", required = false) String cn,
            @RequestParam(value = "certname", required = false) String certname,
            @RequestParam(value = "certtype", required = false) String certtype,
            @RequestParam(value = "reqname", required = false) String reqname,
            @RequestParam(value = "san", required = false) String san,
            @RequestParam(value = "validity", required = false) String validity) throws IOException {

        String jwtToken = token != null && token.startsWith("Bearer ") ? token.substring(7) : null;

        if (jwtToken == null || !isValidToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new InputStreamResource(new ByteArrayInputStream("Unauthorized".getBytes())));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        LocalDateTime start = parseDate(start_date, formatter);
        LocalDateTime end = parseDate(end_date, formatter);

        List<certificate> certificates = certificateServices.getCertsByParams(start, end, cn, certname, certtype, reqname, san, validity);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Certificates");

        Row header = sheet.createRow(0);
       //header.createCell(1).setCellValue("CN");
        header.createCell(2).setCellValue("Certificate Name");
        //header.createCell(3).setCellValue("CertType");
        header.createCell(4).setCellValue("Creation Date Time");
        header.createCell(5).setCellValue("Request Name");
        header.createCell(6).setCellValue("SAN");
        header.createCell(7).setCellValue("Expiration Date Time");

        int rowNum = 1;
        for (certificate cert : certificates) {
            Row row = sheet.createRow(rowNum++);
            //row.createCell(1).setCellValue(cert.getCn());
            row.createCell(2).setCellValue(cert.getCertname());
            //row.createCell(3).setCellValue(cert.getCerttype());
            row.createCell(4).setCellValue(cert.getDateissued().toString());
            row.createCell(5).setCellValue(cert.getReqname());
            row.createCell(6).setCellValue(cert.getSan());
            row.createCell(7).setCellValue(cert.getValidity());
        }

        workbook.write(outputStream);
        workbook.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificates.csv")
                .body(new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())));
    }
    @Value("${crt.api.url}")
    private String crtApiUrl;

    private final RestTemplate restTemplate;

    public CertificateController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }

    @GetMapping("/ca-bundle")
    public ResponseEntity<?> fetchCrt(@RequestHeader("Authorization") String token) throws IOException {
        String jwtToken = token != null && token.startsWith("Bearer ") ? token.substring(7) : null;

        if (jwtToken == null || !isValidToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        ResponseEntity<byte[]> response = restTemplate.exchange(
                crtApiUrl, // Update the URL if needed
                HttpMethod.GET,
                null,
                byte[].class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=ca-cert-bundle.crt");

            ByteArrayInputStream bis = new ByteArrayInputStream(response.getBody());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(response.getBody().length)
                    .contentType(MediaType.parseMediaType("application/x-x509-ca-cert"))
                    .body(new InputStreamResource(bis));
        } else if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("204");
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(String.valueOf(response.getStatusCode().value()));
        }
    }


    @Value("${revoke.api.url}")
    private String revokeApiUrl;

    @DeleteMapping("/certs")
    public ResponseEntity<String> callRevokeCertificate(
            @RequestParam String san,
            @RequestParam String reason,
            @RequestHeader("Authorization") String token) {

        logger.info("san: {}", san);
        logger.info("reason: {}", reason);
        logger.info("token: {}", token);

        // Extract and validate JWT token
        String jwtToken = token != null && token.startsWith("Bearer ") ? token.substring(7) : null;
        if (jwtToken == null || !isValidToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            // Build the URI with query parameters
            URI uri = UriComponentsBuilder.fromHttpUrl(revokeApiUrl)
                    .queryParam("san", san)
                    .queryParam("reason", reason)
                    .build()
                    .toUri();

            logger.info("Calling revoke API at URI: {}", uri);

//            // Prepare headers
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + jwtToken);

            // Create a request entity with the URI
            RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.DELETE, uri);

            // Call the revoke API
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
            logger.info("Status Code: {}", response.getStatusCode());
            logger.info("Response Body: {}", response.getBody());

            // Return the response from the revoke API
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpStatusCodeException e) {
            logger.error("HttpStatusCodeException occurred", e);
            return ResponseEntity.status(e.getStatusCode()).body("Error calling revoke API: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Exception occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error calling revoke API: " + e.getMessage());
        }
    }






    @Value("${api.url}")
    private String apiUrl;

    @GetMapping("/download-certs")
    public ResponseEntity<InputStreamResource> callCertBySan(
            @RequestParam String san,
            @RequestHeader("Authorization") String token) throws IOException {

        // Extract and validate JWT token
        String jwtToken = token != null && token.startsWith("Bearer ") ? token.substring(7) : null;
        if (jwtToken == null || !isValidToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Build the URL for the API call
        String url = apiUrl + san;
        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                byte[].class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=CertBySan.crt");

            ByteArrayInputStream bis = new ByteArrayInputStream(response.getBody());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(response.getBody().length)
                    .contentType(MediaType.parseMediaType("application/x-x509-ca-cert"))
                    .body(new InputStreamResource(bis));
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(null);
        }
    }



    @Autowired
       private AuthService authService;



    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String authorization,
                                   @RequestParam(value = "sessionId") String sessionId) {
        // Decode and extract username and secretKey from Basic Auth
        String[] credentials = extractCredentials(authorization);
        String username = credentials[0];
        String secretKey = credentials[1];

        logger.info("authorization: {}", authorization);
        logger.info("sessionId: {}", sessionId);

        // Dummy user validation
        if (("user".equals(username) && "userSecret".equals(secretKey)) ||
                ("admin".equals(username) && "adminSecret".equals(secretKey))) {
            // If credentials are valid, generate the token
            String token = authService.generateToken(sessionId, username, secretKey);
            JwtResponse response = new JwtResponse(token, username);
            logger.info(response.getJwtToken());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid Username, SecretKey, or SessionId");
            logger.error(errorResponse.toString());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }


    private String[] extractCredentials(String authorization) {
        if (authorization != null && authorization.startsWith("Basic ")) {
            String base64Credentials = authorization.substring(6);
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            return credentials.split(":", 2);
        }
        throw new RuntimeException("Invalid Authorization header");
    }


    private boolean validateSessionId(String sessionId) {
        // Dummy validation logic for session ID
        return "validSessionId".equals(sessionId);
    }

       // Validate token endpoint
       @GetMapping("/validate")
       public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
           String jwtToken = token.substring(7); // Remove "Bearer " prefix
           ResponseEntity<Map<String, Object>> response = authService.validate(jwtToken);

           if (response.getStatusCode().is2xxSuccessful()) {
               return ResponseEntity.ok(response.getBody());
           } else {
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid Token"));
           }
       }

       // Invalidate token endpoint
       @PostMapping("/logout")
       public ResponseEntity<?> invalidateToken(@RequestHeader("Authorization") String token) {
           String jwtToken = token.substring(7); // Remove "Bearer " prefix
           ResponseEntity<String> response = authService.invalidate(jwtToken);

           if (response.getStatusCode().is2xxSuccessful()) {
               return ResponseEntity.ok(Map.of("message", "Token invalidated"));
           } else {
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid Token"));
           }
       }
   }
