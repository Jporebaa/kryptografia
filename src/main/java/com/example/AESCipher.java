package com.example;

import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCipher {

    public static void openWindow() {
        JFrame frame = new JFrame("Szyfrowanie AES");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        JLabel inputLabel = new JLabel("Tekst wejściowy:");
        JTextArea inputField = new JTextArea(3, 20);

        JLabel keyLabel = new JLabel("Klucz AES (Base64):");
        JTextField keyField = new JTextField();

        JLabel ivLabel = new JLabel("IV (Base64):");
        JTextField ivField = new JTextField();

        JButton generateKeyButton = new JButton("Generuj klucz i IV");
        generateKeyButton.addActionListener(e -> {
            try {
                KeyAndIV keyAndIV = generateKeyAndIV();
                keyField.setText(keyAndIV.key);
                ivField.setText(keyAndIV.iv);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Błąd podczas generowania klucza i IV: " + ex.getMessage());
            }
        });

        JButton encryptButton = new JButton("Szyfruj");
        JTextArea outputField = new JTextArea(3, 20);
        outputField.setEditable(false);

        encryptButton.addActionListener(e -> {
            try {
                String encrypted = encrypt(inputField.getText(), keyField.getText(), ivField.getText());
                outputField.setText(encrypted);
            } catch (Exception ex) {
                outputField.setText("Błąd: " + ex.getMessage());
            }
        });

        JButton decryptButton = new JButton("Deszyfruj");
        decryptButton.addActionListener(e -> {
            try {
                String decrypted = decrypt(outputField.getText(), keyField.getText(), ivField.getText());
                inputField.setText(decrypted);
            } catch (Exception ex) {
                outputField.setText("Błąd: " + ex.getMessage());
            }
        });

        panel.add(inputLabel);
        panel.add(new JScrollPane(inputField));
        panel.add(keyLabel);
        panel.add(keyField);
        panel.add(ivLabel);
        panel.add(ivField);
        panel.add(generateKeyButton);
        panel.add(encryptButton);
        panel.add(decryptButton);
        panel.add(new JScrollPane(outputField));

        frame.add(panel);
        frame.setVisible(true);
    }

    public static String encrypt(String text, String base64Key, String base64Iv) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(base64Key), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(base64Iv));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText, String base64Key, String base64Iv) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(base64Key), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(base64Iv));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    public static KeyAndIV generateKeyAndIV() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256, new SecureRandom());
        SecretKey secretKey = keyGen.generateKey();

        byte[] ivBytes = new byte[16];
        SecureRandom.getInstanceStrong().nextBytes(ivBytes);

        return new KeyAndIV(
                Base64.getEncoder().encodeToString(secretKey.getEncoded()),
                Base64.getEncoder().encodeToString(ivBytes)
        );
    }

    static class KeyAndIV {
        String key;
        String iv;

        KeyAndIV(String key, String iv) {
            this.key = key;
            this.iv = iv;
        }
    }
}
