package repository;

import org.springframework.data.repository.CrudRepository;
import repository.dao.Benutzer;
import repository.dao.Token;

public interface BenutzerRepository extends CrudRepository<Benutzer, Long> {
    Benutzer findBenutzerById(long id);
    Benutzer findBenutzerByBenutzername(String benutzername);
    Benutzer findBenutzerByToken_TokenContent(String tokenContent);
}
