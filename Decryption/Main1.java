package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.Scanner;


public class Main1 extends Application {

    private TextField secretCodeField;
    private TextField encryptedFilePathField;
    private TextField secretKeyField;
    private TextField ivField;
    private long decryptionTime; // Variable to store decryption time

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Asmita Roy Mondal");

        // Create header label
        Label headerLabel = new Label("CRYPTIFY DECRYPTION");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-color: #7B68EE; -fx-padding: 10px;-fx-text-fill: white;-fx-background-radius: 5px;");

        Label secretCodeLabel = new Label("Secret Code:");
        // Replace the existing TextField with PasswordField
        PasswordField secretCodeField = new PasswordField();
        secretCodeField.setPromptText("Enter Secret Code");
        this.secretCodeField = secretCodeField;

        Label encryptedFilePathLabel = new Label("Encrypted File Path:");
        encryptedFilePathField = new TextField();
        encryptedFilePathField.setPromptText("Select Encrypted File");

        Label secretKeyLabel = new Label("Secret Key (Base64):");
        secretKeyField = new TextField();
        secretKeyField.setPromptText("Enter Secret Key");

        Label ivLabel = new Label("Initialization Vector (Base64):");
        ivField = new TextField();
        ivField.setPromptText("Enter IV");

        Button chooseFileButton = new Button("Choose Encrypted File");
        chooseFileButton.setOnAction(e -> chooseEncryptedFile(primaryStage));

        Button decryptButton = new Button("Decrypt");
        decryptButton.setOnAction(e -> {
            long startTime = System.nanoTime(); // Record start time
            decryptFile(); // Perform decryption
            long endTime = System.nanoTime(); // Record end time
            decryptionTime = endTime - startTime; // Calculate decryption time
            displayPerformanceMetrics(); // Display performance metrics

        });

        // Create layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(headerLabel, 0, 0, 3, 1); // Spanning multiple columns for the header
        grid.add(secretCodeLabel, 0, 1);
        grid.add(secretCodeField, 1, 1);
        grid.add(encryptedFilePathLabel, 0, 2);
        grid.add(encryptedFilePathField, 1, 2);
        grid.add(chooseFileButton, 2, 2);
        grid.add(secretKeyLabel, 0, 3);
        grid.add(secretKeyField, 1, 3);
        grid.add(ivLabel, 0, 4);
        grid.add(ivField, 1, 4);
        grid.add(decryptButton, 0, 5, 3, 1); // Spanning multiple columns for the button

        // Set scene
        Scene scene = new Scene(grid, 650, 300);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void chooseEncryptedFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Encrypted File");
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            encryptedFilePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void decryptFile() {
        // Get user input
        CharSequence secretCodeChars = secretCodeField.getCharacters();
        String secretCode = secretCodeChars.toString();
        String encryptedFilePath = encryptedFilePathField.getText();
        String base64Key = secretKeyField.getText();
        String base64IV = ivField.getText();

        // Call the decryptFile method with user input
        try {
            Scanner scanner = new Scanner(System.in);
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mds", "root", "roy1968");
            System.out.println("Successfully Connected to Database!");
            decryptFile(scanner, con, secretCode, encryptedFilePath, base64Key, base64IV);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void decryptFile(Scanner scanner, Connection con, String secretCode, String encryptedFilePath, String base64Key, String base64IV) throws Exception {

        // Input secret key
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        // Input IV
        byte[] iv = Base64.getDecoder().decode(base64IV);

        String selectQuery = "SELECT secret_code FROM cryptify1 WHERE secret_key = ? AND iv = ?";
        PreparedStatement pstmt = con.prepareStatement(selectQuery);
        pstmt.setString(1, base64Key);
        pstmt.setString(2, base64IV);
        ResultSet rs = pstmt.executeQuery();
        
        if(rs.next()) {
        	String text = rs.getString("secret_code");
            String key = generateKey(text, secretCode);
            String original_text = originalText(text, key);
            if (original_text.equals("AsMiTa2001")) {
                // Read encrypted bytes from file
                byte[] encryptedBytes = readFileContent(encryptedFilePath);

                // Perform decryption on the encrypted bytes
                byte[] decryptedBytes = performDecryption(encryptedBytes, secretKey, iv);

                // Choose output file extension based on input file extension
                String outputFilePath = chooseOutputFilePath(encryptedFilePath, false);

                // Write decrypted bytes to a file
                writeToFile(outputFilePath, decryptedBytes);

                System.out.println("File decrypted successfully. Decrypted file saved as: " + outputFilePath);
                displayDecryptionSuccess(outputFilePath);

            } else {
                System.out.println("OOPS! Wrong Code");
                displayWrongCodeError1();

            }
        }
        else
        {
            displayWrongCodeError();

        }
        
    }


    private static void displayDecryptionSuccess(String outputFilePath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Decryption Successful");
        alert.setHeaderText("File Decrypted Successfully");
//        int dotIndex = outputFilePath.lastIndexOf('\\');
//        String path=outputFilePath.substring(dotIndex+1);

        alert.setContentText("Decrypted file saved as:\n" + outputFilePath+ "\n\n"+"\r\n"); // Set content text with the output file path
        alert.showAndWait();
    }


    private static void displayWrongCodeError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Decryption Error");
        alert.setHeaderText("Wrong Code");
        alert.setContentText("OOPS! Wrong Code");
        alert.showAndWait();
    }

    private static void displayWrongCodeError1() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Decryption Error");
        alert.setHeaderText("Wrong Key or IV");
        alert.setContentText("OOPS! Please Check Your Codes");
        alert.showAndWait();
    }
    
    // Read file content into a byte array
    public static byte[] readFileContent(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileContent = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(fileContent);
        fis.close();
        return fileContent;
    }


    // Perform decryption on the input bytes with the secret key and IV
    public static byte[] performDecryption(byte[] encryptedBytes, SecretKey secretKey, byte[] iv) throws Exception {
        // Decryption
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        return cipher.doFinal(encryptedBytes);
    }

    // Generate key based on secret code and input key
    public static String generateKey(String str, String key) {
        int x = str.length();

        for (int i = 0; ; i++) {
            if (x == i)
                i = 0; // reaching the end of key, so start again
            if (key.length() == str.length())
                break; // break as soon as key length equals string length
            key += (key.charAt(i)); // else keep adding characters of the given keyword
        }
        return key.toUpperCase();
    }

    static String originalText(String cipher_text, String key) {
        String orig_text = "";

        for (int i = 0; i < cipher_text.length(); i++) {
            char c = cipher_text.charAt(i);
            char k = key.charAt(i); // Use key characters cyclically
            if (Character.isLetter(c)) {
                if (Character.isUpperCase(c)) {
                    int x = (c - 'A' - (k - 'A') + 26) % 26;
                    orig_text += (char) (x + 'A');
                } else {
                    int x = (c - 'a' - (k - 'A') + 26) % 26; // Always use uppercase key
                    orig_text += (char) (x + 'a');
                }
            } else if (Character.isDigit(c)) {
                int shiftedDigit = ((c - '0') - 3 + 10) % 10;
                orig_text += (char) (shiftedDigit + '0');
            } else {
                orig_text += c;
            }
        }
        return orig_text;
    }

    // Choose output file extension based on input file extension
    public static String chooseOutputFilePath(String inputFilePath, boolean isEncryption) {
        String outputFilePath = inputFilePath.substring(0, inputFilePath.lastIndexOf('.'));
        int dotIndex = inputFilePath.lastIndexOf('.');
        if (dotIndex != -1) {
            String extension = inputFilePath.substring(dotIndex + 1);
            if (isEncryption) {
                // Append appropriate encryption extension based on input file extension
                switch (extension.toLowerCase()) {
                    case "docx":
                        outputFilePath += "_encrypted.docx";
                        break;
                    case "xlsx":
                        outputFilePath += "_encrypted.xlsx";
                        break;
                    case "doc":
                        outputFilePath += "_encrypted.doc";
                        break;
                    case "txt":
                        outputFilePath += "_encrypted.txt";
                        break;
                    case "csv":
                        outputFilePath += "_encrypted.csv";
                        break;
                    default:
                        outputFilePath += "_encrypted.dat"; // Default extension
                }
            } else {
                // Remove existing extension and append appropriate decryption extension
                // outputFilePath = inputFilePath.substring(0, dotIndex);
                switch (extension.toLowerCase()) {
                    case "docx":
                    case "xlsx":
                    case "doc":
                    case "txt":
                    case "csv":
                        // Remove "_encrypted" suffix if present
                        outputFilePath = outputFilePath.replace("_encrypted", "_decrypted.");
                        outputFilePath += extension;
                        break;
                    default:
                        outputFilePath += "_decrypted.dat"; // Default extension
                }
            }
        } else {
            // No extension found in input file path
            if (isEncryption) {
                outputFilePath += "_encrypted.dat"; // Default extension
            } else {
                outputFilePath += "_decrypted.dat"; // Default extension
            }
        }
        return outputFilePath;
    }

    // Write byte array to a file
    public static void writeToFile(String filePath, byte[] data) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(data);
        fos.close();
    }
    
    private void displayPerformanceMetrics() {
        System.out.println("Decryption Time: " + decryptionTime + " nanoseconds");
    }
    
    public static void main(String[] args) throws Exception {

        launch(args);

    }
}
