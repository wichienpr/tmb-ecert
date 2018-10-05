package com.tmb.ecert.common.utils;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.util.Base64;

import javax.crypto.SecretKey;

public class ApplicationKeyStore {
	
	static final String keys= "TR6yH72V3w";
	
	public ApplicationKeyStore() throws Exception{
		
	}
    

    public  String base64String(SecretKey secretKey) {
        return new String(Base64.getEncoder().encode(secretKey.getEncoded()));
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
        final KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(new FileInputStream(keyStoreFile), keys.toCharArray());
        return keyStore;
    }
}
