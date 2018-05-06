package repository;

import org.springframework.data.repository.CrudRepository;
import repository.dao.Berechtigungslevel;
import repository.enums.EnumBerechtigungslevel;

import java.util.List;

public interface BerechtigungslevelRepository extends CrudRepository<Berechtigungslevel, Long> {
    Berechtigungslevel findBerechtigungslevelByBeschreibung(EnumBerechtigungslevel beschreibung);

    List<Berechtigungslevel> findAllBy();

}
