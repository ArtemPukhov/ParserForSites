package ru.dugaweld.parser.services;

import org.springframework.stereotype.Service;
import ru.dugaweld.parser.config.SiteList;
import ru.dugaweld.parser.repository.SiteRepository;

@Service
public class SiteService {
    private final SiteRepository siteRepository;
    SiteList sites;

    public SiteService(SiteRepository siteRepository, SiteList sites) {
        this.siteRepository = siteRepository;
        this.sites = sites;
    }



}