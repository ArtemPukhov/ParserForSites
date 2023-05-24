package ru.dugaweld.parser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dugaweld.parser.config.SiteList;
import ru.dugaweld.parser.controllers.ProductController;
import ru.dugaweld.parser.model.Product;
import ru.dugaweld.parser.model.Site;
import ru.dugaweld.parser.repository.ProductRepository;
import ru.dugaweld.parser.repository.SiteRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    public static List<Product> products = new ArrayList<>();
    @Autowired
    private SiteList siteList;
    SiteRepository siteRepository = ProductController.getSiteRepository();
    ProductRepository productRepository = ProductController.getProductRepository();

    public void parseAndSaveProducts() {
        for(Site site : siteList.getSites()) {
            siteRepository.save(site);
            ParserService parserService = new ParserService(site);
            parserService.invoke();
        }

    }
    public List<Product> searchProduct(String query) {
        return productRepository.findByNameContaining(query);
    }
}
