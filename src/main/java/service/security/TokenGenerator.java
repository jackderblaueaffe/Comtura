package service.security;

import repository.dao.Benutzer;
import repository.dao.Token;
import repository.dao.Benutzer;

import java.util.Random;

public class TokenGenerator {

    public static final long EXPIRATION_TIME = 259200000; // 30 * 24 * 60 * 60 * 1000 -> 30 Tage

    public static Token generateToken(Benutzer benutzer) {
        Token token = new Token();
        byte[] key = randomString(16);
        try {
            token.setTokenContent(Encryption.encryptAES(benutzer.getBenutzername() + benutzer.getPasswort(), key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        token.setEncryptionKey(key);
        token.setAblaufdatum(System.currentTimeMillis() + EXPIRATION_TIME);
        token.setBenutzer(benutzer);

        return token;
    }

    public static byte[] randomString(int length) {
        Random rand = new Random();
        byte[] b = new byte[length];

        for(int i = 0; i < b.length; i++) {
            b[i] = (byte) (rand.nextInt(26) + 65);
        }

        return b;
    }

}
