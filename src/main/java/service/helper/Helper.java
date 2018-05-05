package service.helper;

import repository.TokenRepository;
import repository.dao.Benutzer;
import repository.dao.Token;

import java.text.SimpleDateFormat;
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
        return benutzer != null && benutzer.getToken().getAblaufdatum() > new Date().getTime();
    }

    public static boolean checkPasswortPolicy(String passwort) {
        //Mindestens 5 Zeichen
        return passwort.length() >= 5;
    }

    public static String convertDateFromLongToZeitpunktString(long zeitstempel) {
        Date datum = new Date(zeitstempel);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return sdf.format(datum);
    }

    public static String convertDateFromLongToZeitString(long zeitstempel) {
        Date datum = new Date(zeitstempel);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(datum);
    }

    public static String convertDateFromLongToDatumString(long zeitstempel) {
        Date datum = new Date(zeitstempel);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(datum);
    }

}
