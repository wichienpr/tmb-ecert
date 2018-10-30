package com.tmb.aes256;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class TmbKeyStore {
	
	static final String keys= "etM256ACeresbt";
	
	public TmbKeyStore() throws Exception{
		
	}
	
	public  void storeSecretKey(String keyStoreFile) throws Exception {
		
    	KeyStore keyStore = createKeyStore(keyStoreFile);
		
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
	    kgen.init(256); // 192 and 256 bits may not be available
	    SecretKey secretKey = kgen.generateKey();
	    
	    // store the secret key
        KeyStore.SecretKeyEntry keyStoreEntry = new KeyStore.SecretKeyEntry(secretKey);
        PasswordProtection keyPassword = new PasswordProtection("pw-secret".toCharArray());
        keyStore.setEntry("mySecretKey", keyStoreEntry, keyPassword);
        keyStore.store(new FileOutputStream(keyStoreFile), keys.toCharArray());
    }
    
    public  SecretKey loadSecretKey(String keyStorePath) throws Exception {
    	KeyStore keyStore = createKeyStore(keyStorePath);
    	
    	PasswordProtection keyPassword = new PasswordProtection("pw-secret".toCharArray());
    	
    	// retrieve the stored key back
        KeyStore.Entry entry = keyStore.getEntry("mySecretKey", keyPassword);
        SecretKey keyFound = ((KeyStore.SecretKeyEntry) entry).getSecretKey();
        
        return keyFound;
    }

    private  KeyStore createKeyStore(String keyStoreFile) throws Exception {
    	
    	File file = new File(keyStoreFile);

        final KeyStore keyStore = KeyStore.getInstance("JCEKS");
        if (file.exists()) {
            // .keystore file already exists => load it
            keyStore.load(new FileInputStream(file), keys.toCharArray());
        } else {
            // .keystore file not created yet => create it
            keyStore.load(null, null);
            keyStore.store(new FileOutputStream(keyStoreFile), keys.toCharArray());
        }

        return keyStore;
    }
}
