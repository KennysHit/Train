package com.example.train;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

class Demo {
    public static String calcAuthorization ( String source , String secretId , String secretKey , String datetime )
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String signStr = "x-date: " + datetime + "\n" + "x-source: " + source;
        Mac mac = Mac.getInstance ( "HmacSHA1" );
        Key sKey = new SecretKeySpec ( secretKey.getBytes ( "UTF-8" ) , mac.getAlgorithm ( ) );
        mac.init ( sKey );
        byte[] hash = mac.doFinal ( signStr.getBytes ( "UTF-8" ) );
        String sig = new BASE64Encoder ( ).encode ( hash );

        String auth = "hmac id=\"" + secretId + "\", algorithm=\"hmac-sha1\", headers=\"x-date x-source\", signature=\"" + sig + "\"";
        return auth;
    }

    public static String urlencode ( Map< ?, ? > map ) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder ( );
        for (Map.Entry< ?, ? > entry : map.entrySet ( )) {
            if ( sb.length ( ) > 0 ) {
                sb.append ( "&" );
            }
            sb.append ( String.format ( "%s=%s" ,
                    URLEncoder.encode ( entry.getKey ( ).toString ( ) , "UTF-8" ) ,
                    URLEncoder.encode ( entry.getValue ( ).toString ( ) , "UTF-8" )
            ) );
        }
        return sb.toString ( );
    }
}