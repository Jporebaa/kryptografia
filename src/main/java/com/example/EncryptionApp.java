package com.example;

import javax.swing.*;
import java.awt.*;

public class EncryptionApp {

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Wybierz metodę szyfrowania");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 500);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 1));

        // Przycisk dla szyfru polialfabetycznego
        JButton polyalphabeticButton = new JButton("Szyfr Polialfabetyczny");
        polyalphabeticButton.addActionListener(e -> PolyalphabeticCipher.openWindow());
        mainPanel.add(polyalphabeticButton);

        // Przycisk dla szyfru monoalfabetycznego
        JButton monoalphabeticButton = new JButton("Szyfr Monoalfabetyczny");
        monoalphabeticButton.addActionListener(e -> MonoalphabeticCipher.openWindow());
        mainPanel.add(monoalphabeticButton);

        // Przycisk dla DES
        JButton desButton = new JButton("Szyfrowanie DES");
        desButton.addActionListener(e -> DESCipher.openWindow());
        mainPanel.add(desButton);

        // Przycisk dla szyfrowania plików
        JButton fileEncryptionButton = new JButton("Szyfrowanie Pliku");
        fileEncryptionButton.addActionListener(e -> FileEncryption.openWindow());
        mainPanel.add(fileEncryptionButton);

        // Przycisk dla RSA
        JButton rsaButton = new JButton("Szyfrowanie RSA");
        rsaButton.addActionListener(e -> RSACipher.openWindow());
        mainPanel.add(rsaButton);

        // Przycisk dla podpisu cyfrowego
        JButton digitalSignatureButton = new JButton("Podpis Cyfrowy");
        digitalSignatureButton.addActionListener(e -> DigitalSignature.openWindow());
        mainPanel.add(digitalSignatureButton);

        // Dodanie panelu do ramki i wyświetlenie GUI
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }
}
