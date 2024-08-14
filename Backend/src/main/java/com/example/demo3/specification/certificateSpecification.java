package com.example.demo3.specification;

import com.example.demo3.entity.certificate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class certificateSpecification {

    public static Specification<certificate> hasStartDate(LocalDateTime startDate) {
        return (root, query, criteriaBuilder) ->
                startDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.greaterThanOrEqualTo(root.get("dateissued"), startDate);
    }

    public static Specification<certificate> hasEndDate(LocalDateTime endDate) {
        return (root, query, criteriaBuilder) ->
                endDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.lessThanOrEqualTo(root.get("dateissued"), endDate);
    }


    public static Specification<certificate> hasCn(String cn) {
        return (root, query, criteriaBuilder) ->
                cn == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("cn")), "%" + cn.toLowerCase() + "%");
    }

    public static Specification<certificate> hasCertname(String certname) {
        return (root, query, criteriaBuilder) ->
                certname == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("certname")), "%" + certname.toLowerCase() + "%");
    }

    public static Specification<certificate> hasCerttype(String certtype) {
        return (root, query, criteriaBuilder) ->
                certtype == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("certtype")), "%" + certtype.toLowerCase() + "%");
    }

    public static Specification<certificate> hasReqname(String reqname) {
        return (root, query, criteriaBuilder) ->
                reqname == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("reqname")), "%" + reqname.toLowerCase() + "%");
    }

    public static Specification<certificate> hasSan(String san) {
        return (root, query, criteriaBuilder) ->
                san == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("san")), "%" + san.toLowerCase() + "%");
    }

    public static Specification<certificate> hasValidity(String validity) {
        return (root, query, criteriaBuilder) ->
                validity == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("validity")), "%" + validity.toLowerCase() + "%");
    }
}
