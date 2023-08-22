package ru.dugaweld.parser.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter

public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String url;
    @OneToMany(mappedBy = "site")
    private List<Product> products = new ArrayList<>();
    private String selectPrice;
    private String selectName;
    private String selectSku;
    private String startWith;

}
