package utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;

public class FileEncryptorUtils {

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final int IV_SIZE = 16;

    public static SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128); // 128-bit AES key
        return keyGen.generateKey();
    }

    public static void encryptFile(Path inputFile, Path outputFile, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] inputBytes = Files.readAllBytes(inputFile);
        byte[] encryptedBytes = cipher.doFinal(inputBytes);

        // Guardamos el IV al principio del archivo cifrado
        byte[] outputBytes = new byte[IV_SIZE + encryptedBytes.length];
        System.arraycopy(iv, 0, outputBytes, 0, IV_SIZE);
        System.arraycopy(encryptedBytes, 0, outputBytes, IV_SIZE, encryptedBytes.length);

        Files.write(outputFile, outputBytes);
    }

    public static void decryptFile(Path inputFile, Path outputFile, SecretKey key) throws Exception {
        byte[] fileContent = Files.readAllBytes(inputFile);

        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(fileContent, 0, iv, 0, IV_SIZE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        byte[] encryptedData = new byte[fileContent.length - IV_SIZE];
        System.arraycopy(fileContent, IV_SIZE, encryptedData, 0, encryptedData.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        Files.write(outputFile, decryptedBytes);
    }
}
