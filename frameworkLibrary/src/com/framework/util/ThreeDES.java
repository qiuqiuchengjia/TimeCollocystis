package com.framework.util;

/*
 * 当前文件：ThreeDES.java
 * 作者：许德鹏
 * 时间：2015-01-17
 * 描述：使用3DES通过用户id加密
 */

import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * 
 * 使用DES加密与解密
 * 方法: void getKey(String strKey)从strKey的字条生成一个Key
 * 
 * encryptByUserid(String userid, String src)对src进行加密,返回String密文 String
 * decryptByUserid(String userid, String encoded)对encoded进行解密,返回String明文
 */

public class ThreeDES {
	private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish
    

    /**
     * 根据用户id加密
     * @param src 需要加密的字符串
     * @throws UnsupportedEncodingException 
     * */
    public static String encryptByUserid(String userid, String src) throws UnsupportedEncodingException {
    	byte[] keyBytes = getPassword(userid);    //keybyte为加密密钥，长度为24字节
    	
    	try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
            //加密
            Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            
            byte[] encoded = c1.doFinal(src.getBytes("UTF-8"));
            return new String(encoded,"ISO-8859-1");
            
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    
    /**
     * 根据用户id解密 
     * @param encoded 加密后的字符串
     * @throws UnsupportedEncodingException 
     * */
    public static String decryptByUserid(String userid, String encoded) throws UnsupportedEncodingException {
    	byte[] keyBytes = getPassword(userid);    //keybyte为加密密钥，长度为24字节
    	
    	try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);

            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            
            byte[] srcBytes = c1.doFinal(encoded.getBytes("ISO-8859-1"));//encoded为加密后的缓冲区
            return new String(srcBytes,"UTF-8");
            
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    
    /**
     * 通过用户id获取加密解密对称秘钥 24字节的密钥
     * */
    public static byte[] getPassword(String userid){
    	if(userid==null) userid="";
    	String password_="";
    	if(userid.length()<24){
			for(int i=userid.length();i<24;i++){
				password_=password_+"0";
			}
		}else{
			password_=userid.substring(0, 24);
		}
    	return password_.getBytes();
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException
    {
    	String userid="005002b2-5a9f-4aa7-a06a-ba681b5264a2";
//    	String userid=null;
        String szSrc = "{\"ret\":0,\"msg\":\"传入参数未设定\"}";
        
        System.out.println("加密前的字符串:" + szSrc);
        
        String encoded = encryptByUserid(userid, szSrc);       
        System.out.println("加密后的字符串:" + encoded);
        
        String srcBytes = decryptByUserid(userid, encoded);
        System.out.println("解密后的字符串:" + srcBytes);
        
    }

}