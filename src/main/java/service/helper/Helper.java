package service.helper;

import repository.TokenRepository;
import repository.dao.Benutzer;
import repository.dao.Token;

import java.util.Date;

public class Helper {

    public static Benutzer getBenutzerByTokenContent(TokenRepository tokenRepository, String tokenContent) {
        Token token = tokenRepository.findTokenByTokenContent(tokenContent);
        if(token != null && token.getAblaufdatum() > new Date().getTime()) {
            return token.getBenutzer();
        } else {
            //kein Token gefunden oder Token abgelaufen
            return null;
        }

    }

    public static boolean isTokenValid(TokenRepository tokenRepository, String tokenContent) {
        Benutzer benutzer = getBenutzerByTokenContent(tokenRepository, tokenContent);
        if(benutzer != null && benutzer.getToken().getAblaufdatum() > new Date().getTime()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPasswortPolicy(String passwort) {
        //Mindestens 5 Zeichen
        if(passwort.length() >= 5) {
            return true;
        }
        return false;
    }

}
