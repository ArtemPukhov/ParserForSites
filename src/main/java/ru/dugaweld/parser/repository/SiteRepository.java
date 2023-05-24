package ru.dugaweld.parser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dugaweld.parser.model.Site;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
}
