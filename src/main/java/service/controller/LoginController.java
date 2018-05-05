package service.controller;

import api.errors.Fehlermeldung;
import api.request.LoginRequest;
import api.request.TokenRequest;
import api.response.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.BenutzerRepository;
import repository.TokenRepository;
import repository.dao.Benutzer;
import repository.dao.Token;
import repository.validation.EntityValidator;
import service.helper.Helper;
import service.security.Encryption;
import service.security.TokenGenerator;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;

@RestController
@RequestMapping("/service/login")
public class LoginController {

    @Autowired
    BenutzerRepository benutzerRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    EntityValidator validator;

    @Autowired
    Encryption encryption;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request) {
        final int anzahlVersuchePasswort = 5;
        try {
            Benutzer benutzer = benutzerRepository.findBenutzerByBenutzername(request.getBenutzername());
            if (benutzer != null) {
                if (benutzer.getGesperrtBis() > new Date().getTime()) {
                    return new ResponseEntity(new Fehlermeldung("Der Account ist bis " + Helper.convertDateFromLongToZeitString(benutzer.getGesperrtBis()) + " gesperrt, weil es zu viele fehlerhafte Loginversuche gab."), HttpStatus.FORBIDDEN);
                }
                String passwortKlartext = new String(encryption.decryptRSA(Base64.getDecoder().decode(request.getPasswort())));
                if (!benutzer.getPasswort().equals(Encryption.SHAencrypt(passwortKlartext))) {
                    if (benutzer != null) {
                        benutzer.setFehlgeschlageneLoginversuche(benutzer.getFehlgeschlageneLoginversuche() + 1);
                        if (benutzer.getFehlgeschlageneLoginversuche() >= anzahlVersuchePasswort) {
                            benutzer.setGesperrtBis(new Date().getTime() + 1000 * 60); // Account für eine Minute sperren
                            benutzer.setFehlgeschlageneLoginversuche(anzahlVersuchePasswort - 1); // einen weiteren Loginversuch erlauben
                        }
                        benutzerRepository.save(benutzer);
                    }
                    return new ResponseEntity(new Fehlermeldung("Die eingegebenen Anmeldedaten sind nicht korrekt."), HttpStatus.BAD_REQUEST);
                }
                Token generierterToken = TokenGenerator.generateToken(benutzer);
                Token aktuellerToken = tokenRepository.findTokenByBenutzer_Id(benutzer.getId());
                benutzer.setFehlgeschlageneLoginversuche(0);
                benutzerRepository.save(benutzer);
                if (aktuellerToken != null) { // Token in Datenbank aktualisieren
                    aktuellerToken.setTokenContent(generierterToken.getTokenContent());
                    aktuellerToken.setAblaufdatum(generierterToken.getAblaufdatum());
                    tokenRepository.save(aktuellerToken);
                } else { // neuen Token in Datenbank anlegen
                    tokenRepository.save(generierterToken);
                }
                return new ResponseEntity(new TokenResponse(generierterToken.getTokenContent(), generierterToken.getAblaufdatum(), generierterToken.getBenutzer().getId()), HttpStatus.OK);
            } else {
                return new ResponseEntity(new Fehlermeldung("Der eingegebene Benutzername existiert nicht."), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestBody TokenRequest request) {
        Benutzer benutzer = Helper.getBenutzerByTokenContent(tokenRepository, request.getToken());
        benutzer.getToken().setAblaufdatum(0L);
        benutzer.getToken().setTokenContent("");
        try {
            benutzerRepository.save(benutzer);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getPublicKey")
    public ResponseEntity<PublicKey> getPublicKey() {
        return new ResponseEntity(encryption.getRSAPublicKey(), HttpStatus.OK);
    }

}
