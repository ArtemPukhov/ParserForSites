package ru.dugaweld.parser.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.dugaweld.parser.config.SiteList;
import ru.dugaweld.parser.model.Site;
import ru.dugaweld.parser.repository.ProductRepository;
import ru.dugaweld.parser.repository.SiteRepository;
import ru.dugaweld.parser.services.ProductService;
import telegram_bot.MyBot;
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

    @PostMapping("/parseOneSite")
    public ResponseEntity<Void> parseOneSite(@RequestParam("siteIndex") int siteIndex) {
        // Получите список сайтов из вашего хранилища (например, базы данных)

        // Проверьте, что индекс сайта находится в допустимом диапазоне
        if (siteIndex >= 0 && siteIndex < siteList.getSites().size()) {
            String siteUrl = siteList.getSites().get(siteIndex).getUrl();
            // Ваш код для парсинга указанного сайта
            productService.parseOneSite(siteUrl);
            return ResponseEntity.ok().build();
        } else {
            // Возвращайте ошибку, если индекс сайта недопустим
            return ResponseEntity.badRequest().build();
        }
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
