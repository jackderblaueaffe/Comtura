package service.controller;

import api.errors.Fehlermeldung;
import api.request.LoginRequest;
import api.response.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.BenutzerRepository;
import repository.TokenRepository;
import repository.dao.Benutzer;
import repository.dao.Token;
import repository.validation.EntityValidator;
import service.security.TokenGenerator;

@RestController
@RequestMapping("/service/login")
public class LoginController {

    @Autowired
    BenutzerRepository benutzerRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    EntityValidator validator;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        Benutzer benutzer = benutzerRepository.findBenutzerByBenutzername(loginRequest.getBenutzername());
        String passwort = loginRequest.getPasswort();
        if(benutzer == null || !benutzer.getPasswort().equals(passwort)) {
            return new ResponseEntity(new Fehlermeldung("Die eingegebenen Anmeldedaten sind nicht korrekt."), HttpStatus.BAD_REQUEST);
        }
        Token generierterToken = TokenGenerator.generateToken(benutzer);
        Token aktuellerToken = tokenRepository.findTokenByBenutzer_Id(benutzer.getId());
        if(aktuellerToken != null) { // Token in Datenbank aktualisieren
            aktuellerToken.setTokenContent(generierterToken.getTokenContent());
            aktuellerToken.setAblaufdatum(generierterToken.getAblaufdatum());
            tokenRepository.save(aktuellerToken);
        } else { // neuen Token in Datenbank anlegen
            tokenRepository.save(generierterToken);
        }

        return new ResponseEntity(new TokenResponse(generierterToken.getTokenContent(), generierterToken.getAblaufdatum(), generierterToken.getBenutzer().getId()), HttpStatus.OK);
    }

}
