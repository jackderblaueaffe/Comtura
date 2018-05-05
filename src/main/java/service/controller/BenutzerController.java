package service.controller;

import api.errors.ValidationErrorMessages;
import api.request.ChangePasswortRequest;
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
import repository.validation.EntityValidator;
import service.helper.Helper;
import service.security.Encryption;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/service/benutzer")
public class BenutzerController {

    @Autowired
    BenutzerRepository benutzerRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    EntityValidator validator;

    @Autowired
    Encryption encryption;

    @PostMapping("/changePasswort")
    public ResponseEntity changePasswort(@RequestBody ChangePasswortRequest request) {

        List<String> errors = new ArrayList<>();
        Benutzer benutzer = Helper.getBenutzerByTokenContent(tokenRepository, request.getToken());

        String passwort1, passwort2;
        try {
            passwort1 = new String(encryption.decryptRSA(Base64.getDecoder().decode(request.getPasswort1())));
            passwort2 = new String(encryption.decryptRSA(Base64.getDecoder().decode(request.getPasswort2())));
            if(passwort1.isEmpty()){
                errors.add("Das 1. Passwort muss vorhanden sein.");
            }
            if(passwort2.isEmpty()){
                errors.add("Das 2. Passwort muss vorhanden sein.");
            }
            if(benutzer == null) { //z.B. Token abgelaufen
                errors.add("Sie haben keine Berechtigung diese Aktion durchzuführen. Bitte melden Sie sich erneut an.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new ValidationErrorMessages(errors), HttpStatus.BAD_REQUEST);
        }
        if(errors.size() > 0)
        {
            return new ResponseEntity(new ValidationErrorMessages(errors), HttpStatus.BAD_REQUEST);
        }

        if(!passwort1.equals(passwort2))
        {
            errors.add("Die eingegeben Passwörter stimmen nicht überein.");
            return new ResponseEntity<>(new ValidationErrorMessages(errors), HttpStatus.BAD_REQUEST);
        }

        if(!Helper.checkPasswortPolicy(passwort1)) {
            errors.add("Das Passwort entspricht nicht den Passwortrichtlinien. (mindestens 5 Zeichen)");
            return new ResponseEntity<>(new ValidationErrorMessages(errors), HttpStatus.BAD_REQUEST);
        }


        benutzer.setPasswort(Encryption.SHAencrypt(passwort1));

        //in Datenbank speichern
        try
        {
            benutzerRepository.save(benutzer);
        }
        catch(ConstraintViolationException c)
        {
            errors = validator.createErrorListByConstraintViolationException(c);
            return new ResponseEntity(new ValidationErrorMessages(errors), HttpStatus.BAD_REQUEST);
        }

        //Erfolgsmeldung
        return new ResponseEntity(HttpStatus.OK);
    }

}
