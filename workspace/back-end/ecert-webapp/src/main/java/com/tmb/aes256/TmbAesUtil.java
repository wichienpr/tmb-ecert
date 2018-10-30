package com.tmb.aes256;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
public class TmbAesUtil {

	public static String encrypt(String keyStorePath,String password) throws Exception {

		TmbKeyStore keyStore = new TmbKeyStore();
		
		SecretKey keyFound = keyStore.loadSecretKey(keyStorePath);

		// Generate the secret key specs.
		byte[] raw = keyFound.getEncoded();

		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

		// Instantiate the cipher

		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		byte[] encrypted = cipher.doFinal((password).getBytes());
		String encrypt = Base64.encodeBase64String(encrypted);
		System.out.println("Encrypt Password: " + encrypt);
		return encrypt;
	}

	public static String decrypt(String keyStorePath,String encryptedText) throws Exception {

		TmbKeyStore keyStore = new TmbKeyStore();

		SecretKey keyFound = keyStore.loadSecretKey(keyStorePath);

		// Generate the secret key specs.
		byte[] raw = keyFound.getEncoded();

		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypt = cipher.doFinal(Base64.decodeBase64(encryptedText));
		String decryptString = new String(decrypt);
		System.out.println("Descypt Password: " + decryptString);
		return decryptString;
	}


}
