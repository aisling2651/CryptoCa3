/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CryptoCa3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import java.security.Key;
import java.security.SecureRandom;

import java.util.Scanner;

/**
 *
 * @author aisli
 */
public class CryptoCa3
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {
        //Generate key
        Key secretKey = GenerateKey();

        System.out.println("Enter user name: ");
        Scanner keyboard = new Scanner(System.in);
        String user = keyboard.next();
        if (checkUser(user) == false)
        {
            writeToFile(secretKey, user);
            //display key
            System.out.println("Secret Key: \t" + secretKey + "\n");

            //encypt message using key
            String message = "Hello World";
            //get array of bytes from encryptData message
            byte[] cipherText = encryptData(message, secretKey);

            //if array is not null -> output
            if (cipherText != null)
            {
                //Outputting the encrypted message 
                System.out.print("Encrypted data: \t");
                for (int i = 0; i < cipherText.length; i++)
                {
                    System.out.print(cipherText[i] + " ");
                }

            }

            String plainText = decryptData(cipherText, secretKey);
            if (plainText != null)
            {
                //Outputting the encrypted message 
                System.out.print("\nDecrypted data: \t" + plainText);

                System.out.println("\n");
            }
        }
        else
        {
            System.out.println("User already exists");
        }

    }

    //Reference: https://www.tutorialspoint.com/java_cryptography/java_cryptography_keygenerator.htm
    //This website helped me understand how to use the Key Generator class in the crypto library to generate a secure random key 
    //Creating a KeyGenerator object
    public static Key GenerateKey()
    {
        Key key = null;
        try
        {
            //getting instance of key generator of type AES
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");

            //Creating a SecureRandom object
            SecureRandom secRandom = new SecureRandom();

            //Initializing the KeyGenerator
            //keyGen.init(128);
            //Throws an error when trying to encrypt as JAva only supports 128 but encryption
            //keyGen.init(256);
            //secRandom - strong random number generator
            keyGen.init(secRandom);

            //Creating/Generating a key
            key = keyGen.generateKey();

        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
        }

        return key;

    }

    //Method stores user name and key generated
    public static void writeToFile(Key secretKey, String user) throws Exception
    {
        try
        {
            Writer output;
            //clears file every time
            output = new BufferedWriter(new FileWriter("C:/Users/aisli/OneDrive/Documents/NetBeansProjects/CryptoCa3/keyStore.txt", true));
            output.append(user + "~" + secretKey.toString() + "\n");
            output.close();

            System.out.println("Details Stored.");

        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
        }

    }

    //checks if user already exists in the file
    public static boolean checkUser(String user) throws FileNotFoundException, IOException
    {
        boolean found = false;
        File file = new File("C:/Users/aisli/OneDrive/Documents/NetBeansProjects/CryptoCa3/keyStore.txt");

        Scanner sc = new Scanner(file);
        while (sc.hasNextLine())
        {
            String currentLine = sc.nextLine();
            String[] line = currentLine.split("~");
            String name = line[0];
            if (user.equalsIgnoreCase(name))
            {
                found = true;
            }

        }
         return found;
    }

    //Encrypts the message using the key and cipher class
    public static byte[] encryptData(String plaintext, Key secretKey)
    {
        byte[] cipherText = null;
        System.out.println("*** En/Decrypting message using AES algorithm ***");
        try
        {

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            cipherText = cipher.doFinal(plaintext.getBytes());

            System.out.println("Original Data: \t\t" + plaintext);

        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
        }
        return cipherText;
    }

    //decrypts method using key and cipher class
    public static String decryptData(byte[] cipherText, Key secretKey)
    {
        String plaintext = null;
        try
        {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] plainBytesDecrypted = cipher.doFinal(cipherText);
            plaintext = new String(plainBytesDecrypted);

        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);

        }
        return plaintext;

    }

}
