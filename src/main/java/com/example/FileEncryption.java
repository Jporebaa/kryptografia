package com.example;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class FileEncryption {

    public static void openWindow() {
        JFrame frame = new JFrame("Szyfrowanie Pliku");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField inputFileField = new JTextField(20);
        JButton chooseFileButton = new JButton("Wybierz plik");
        panel.add(new JLabel("Ścieżka pliku:"));
        panel.add(inputFileField);
        panel.add(chooseFileButton);

        JTextField keyFileField = new JTextField(20);
        JButton chooseKeyFileButton = new JButton("Wybierz plik klucza (tylko do deszyfrowania)");
        panel.add(new JLabel("Ścieżka pliku klucza:"));
        panel.add(keyFileField);
        panel.add(chooseKeyFileButton);

        JButton encryptButton = new JButton("Szyfruj");
        JButton decryptButton = new JButton("Deszyfruj");
        panel.add(encryptButton);
        panel.add(decryptButton);

        JTextArea outputArea = new JTextArea(5, 30);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        panel.add(new JScrollPane(outputArea));

        chooseFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                inputFileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        chooseKeyFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                keyFileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        encryptButton.addActionListener(e -> {
            try {
                String inputFilePath = inputFileField.getText().trim();
                if (inputFilePath.isEmpty()) {
                    outputArea.setText("Błąd: Ścieżka pliku jest pusta.");
                    return;
                }

                // Ścieżki do pliku zaszyfrowanego i klucza na pulpicie
                String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
                String encryptedFilePath = desktopPath + File.separator + "zaszyfrowany_plik.enc";
                String keyFilePath = desktopPath + File.separator + "klucz.txt";

                // Generowanie klucza AES
                KeyGenerator aesKeyGen = KeyGenerator.getInstance("AES");
                aesKeyGen.init(128);
                SecretKey aesKey = aesKeyGen.generateKey();

                // Zapis klucza do pliku w formacie Base64
                try (FileWriter keyWriter = new FileWriter(keyFilePath)) {
                    keyWriter.write(Base64.getEncoder().encodeToString(aesKey.getEncoded()));
                }

                // Szyfrowanie pliku
                encryptFile(inputFilePath, encryptedFilePath, aesKey, "AES");
                outputArea.setText("Plik zaszyfrowano i zapisano w: " + encryptedFilePath + "\nKlucz zapisano w: " + keyFilePath);
            } catch (Exception ex) {
                outputArea.setText("Błąd: " + ex.getClass().getName() + " - " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        decryptButton.addActionListener(e -> {
            try {
                String inputFilePath = inputFileField.getText().trim();
                String keyFilePath = keyFileField.getText().trim();
                if (inputFilePath.isEmpty() || keyFilePath.isEmpty()) {
                    outputArea.setText("Błąd: Ścieżka pliku lub klucza jest pusta.");
                    return;
                }

                // Odczyt klucza z pliku w formacie Base64
                SecretKey aesKey;
                try (BufferedReader keyReader = new BufferedReader(new FileReader(keyFilePath))) {
                    String keyString = keyReader.readLine();
                    if (keyString == null || keyString.isEmpty()) {
                        throw new IllegalArgumentException("Plik klucza jest pusty lub nieprawidłowy.");
                    }
                    byte[] decodedKey = Base64.getDecoder().decode(keyString);
                    aesKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
                }

                // Ścieżka do odszyfrowanego pliku
                String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
                String decryptedFilePath = desktopPath + File.separator + "odszyfrowany_plik.txt";

                // Deszyfrowanie pliku
                decryptFile(inputFilePath, decryptedFilePath, aesKey, "AES");
                outputArea.setText("Plik odszyfrowano i zapisano w: " + decryptedFilePath);
            } catch (Exception ex) {
                outputArea.setText("Błąd: " + ex.getClass().getName() + " - " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void encryptFile(String filePath, String outputFilePath, SecretKey key, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
        byte[] encrypted = cipher.doFinal(fileContent);
        Files.write(Paths.get(outputFilePath), encrypted);
    }

    public static void decryptFile(String filePath, String outputFilePath, SecretKey key, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
        byte[] decrypted = cipher.doFinal(fileContent);
        Files.write(Paths.get(outputFilePath), decrypted);
    }
}
