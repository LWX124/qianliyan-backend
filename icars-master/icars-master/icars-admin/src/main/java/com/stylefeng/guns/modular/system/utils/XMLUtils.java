package com.stylefeng.guns.modular.system.utils;

import com.stylefeng.guns.modular.system.vo.ReceiveMsg;
import com.stylefeng.guns.modular.system.vo.ReplyMsg;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class XMLUtils {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * @Description: 接收的xml格式转换为model实体
     * @params:  * @param
     * @Author: lvyq
     * @Date: 2023/3/7 15:38
     */
    public static ReceiveMsg XMLTOModel(HttpServletRequest request){
        ReceiveMsg receiveMsg= new ReceiveMsg();
        try {
            InputStream inputStream;
            StringBuffer sb = new StringBuffer();
            inputStream = request.getInputStream();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            Document document = DocumentHelper.parseText(String.valueOf(sb));
            Element root = document.getRootElement();
            receiveMsg.setToUserName(root.elementText("ToUserName"));
            receiveMsg.setFromUserName(root.elementText("FromUserName"));
            receiveMsg.setMsgType(root.elementText("MsgType"));
            receiveMsg.setContent(root.elementText("Content"));
            receiveMsg.setCreateTime(root.elementText("CreateTime"));
            receiveMsg.setMsgId(root.elementText("MsgId"));
            receiveMsg.setMsgDataId(root.elementText("MsgDataId"));
            receiveMsg.setIdx(root.elementText("Idx"));
            receiveMsg.setEvent(root.elementText("Event"));
            in.close();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return receiveMsg;
    }

    /**
     * @Description: 对象转xml
     * @params:  * @param data
     * @return: {@link java.lang.String}
     * @Author: lvyq
     * @Date: 2023/3/7 21:37
     */
    public static String ObjToXml(ReplyMsg data){
        JAXBContext context = null;
        Marshaller marshaller=null;
        String xmlObj="";
        try {
            context = JAXBContext.newInstance(ReplyMsg.class);
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(data, baos);
            xmlObj= baos.toString();
        }catch(Exception   e){
            e.printStackTrace();
        }
        return xmlObj;
    }
}
