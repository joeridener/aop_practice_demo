package encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * A small simple class that I use for top-notch encryption. Uses static methods that are pretty self-explanatory.
 */
public class CryptoUtils {

    public static SecretKey generateKey(){

        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");

        }catch(Exception e)
        {
            e.printStackTrace(System.err);
        }
        keyGen.init(128);
        return keyGen.generateKey();

    }

    public static IvParameterSpec generateIv(){
        byte[] iv = new byte[16];//AES block-size
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static String encrypt( String input, SecretKey key, IvParameterSpec iv) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] encrypted = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt( String cipherText, SecretKey key, IvParameterSpec iv) throws Exception{
        Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        byte[] decoded = cipher.doFinal( Base64.getDecoder().decode(cipherText) );
        return new String(decoded, StandardCharsets.UTF_8);
    }
}
