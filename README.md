# CryptiFY 🔐 🗞️
## Fortifying Data Security through AES Encryption and Decryption 💂‍♀️ 💂

# Introduction 🔑 📜

In contemporary society, data security has emerged as a critical concern due to the escalating frequency and severity of data breaches. These breaches represent instances where sensitive information is compromised, leading to severe consequences for individuals, organizations, and even nations. Encryption serves as a crucial defense mechanism against data breaches, ensuring that even if attackers gain access to data, they cannot decipher its contents without the appropriate decryption keys. Therefore, the development of robust encryption solutions, such as the proposed "CryptiFY" application, is imperative in addressing the persistent threat of data breaches and enhancing overall data security in the digital age.

# Project Flow ⛵

### Encryption 🔒 

- Accept user input for the secret code, file to be encrypted, and desired key size. 📁
- Encrypt the file using AES encryption and the secret code with a combination of Vigenère and Caesar ciphers. 🗝️
- Establish connection with the Database 🏪
- Store the secret key, IV, encrypted secret code, and encrypted file in a database. 🏠
- Display Success message if done else throw Error. :heavy_check_mark:	
  
![encrypt](https://github.com/AsmitaMondal/CryptiFY/assets/108891810/88cbd5db-cf85-4760-a9d7-2afc17d97407)

  

### Decryption 🔓

- Accept encrypted file, secret code, secret key, and IV from the user. 📂
- Retrieve the encrypted secret code from the database based on the secret key and IV combination. 🏠
- Decrypt the encrypted secret code retrieved from the database with the given user input secret code. 🎁
- Decrypt the file if the decrypted secret code, secret key, and IV combination match the database records. 📰
- Display error if any of the inputs is faulty. ❎


![decrypt](https://github.com/AsmitaMondal/CryptiFY/assets/108891810/57622404-8ac8-4697-a3e7-f6c26956c450)

## Supported Files 🗃️

- .csv
- .txt
- .xlsx
- .doc
- .docx

# Demo Video 👓



https://github.com/AsmitaMondal/CryptiFY/assets/108891810/79d82ac6-b1a5-4924-8464-806e0c38bbe5



# Purpose 💖

The purpose of this project is to address the critical need for robust data security measures in an increasingly digital world. With the proliferation of sensitive information stored and transmitted electronically, the risk of unauthorized access, data breaches, and cyberattacks has become ever-present. By focusing on encryption and decryption techniques, the project aims to fortify data security and protect valuable information from potential threats. Through the implementation of advanced encryption algorithms such as AES (Advanced Encryption Standard), along with Classical Substitution Ciphers in Java, the project seeks to establish a reliable framework for safeguarding sensitive data across various platforms and environments. Ultimately, the project's overarching goal is to contribute to the creation of a safer digital landscape where individuals can confidently engage in online activities, businesses can protect their assets and reputation, and society as a whole can benefit from the secure exchange of information.

# Requirement Specifications 🗄️

The following have been used to create this project:

### Software 🚀

**Development Environment:**

1. Eclipse IDE for Java Developers (Version 2024-03)
2. MySQL Workbench (Version 8.0)
3. Java Development Kit (JDK) (Version 21.0.1 LTS)

**Java Libraries and Dependencies:**

1. Java Cryptography Extension (JCE)
2. JavaFX SDK (Version 22)
3. Connector/J JDBC Driver (Version 8.3.0)

### Hardware 🪛

**Computer System:**
Dell Inspiron 15 3525 Laptop

**Processor:**
AMD Ryzen 7 5825U with AMD Radeon (TM) Graphics, 2000 Mhz, 8 Core(s), 16 Logical Processor(s)

**Memory (RAM):**
16GB RAM

**Operating System:**
Microsoft Windows 11 Home

**Display Size:**
15.6 inches 

**Display Resolution:**
Full HD WVA Display

# Performance Comparison ⚖️

A comparison was performed on the files that could be used for the project and the following was observed:
- Key Size: `128 bits`
- Secret Code: `SHILADITYA`

![Screenshot 2024-04-13 230623](https://github.com/AsmitaMondal/CryptiFY/assets/108891810/b18ce441-3034-4e99-aecf-3244acf4893e)

*Table: Comparison of Time for Different File Types*

### Inference 🎇

`CSV` files took least time to perform all processes while `TXT` files took the most. Even though the difference in time is less, it still has a significant impact when the size of data is even bigger.
This difference can be accounted for by the presence of free-form text in TXT files and structured, well defined data in CSV or XLSX files which makes it easier to find patterns and make the process faster. TXT files require more time for parsing and processing as compared to the compressed, patterned CSV files. 


# Future Scope 👀

- To incorporate more file types such as audio, images and pdf thus streamlining the process for better usability and scalability.
- To create a deployable web application that can be accessed by users globally.
- To use more encryption layers, algorithms and techniques so as to make the process more robust and defiant to attacks.

# Additional Information 😃

1. The [`documentation`](https://github.com/AsmitaMondal/CryptiFY/tree/main/Documentation) can be refered to for detailed understanding of the project.
2. All codes are uploaded in their respective folders.
3. [`OtherEssential`](https://github.com/AsmitaMondal/CryptiFY/tree/main/OtherEssentials)contains jar files required for the working of this project.
4. This was done under the guidance of Dr. Vyshali Gogi [ Christ (Deemed to be University) Bangalore, Central Campus]
