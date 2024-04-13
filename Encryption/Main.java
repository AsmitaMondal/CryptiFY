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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Base64;

public class Main extends Application {

    private TextField secretCodeField;
    private TextField filePathField;
    private ComboBox<Integer> keySizeComboBox;
    private long encryptionTime;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Asmita Roy Mondal");

        // Create header label
        Label headerLabel = new Label("CRYPTIFY ENCRYPTION");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-color: #7B68EE; -fx-padding: 10px;-fx-text-fill: white;-fx-background-radius: 5px;");
     // Create UI components
        Label secretCodeLabel = new Label("Secret Code:");
        // Replace the existing TextField with PasswordField
        PasswordField secretCodeField = new PasswordField();
        secretCodeField.setPromptText("Enter Secret Code");
        this.secretCodeField = secretCodeField;

        

        Label filePathLabel = new Label("File Path:");
        filePathField = new TextField();
        filePathField.setPromptText("Select File");

        Button chooseFileButton = new Button("Choose File");
        chooseFileButton.setOnAction(e -> chooseFile(primaryStage));

        Label keySizeLabel = new Label("Key Size:");
        keySizeComboBox = new ComboBox<>();
        keySizeComboBox.getItems().addAll(128, 192, 256);
        keySizeComboBox.setValue(128);

        Button encryptButton = new Button("Encrypt");
        encryptButton.setOnAction(e -> {
            long startTime = System.nanoTime(); // Record start time
            encryptFile(); // Perform decryption
            long endTime = System.nanoTime(); // Record end time
            encryptionTime = endTime - startTime; // Calculate decryption time
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
        grid.add(filePathLabel, 0, 2);
        grid.add(filePathField, 1, 2);
        grid.add(chooseFileButton, 2, 2);
        grid.add(keySizeLabel, 0, 3);
        grid.add(keySizeComboBox, 1, 3);
        grid.add(encryptButton, 0, 4, 3, 1); // Spanning multiple columns for the button

        // Set scene
        Scene scene = new Scene(grid, 500, 250);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void chooseFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }
    static String generateKey(String str, String key) {
        int x = str.length();

        for (int i = 0; ; i++) {
            if (x == i)
                i = 0; //reaching the end of key, so start again
            if (key.length() == str.length())
                break; //break as soon as key length equals string length
            key += (key.charAt(i)); //else keep adding characters of the given keyword
        }
        return key.toUpperCase(); 
    }
    static String cipherText(String str, String key) {
        String cipher_text = "";

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            char k = key.charAt(i ); // Use key characters cyclically

            if (Character.isLetter(c)) {
                if (Character.isUpperCase(c)) {
                    int x = (c - 'A' + k - 'A') % 26;
                    cipher_text += (char) (x + 'A');
                } else {
                    int x = (c - 'a' + k - 'A') % 26; // Always use uppercase key
                    cipher_text += (char) (x + 'a');
                }

            } else if (Character.isDigit(c)) {
                int shiftedDigit = ((c - '0') + 3) % 10;
                cipher_text += (char) (shiftedDigit + '0');
            } else {
                cipher_text += c;
            }
        }
        return cipher_text;
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
        String outputFilePath = inputFilePath.substring(0,inputFilePath.lastIndexOf('.'));
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
                //outputFilePath = inputFilePath.substring(0, dotIndex);
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
    
    private void encryptFile() {
        // Get user input
        CharSequence secretCodeChars = secretCodeField.getCharacters();
        String secretCode = secretCodeChars.toString();
        String filePath = filePathField.getText();
        int keySize = keySizeComboBox.getValue();
        if (!secretCode.matches("[a-zA-Z]*")) {
            // Display an alert box to the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Secret code should only contain letters.");
            alert.showAndWait();

            // Reset the form field
            secretCodeField.clear();
            return; // Exit method to prevent further execution
        }
        

        // Call the existing encryptFile method with user input
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/mds","root","roy1968");
        	System.out.println("Successfully Connected to Database!");
            	
            String key=generateKey("AsMiTa2001",secretCode);
            String cipher_text = cipherText("AsMiTa2001", key);
            encryptFile(cipher_text, filePath, keySize, con);
            System.out.println("Encryption successful.");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void encryptFile(String secretCode, String filePath, int keySize, Connection con) throws Exception {
        // Read file content
        byte[] fileContent = readFileContent(filePath);

        // Generate a secret key with the desired key size
        SecretKey secretKey = generateSecretKey(keySize);

        // Generate a random IV
        byte[] iv = generateIV();

        // Perform encryption on the file content with the secret key and IV
        byte[] encryptedBytes = performEncryption(fileContent, secretKey, iv);

        // Choose output file extension based on input file extension
        String outputFilePath = chooseOutputFilePath(filePath, true);

        // Write encrypted bytes to a file
        writeToFile(outputFilePath, encryptedBytes);
        

        // Convert secret key, IV to Base64 strings
        String base64SecretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        String base64IV = Base64.getEncoder().encodeToString(iv);
        
        System.out.println("File encrypted successfully. Encrypted file saved as: " + outputFilePath);
        System.out.println("Secret Key (Base64): " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        System.out.println("IV (Base64): " + Base64.getEncoder().encodeToString(iv));

        // Prepare SQL INSERT statement
        String insertQuery = "INSERT INTO cryptify1 (secret_key, iv, secret_code, encrypted_file) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(insertQuery);


        // Set values in the prepared statement
        pstmt.setString(1, base64SecretKey);
        pstmt.setString(2, base64IV);
        pstmt.setString(3, secretCode);
        pstmt.setBytes(4, encryptedBytes);

        // Execute INSERT statement to store data in the database
        pstmt.executeUpdate();

        // Close resources
        pstmt.close();
        displayEncryptionInfo(outputFilePath, base64SecretKey, base64IV);

    }
    
    private static void displayEncryptionInfo(String outputFilePath, String secretKey, String iv) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Encryption Successful");
        alert.setHeaderText("File Encrypted Successfully");
        alert.setContentText("File Name: " + outputFilePath + "\n\n" +
                "Secret Key: " + secretKey + "\n\n" +
                "Initialization Vector (IV): " + iv);
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

    // Perform encryption on the input bytes with the secret key and IV
    public static byte[] performEncryption(byte[] inputBytes, SecretKey secretKey, byte[] iv) throws Exception {
        // Encryption
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        return cipher.doFinal(inputBytes);
    }

    // Generate a secret key with the desired key size
    public static SecretKey generateSecretKey(int keySize) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();
    }

    // Generate a random IV (Initialization Vector)
    public static byte[] generateIV() {
        byte[] iv = new byte[16]; // IV size is typically 16 bytes for AES
        new java.security.SecureRandom().nextBytes(iv);
        return iv;
    }

    // Write byte array to a file
    public static void writeToFile(String filePath, byte[] data) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(data);
        fos.close();
    }

    private void displayPerformanceMetrics() {
        System.out.println("Encryption Time: " + encryptionTime + " nanoseconds");
    }
    
    public static void main(String[] args) throws Exception {
 
        launch(args);
    }
}
