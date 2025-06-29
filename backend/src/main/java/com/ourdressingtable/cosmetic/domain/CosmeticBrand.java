package com.ourdressingtable.cosmetic.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cosmetic_brands")
public class CosmeticBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cosmetic_brand_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "homepage_url")
    private String homepageUrl;

    @Lob
    private String description;

    @Builder
    public CosmeticBrand(Long id, String name, String logoUrl, String homepageUrl, String description) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.homepageUrl = homepageUrl;
        this.description = description;

    }
}
