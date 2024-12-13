package com.example;

import javax.swing.*;
import java.awt.*;
import java.security.*;
import java.util.Base64;

public class DigitalSignature {

    private static KeyPair keyPair;

    public static void openWindow() {
        JFrame frame = new JFrame("Podpis Cyfrowy");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField inputTextField = new JTextField(20);
        panel.add(new JLabel("Tekst wejściowy:"));
        panel.add(inputTextField);

        JTextField signatureField = new JTextField(20);
        panel.add(new JLabel("Podpis cyfrowy (Base64):"));
        panel.add(signatureField);

        JButton generateKeysButton = new JButton("Generuj klucze");
        JButton signButton = new JButton("Podpisz");
        JButton verifyButton = new JButton("Zweryfikuj");
        panel.add(generateKeysButton);
        panel.add(signButton);
        panel.add(verifyButton);

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

        signButton.addActionListener(e -> {
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

                // Tworzenie podpisu cyfrowego
                String signature = signText(inputText, keyPair.getPrivate());
                signatureField.setText(signature);
                outputArea.setText("Podpis cyfrowy wygenerowany: " + signature);
            } catch (Exception ex) {
                outputArea.setText("Błąd: " + ex.getClass().getName() + " - " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        verifyButton.addActionListener(e -> {
            try {
                String inputText = inputTextField.getText().trim();
                String signature = signatureField.getText().trim();

                if (inputText.isEmpty() || signature.isEmpty()) {
                    outputArea.setText("Błąd: Tekst wejściowy lub podpis cyfrowy jest pusty.");
                    return;
                }

                if (keyPair == null) {
                    outputArea.setText("Błąd: Najpierw wygeneruj klucze.");
                    return;
                }

                // Weryfikacja podpisu cyfrowego
                boolean isValid = verifySignature(inputText, signature, keyPair.getPublic());
                outputArea.setText(isValid ? "Podpis cyfrowy jest prawidłowy." : "Podpis cyfrowy jest nieprawidłowy.");
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

    public static String signText(String text, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(text.getBytes());
        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    public static boolean verifySignature(String text, String signatureStr, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(text.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
        return signature.verify(signatureBytes);
    }
}
