package service.helper;

import repository.BerechtigungslevelRepository;
import repository.TokenRepository;
import repository.dao.Benutzer;
import repository.dao.Berechtigungslevel;
import repository.dao.Token;
import repository.enums.EnumBerechtigungslevel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    /**
     * Fügt alle noch nicht vorhandenen Berechtigungslevel in die Datenbank ein.
     *
     * @param berechtigungslevelRepository wird benötigt, da ansonsten Zugriff in Helper möglich
     */
    public static void insertBerechtigungslevelIfNotExist(BerechtigungslevelRepository berechtigungslevelRepository) {
        List<Berechtigungslevel> alleBerechrichtungslevel = berechtigungslevelRepository.findAllBy();
        List<String> alleBeschreibungen = new ArrayList<>();
        for (Berechtigungslevel berechtigungslevel : alleBerechrichtungslevel) {
            alleBeschreibungen.add(berechtigungslevel.getBeschreibung().name());
        }

        if (!alleBeschreibungen.contains(EnumBerechtigungslevel.KUNDE.name())) {
            Berechtigungslevel bl = new Berechtigungslevel();
            bl.setBeschreibung(EnumBerechtigungslevel.KUNDE);
            berechtigungslevelRepository.save(bl);
        }
        if (!alleBeschreibungen.contains(EnumBerechtigungslevel.MITARBEITER.name())) {
            Berechtigungslevel bl = new Berechtigungslevel();
            bl.setBeschreibung(EnumBerechtigungslevel.MITARBEITER);
            berechtigungslevelRepository.save(bl);
        }
        if (!alleBeschreibungen.contains(EnumBerechtigungslevel.ADMINISTRATOR.name())) {
            Berechtigungslevel bl = new Berechtigungslevel();
            bl.setBeschreibung(EnumBerechtigungslevel.ADMINISTRATOR);
            berechtigungslevelRepository.save(bl);
        }
    }

}
