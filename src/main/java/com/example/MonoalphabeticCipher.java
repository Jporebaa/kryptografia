package com.example;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MonoalphabeticCipher {

    public static void openWindow() {
        JFrame frame = new JFrame("Szyfr Monoalfabetyczny");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField inputField = new JTextField(20);
        panel.add(new JLabel("Tekst wejściowy:"));
        panel.add(inputField);

        JTextField mappingField = new JTextField(20);
        panel.add(new JLabel("Mapowanie (np. A=B,C=D,...):"));
        panel.add(mappingField);

        JButton encryptButton = new JButton("Szyfruj");
        JButton decryptButton = new JButton("Deszyfruj");
        panel.add(encryptButton);
        panel.add(decryptButton);

        JTextArea outputArea = new JTextArea(5, 20);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        panel.add(new JScrollPane(outputArea));

        encryptButton.addActionListener(e -> {
            try {
                String input = inputField.getText();
                String mapping = mappingField.getText();
                Map<Character, Character> key = parseMapping(mapping);
                outputArea.setText(encrypt(input, key));
            } catch (Exception ex) {
                outputArea.setText("Błąd: " + ex.getMessage());
            }
        });

        decryptButton.addActionListener(e -> {
            try {
                String input = inputField.getText();
                String mapping = mappingField.getText();
                Map<Character, Character> key = parseMapping(mapping);
                outputArea.setText(decrypt(input, key));
            } catch (Exception ex) {
                outputArea.setText("Błąd: " + ex.getMessage());
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    public static String encrypt(String text, Map<Character, Character> key) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            result.append(key.getOrDefault(c, c));
        }
        return result.toString();
    }

    public static String decrypt(String text, Map<Character, Character> key) {
        Map<Character, Character> reverseKey = new HashMap<>();
        for (Map.Entry<Character, Character> entry : key.entrySet()) {
            reverseKey.put(entry.getValue(), entry.getKey());
        }
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            result.append(reverseKey.getOrDefault(c, c));
        }
        return result.toString();
    }

    public static Map<Character, Character> parseMapping(String mapping) {
        Map<Character, Character> map = new HashMap<>();
        for (String pair : mapping.split(",")) {
            String[] kv = pair.split("=");
            map.put(kv[0].charAt(0), kv[1].charAt(0));
        }
        return map;
    }
}
