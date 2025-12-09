package com.project.bank;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.mindrot.jbcrypt.BCrypt;


public class PasswordHasher implements IPasswordHasher{
    @Override
    public String encypt(String pass) {

        String hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt(12));
        
        return hashedPassword;

    }

    @Override
    public boolean check(String input , String hashedPass) {

        if (BCrypt.checkpw(input, hashedPass)) {
            return true;
        }
        return false;
    }
}
