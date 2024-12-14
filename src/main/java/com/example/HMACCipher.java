package com.example;

import javax.swing.*;
import java.awt.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class    HMACCipher {

    public static void openWindow() {
        JFrame frame = new JFrame("Szyfrowanie HMAC");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JLabel inputLabel = new JLabel("Tekst wejściowy:");
        JTextField inputField = new JTextField();

        JLabel keyLabel = new JLabel("Klucz (Base64):");
        JTextField keyField = new JTextField();

        JButton generateKeyButton = new JButton("Generuj klucz");
        generateKeyButton.addActionListener(e -> {
            keyField.setText(generateRandomKey());
        });

        JButton hashButton = new JButton("Oblicz HMAC");
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);

        hashButton.addActionListener(e -> {
            String inputText = inputField.getText();
            String key = keyField.getText();
            if (inputText.isEmpty() || key.isEmpty()) {
                outputArea.setText("Błąd: Tekst wejściowy i klucz nie mogą być puste.");
                return;
            }
            try {
                String hmac = calculateHMAC(inputText, key);
                outputArea.setText("HMAC: " + hmac);
            } catch (Exception ex) {
                outputArea.setText("Błąd: " + ex.getMessage());
            }
        });

        panel.add(inputLabel);
        panel.add(inputField);
        panel.add(keyLabel);
        panel.add(keyField);
        panel.add(generateKeyButton);
        panel.add(hashButton);
        panel.add(new JScrollPane(outputArea));

        frame.add(panel);
        frame.setVisible(true);
    }

    public static String calculateHMAC(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        byte[] hmacBytes = mac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    public static String generateRandomKey() {
        try {
            byte[] keyBytes = new byte[32]; // 256-bit key
            java.security.SecureRandom.getInstanceStrong().nextBytes(keyBytes);
            return Base64.getEncoder().encodeToString(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Błąd generowania klucza: " + e.getMessage());
        }
    }
}
