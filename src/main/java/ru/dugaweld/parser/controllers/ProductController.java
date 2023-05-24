package ru.dugaweld.parser.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.dugaweld.parser.config.SiteList;
import ru.dugaweld.parser.model.Site;
import ru.dugaweld.parser.repository.ProductRepository;
import ru.dugaweld.parser.repository.SiteRepository;
import ru.dugaweld.parser.services.ProductService;
import telegram_bot.MyBot;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    static public ProductRepository productRepository;
    static public SiteRepository siteRepository;
    @Autowired
    static public SiteList siteList;

    public ProductController(ProductRepository productRepository, SiteRepository siteRepository, SiteList siteList) {
        ProductController.productRepository = productRepository;
        ProductController.siteRepository = siteRepository;
        ProductController.siteList = siteList;
    }

    public static ProductRepository getProductRepository() {
        return productRepository;
    }

    public static SiteRepository getSiteRepository() {
        return siteRepository;
    }

    @Autowired
    private ProductService productService;

    @PostMapping("/parse")
    public ResponseEntity<Void> parseAndSaveProducts() {
        productService.parseAndSaveProducts();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/startBot")
    public ResponseEntity<Void> startBot() {
        MyBot bot = new MyBot();
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sites")
    public List<Site> getSiteList() {
       return siteList.getSites();
    }
}
