package ru.dugaweld.parser.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dugaweld.parser.model.Product;
import ru.dugaweld.parser.model.Site;
import ru.dugaweld.parser.repository.ProductRepository;
import ru.dugaweld.parser.controllers.ProductController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveAction;


@Getter
@AllArgsConstructor
public class ParserService extends RecursiveAction {
    static Set<String> urls1 = new HashSet<>();
    List<Product> products = new ArrayList<>();

    @Autowired
    ProductRepository productRepository = ProductController.getProductRepository();

    String url;
    String price;
    String sku;
    String h1;
    String description;
    Site site;

    public ParserService(Site site) {
        this.site = site;
        url = site.getUrl();
    }

    public ParserService(String url, Site site) {
        this.url = url;
        this.site = site;
    }

    @Override
    protected void compute() {
        try {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");
            List<ParserService> tasks = new ArrayList<>();
            String[] splitUrl = url.split("/");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(splitUrl[0] + "/" + splitUrl[1] + "/" + splitUrl[2]);
            String startWith = site.getStartWith();


            for (Element link : links) {
                String linkUrl = link.absUrl("href");
                if (linkUrl.startsWith(startWith) &&
                        !urls1.contains(linkUrl) &&
                        !linkUrl.contains("#") &&
                        !urls1.contains(linkUrl) &&
                        !linkUrl.endsWith("jpg") &&
                        !linkUrl.endsWith("png") &&
                        !linkUrl.endsWith("jpeg") &&
                        !linkUrl.endsWith("JPG") &&
                        !linkUrl.endsWith("pdf") &&
                        !linkUrl.endsWith("doc")) {

                    if (linkUrl.startsWith(startWith)) {
                        Document docPrice = Jsoup.connect(linkUrl).get();
                        Elements priceElements = docPrice.select(site.getSelectPrice());
                        Elements h1Elements = docPrice.select(site.getSelectName());
                        Elements skuElements = docPrice.select(site.getSelectSku());
                        Elements discriptionElements = docPrice.select("div.field:nth-child(4)");
                        if (!(priceElements.size() == 0)) {
                            price = priceElements.get(0).text().replaceAll("[^0-9]", "");
                            if(!(skuElements.size() == 0)) {
                                sku = skuElements.get(0).text().replaceAll("[^0-9]", "");
                            }
                            if(!(h1Elements.size() == 0)) {
                                h1 = h1Elements.get(0).text();
                            }
                            Product product = new Product(linkUrl, price, sku, h1, site);
                            if (productRepository.findByName(product.getName()) == null) {
                                products.add(product);
                                ProductService.products.add(product);
                                if (ProductService.products.size() > 50) {
                                    productRepository.saveAll(ProductService.products);
                                    ProductService.products.clear();
                                }
                                System.out.println(product);
                            }

                        }
                    }
                    if(urls1.contains(linkUrl)) {
                        continue;
                    }
                    urls1.add(linkUrl);

                    ParserService task = new ParserService(linkUrl, site);
                    tasks.add(task);

                }
            }
            // Start all forked tasks
            invokeAll(tasks);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
