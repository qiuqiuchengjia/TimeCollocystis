package com.framework.util;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by mahui on 15/12/4.
 */
public class Rsa {
    private static final String ALGORITHM = "RSA";
    public static final String pubk = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDHBX0VDYyP8Ea2zGkfEXyummX1Kt1+Hpjv9KAceZGIU2Sm1OrnzveVxOmkef7D0M1IeA2BNSyFDAtcmQOPhhcSrAsHtM3fhpkI/zz55OKZXBggTB1A/c/aL3S2KxzMH1r6oiAlIVi96ZqMxBySgFlz1TdGMQjW2kzshZt3PMNlXQIDAQAB";
    public static final String prik = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMcFfRUNjI/wRrbMaR8RfK6aZfUq3X4emO/0oBx5kYhTZKbU6ufO95XE6aR5/sPQzUh4DYE1LIUMC1yZA4+GFxKsCwe0zd+GmQj/PPnk4plcGCBMHUD9z9ovdLYrHMwfWvqiICUhWL3pmozEHJKAWXPVN0YxCNbaTOyFm3c8w2VdAgMBAAECgYB6PU/HZ4wHOKBmVITYlUgMgj7PZ+WlOivbblgNRUny/q2anrotaPy+27RZ1qh5Iz2DHmHFE6DlkzFDTaRXOhfexAhmuCyUCre/eQCYs7fquCKRnxGiJKfzWCKrmpVfa8uN8IKx+w7XW/L8V+NKewyZxdfKrpnCW1ipLJl2X5/xYQJBAPzDfmn5W6U4G2MRWxAHcyRH0wHa0gX0tuHNamTDXE5F+rG6f3YdaXOmQqb5UbGOdqwLcxuqi4nG8ibTdncTDcUCQQDJkdbQdbcJWeSecL2HMWKSa2fYLR7aLkKZZ5kgiziX/zGbxRXMtFJtTYJIvh4gMpC5TjcBeN85ICk7180Frcq5AkA9c1W/c+Uo9MwLuMN30SfYETImiEl5MzZnDSapmqlNSkwZe2rTj3D8sdYQfBq1XRpS13HDryG/fEOQ1uY9Kr+1AkEAihQrUTWyIue/YoHccyXzXlXRFxchkVgrKyb6kH6OUCDyfMBvhIDqDhIQc4A45tl8plPb/lgsoddUJ7hWq9M3kQJAGOJZvgb62C//wApNqOlVwdxmmonlj7peHvy3eTghCZcN4YCvGJMIeEdGLRi9tFLOezSwEU+Pe8yFdzbNgBl7CA==";

    /**
     * @param algorithm
     * @param ins
     * @return
     * @throws java.security.NoSuchAlgorithmException
     * @throws AlipayException
     */
    private static PublicKey getPublicKeyFromX509(String algorithm,
                                                  String bysKey) throws NoSuchAlgorithmException, Exception {
        byte[] decodedKey = Base64.decode(bysKey);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(x509);
    }

    public static String encrypt(String content,String key) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, key);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);

            byte plaintext[] = content.getBytes("UTF-8");
            byte[] output = cipher.doFinal(plaintext);
            String s = new String(Base64.encode(output));

            return s;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String content,String key) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decode(key));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priKey);

            byte[] encodedKey = Base64.decode(content);

            byte[] output = cipher.doFinal(encodedKey);

            //String s = new String(Base64.encode(output));
            String s = new String(output);

            return s;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static String sign(String content, String privateKey) {
        String charset = "UTF-8";
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(charset));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String getMD5(String content) {
        String s = null;
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(content.getBytes());
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory
                    .generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));
            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
