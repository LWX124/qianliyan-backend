package com.jeesite.modules.util2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {

    private static Logger logger = LoggerFactory.getLogger(SerializeUtil.class);

    // 对象序列化成字数组
    public static byte[] serialize(Object object) {

        ObjectOutputStream oos = null;

        ByteArrayOutputStream baos = null;

        try {


            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeObject(object);

            return baos.toByteArray();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (baos != null) {
                    baos.close();
                }

            } catch(Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }


    // 字节数组反序列化成对象
    public static Object unserialize(byte[] bytes) {

        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;

        try {

            bais = new ByteArrayInputStream(bytes);

            ois = new ObjectInputStream(bais);

            return ois.readObject();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

        } finally {

            try {
                if (bais != null) {
                    bais.close();
                }

                if (ois != null) {
                    ois.close();
                }
            } catch(Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return null;

    }
}