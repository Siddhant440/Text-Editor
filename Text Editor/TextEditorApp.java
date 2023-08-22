import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;

public class TextEditorApp extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu;
    private JMenuItem openMenuItem, saveMenuItem, closeMenuItem, printMenuItem;
    private JMenuItem cutMenuItem, copyMenuItem, pasteMenuItem;
    private JButton saveAndSubmitButton;

    private File currentFile;

    public TextEditorApp() {

        setTitle("Text Editor");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        textArea = new JTextArea();
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        openMenuItem = new JMenuItem("Open");
        saveMenuItem = new JMenuItem("Save");
        closeMenuItem = new JMenuItem("Close");
        printMenuItem = new JMenuItem("Print");
        cutMenuItem = new JMenuItem("Cut");
        copyMenuItem = new JMenuItem("Copy");
        pasteMenuItem = new JMenuItem("Paste");
        saveAndSubmitButton = new JButton("Save and Submit");


        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(closeMenuItem);
        fileMenu.add(printMenuItem);
        editMenu.add(cutMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);


        openMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        closeMenuItem.addActionListener(this);
        printMenuItem.addActionListener(this);
        cutMenuItem.addActionListener(this);
        copyMenuItem.addActionListener(this);
        pasteMenuItem.addActionListener(this);
        saveAndSubmitButton.addActionListener(this);


        setJMenuBar(menuBar);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(saveAndSubmitButton, BorderLayout.SOUTH);


        setVisible(true);
    }


     @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openMenuItem) {
            openFile();
        } else if (e.getSource() == saveMenuItem) {
            saveFile();
        } else if (e.getSource() == closeMenuItem) {
            closeFile();
        } else if (e.getSource() == printMenuItem) {
            printText();
        } else if (e.getSource() == cutMenuItem) {
            textArea.cut();
        } else if (e.getSource() == copyMenuItem) {
            textArea.copy();
        } else if (e.getSource() == pasteMenuItem) {
            textArea.paste();
        } else if (e.getSource() == saveAndSubmitButton) {
            saveFile();
            closeFile();
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
                currentFile = file;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error opening the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        if (currentFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                writer.write(textArea.getText());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving the file.", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                closeFile();
            }
        } else {
            saveFileAs();
        }
    }

    private void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(textArea.getText());
                currentFile = file;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void closeFile() {
        if (currentFile != null) {
            textArea.setText("");
            currentFile = null;
        }
    }

    private void printText() {
        PrinterJob job = PrinterJob.getPrinterJob();
        if (job.printDialog()) {
            job.setPrintable(textArea.getPrintable(null, null));
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Error printing the document.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TextEditorApp::new);
    }
}