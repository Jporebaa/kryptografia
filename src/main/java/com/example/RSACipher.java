package com.example;

import javax.crypto.Cipher;
import javax.swing.*;
import java.awt.*;
import java.security.*;
import java.util.Base64;

public class RSACipher {

    private static KeyPair keyPair;

    public static void openWindow() {
        JFrame frame = new JFrame("Szyfrowanie RSA");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField inputTextField = new JTextField(20);
        panel.add(new JLabel("Tekst wejściowy:"));
        panel.add(inputTextField);

        JTextField keyField = new JTextField(20);
        panel.add(new JLabel("Klucz publiczny/privatny (jeśli pusty, wygeneruje się nowe klucze):"));
        panel.add(keyField);

        JButton generateKeysButton = new JButton("Generuj klucze");
        JButton encryptButton = new JButton("Szyfruj");
        JButton decryptButton = new JButton("Deszyfruj");
        panel.add(generateKeysButton);
        panel.add(encryptButton);
        panel.add(decryptButton);

        JTextArea outputArea = new JTextArea(5, 30);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        panel.add(new JScrollPane(outputArea));

        generateKeysButton.addActionListener(e -> {
            try {
                keyPair = generateKeyPair();
                outputArea.setText("Klucze wygenerowane!\n" +
                        "Klucz publiczny: " + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()) + "\n" +
                        "Klucz prywatny: " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
            } catch (Exception ex) {
                outputArea.setText("Błąd: " + ex.getClass().getName() + " - " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        encryptButton.addActionListener(e -> {
            try {
                String inputText = inputTextField.getText().trim();
                if (inputText.isEmpty()) {
                    outputArea.setText("Błąd: Tekst wejściowy jest pusty.");
                    return;
                }

                if (keyPair == null) {
                    outputArea.setText("Błąd: Najpierw wygeneruj klucze.");
                    return;
                }

                // Szyfrowanie tekstu
                String encryptedText = encryptText(inputText, keyPair.getPublic());
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

                if (keyPair == null) {
                    outputArea.setText("Błąd: Najpierw wygeneruj klucze.");
                    return;
                }

                // Deszyfrowanie tekstu
                String decryptedText = decryptText(encryptedText, keyPair.getPrivate());
                outputArea.setText("Odszyfrowany tekst: " + decryptedText);
            } catch (Exception ex) {
                outputArea.setText("Błąd: " + ex.getClass().getName() + " - " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    public static String encryptText(String text, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptText(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }
}
