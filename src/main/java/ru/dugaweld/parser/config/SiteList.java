package ru.dugaweld.parser.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.dugaweld.parser.model.Site;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "indexing-settings")
public class SiteList {
    private List<Site> sites;
}
