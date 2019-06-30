package com.smartdev.fishfarm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectSerializer {
    public static String serialize(Serializable obj) throws IOException {
        if (obj == null) {
            return "";
        }
        ByteArrayOutputStream serialObj = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(serialObj);
        objStream.writeObject(obj);
        objStream.close();
        return encodeBytes(serialObj.toByteArray());
    }

    public static Object deserialize(String str) throws IOException, ClassNotFoundException {
        if (str != null) {
            if (str.length() != 0) {
                return new ObjectInputStream(new ByteArrayInputStream(decodeBytes(str))).readObject();
            }
        }
        return null;
    }

    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 15) + 97));
            strBuf.append((char) ((bytes[i] & 15) + 97));
        }
        return strBuf.toString();
    }

    public static byte[] decodeBytes(String str) {
        byte[] bytes = new byte[(str.length() / 2)];
        for (int i = 0; i < str.length(); i += 2) {
            bytes[i / 2] = (byte) ((str.charAt(i) - 97) << 4);
            int i2 = i / 2;
            bytes[i2] = (byte) (bytes[i2] + (str.charAt(i + 1) - 97));
        }
        return bytes;
    }
}
