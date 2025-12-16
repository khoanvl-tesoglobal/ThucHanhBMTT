/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Buoi3;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.util.Base64;
/**
 *
 * @author Administrator
 */
public class AESCipher {
    private static final String ALGORITHM = "AES";
    private static final String ENCRYPTION_KEY = "encryptionKey"; // Key for AES encryption (16 bytes)

    public static String encrypt(String plaintext, String secretKey) throws NoSuchAlgorithmException,
            InvalidKeyException, NoSuchPaddingException,
            BadPaddingException, IllegalBlockSizeException {

        SecretKey key = generateKey(secretKey);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String ciphertext, String secretKey) throws NoSuchAlgorithmException,
            InvalidKeyException, NoSuchPaddingException,
            BadPaddingException, IllegalBlockSizeException {

        SecretKey key = generateKey(secretKey);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] decodedBytes = Base64.getDecoder().decode(ciphertext);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    private static SecretKey generateKey(String secretKey) throws NoSuchAlgorithmException {
        byte[] keyBytes = secretKey.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
        return keySpec;
    }
    
    public static String generateRegistrationKey(String username, String password) {
        String registrationKey = username + ":" + password + ":" + ENCRYPTION_KEY;
        return registrationKey;
    }

    public static void saveRegistrationKeyToFile(String registrationKey, String filename)
            throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(registrationKey);
        }
    }

    public static String readRegistrationKeyFromFile(String filename)
            throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (String) ois.readObject();
        }
    }
}
