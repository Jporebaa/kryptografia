package com.example;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.util.Base64;

public class DESCipher {

    public static void openWindow() {
        JFrame frame = new JFrame("Szyfrowanie DES");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField inputTextField = new JTextField(20);
        panel.add(new JLabel("Tekst wejściowy:"));
        panel.add(inputTextField);

        JTextField keyField = new JTextField(20);
        panel.add(new JLabel("Klucz (jeśli pusty, wygeneruje się nowy):"));
        panel.add(keyField);

        JButton encryptButton = new JButton("Szyfruj");
        JButton decryptButton = new JButton("Deszyfruj");
        panel.add(encryptButton);
        panel.add(decryptButton);

        JTextArea outputArea = new JTextArea(5, 30);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        panel.add(new JScrollPane(outputArea));

        encryptButton.addActionListener(e -> {
            try {
                String inputText = inputTextField.getText().trim();
                if (inputText.isEmpty()) {
                    outputArea.setText("Błąd: Tekst wejściowy jest pusty.");
                    return;
                }

                // Generowanie lub odczyt klucza
                SecretKey desKey;
                if (keyField.getText().trim().isEmpty()) {
                    KeyGenerator desKeyGen = KeyGenerator.getInstance("DES");
                    desKey = desKeyGen.generateKey();
                    keyField.setText(Base64.getEncoder().encodeToString(desKey.getEncoded()));
                } else {
                    byte[] decodedKey = Base64.getDecoder().decode(keyField.getText().trim());
                    desKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
                }

                // Szyfrowanie tekstu
                String encryptedText = encryptText(inputText, desKey);
                outputArea.setText("Zaszyfrowany tekst: " + encryptedText);
            } catch (Exception ex) {
                outputArea.setText("Błąd: " + ex.getClass().getName() + " - " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        decryptButton.addActionListener(e -> {
            try {
                String encryptedText = inputTextField.getText().trim();
                if (encryptedText.isEmpty()) {
                    outputArea.setText("Błąd: Tekst wejściowy jest pusty.");
                    return;
                }

                // Odczyt klucza
                if (keyField.getText().trim().isEmpty()) {
                    outputArea.setText("Błąd: Klucz jest pusty. Wprowadź klucz użyty do szyfrowania.");
                    return;
                }

                byte[] decodedKey = Base64.getDecoder().decode(keyField.getText().trim());
                SecretKey desKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");

                // Deszyfrowanie tekstu
                String decryptedText = decryptText(encryptedText, desKey);
                outputArea.setText("Odszyfrowany tekst: " + decryptedText);
            } catch (Exception ex) {
                outputArea.setText("Błąd: " + ex.getClass().getName() + " - " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    public static String encryptText(String text, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptText(String encryptedText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }
}
