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
import service.security.Encryption;
import service.security.TokenGenerator;
import service.helper.Helper;

import java.security.PublicKey;
import java.util.Base64;

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
        try {
            Benutzer benutzer = benutzerRepository.findBenutzerByBenutzername(request.getBenutzername());

            String passwortKlartext = new String(encryption.decryptRSA(Base64.getDecoder().decode(request.getPasswort())));
            if (benutzer == null || !benutzer.getPasswort().equals(Encryption.SHAencrypt(passwortKlartext))) {
                return new ResponseEntity(new Fehlermeldung("Die eingegebenen Anmeldedaten sind nicht korrekt."), HttpStatus.BAD_REQUEST);
            }
            Token generierterToken = TokenGenerator.generateToken(benutzer);
            Token aktuellerToken = tokenRepository.findTokenByBenutzer_Id(benutzer.getId());
            if (aktuellerToken != null) { // Token in Datenbank aktualisieren
                aktuellerToken.setTokenContent(generierterToken.getTokenContent());
                aktuellerToken.setAblaufdatum(generierterToken.getAblaufdatum());
                tokenRepository.save(aktuellerToken);
            } else { // neuen Token in Datenbank anlegen
                tokenRepository.save(generierterToken);
            }
            return new ResponseEntity(new TokenResponse(generierterToken.getTokenContent(), generierterToken.getAblaufdatum(), generierterToken.getBenutzer().getId()), HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestBody TokenRequest request) {
        Benutzer benutzer = Helper.getBenutzerByTokenContent(tokenRepository, request.getToken());
        benutzer.getToken().setAblaufdatum(0L);
        benutzer.getToken().setTokenContent("");
        try
        {
            benutzerRepository.save(benutzer);
            return new ResponseEntity(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getPublicKey")
    public ResponseEntity<PublicKey> getPublicKey() {
        return new ResponseEntity(encryption.getRSAPublicKey(), HttpStatus.BAD_REQUEST.OK);
    }

}
