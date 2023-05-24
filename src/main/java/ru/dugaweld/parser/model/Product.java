package ru.dugaweld.parser.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String price;
    private String sku;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    public Product() {
    }

    public Product(String url, String price, String sku, String name, Site site) {
        this.url = url;
        this.price = price;
        this.sku = sku;
        this.name = name;
        this.site = site;
    }


}
