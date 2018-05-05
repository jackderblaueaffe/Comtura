package service.controller;

import api.request.TokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.TokenRepository;
import repository.dao.Benutzer;
import repository.dao.Token;
import repository.validation.EntityValidator;
import service.helper.Helper;

@RestController
@RequestMapping("/service/token")
public class TokenController {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    EntityValidator validator;

    @PostMapping("/isTokenValid")
    public ResponseEntity isTokenValid(@RequestBody TokenRequest tokenRequest) {

        if(Helper.isTokenValid(tokenRepository, tokenRequest.getToken())) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
