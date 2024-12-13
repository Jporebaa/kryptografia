package com.example;

import javax.swing.*;
import java.awt.*;

public class PolyalphabeticCipher {

    public static void openWindow() {
        JFrame frame = new JFrame("Szyfr Polialfabetyczny");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField inputField = new JTextField(20);
        panel.add(new JLabel("Tekst wejściowy:"));
        panel.add(inputField);

        JTextField keyField = new JTextField(20);
        panel.add(new JLabel("Klucz:"));
        panel.add(keyField);

        JButton encryptButton = new JButton("Szyfruj");
        JButton decryptButton = new JButton("Deszyfruj");
        panel.add(encryptButton);
        panel.add(decryptButton);

        JTextArea outputArea = new JTextArea(5, 20);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        panel.add(new JScrollPane(outputArea));

        encryptButton.addActionListener(e -> {
            String input = inputField.getText();
            String key = keyField.getText();
            outputArea.setText(encrypt(input, key));
        });

        decryptButton.addActionListener(e -> {
            String input = inputField.getText();
            String key = keyField.getText();
            outputArea.setText(decrypt(input, key));
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    public static String encrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        key = key.repeat((text.length() / key.length()) + 1).substring(0, text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = (char) ((text.charAt(i) + key.charAt(i) - 2 * 'A') % 26 + 'A');
            result.append(c);
        }
        return result.toString();
    }

    public static String decrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        key = key.repeat((text.length() / key.length()) + 1).substring(0, text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = (char) ((text.charAt(i) - key.charAt(i) + 26) % 26 + 'A');
            result.append(c);
        }
        return result.toString();
    }
}
