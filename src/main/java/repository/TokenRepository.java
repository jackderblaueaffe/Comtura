package repository;

import org.springframework.data.repository.CrudRepository;
import repository.dao.Benutzer;
import repository.dao.Token;

public interface TokenRepository extends CrudRepository<Token, Long> {

    Token findTokenById(long id);
    Token findTokenByTokenContent(String tokenContent);
    Token findTokenByBenutzer_Id(long id);
    Token findTokenByBenutzer_Benutzername(String benutzername);

}
