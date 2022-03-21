package com.fix.quickfixj;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import quickfix.*;

import javax.annotation.PostConstruct;

@Component
public class TradeMessageUtil {
    private DataDictionary dataDictionary;

    @Value("${fixDataDictionary}")
    private String dictionaryPath;

    @PostConstruct
    public void initialize(){
        try {
            dataDictionary=new DataDictionary(dictionaryPath);
        } catch (ConfigError configError) {
            configError.printStackTrace();
        }
    }
    public void sendMessage(SessionID sessionID){
        String fixMsg="8=FIX.4.4\u00019=289\u000135=8\u000134=1090\u000149=TESTSELL1\u000152=20180920-18:23:53.671\u000156=TESTBUY1\u00016=113.35\u000111=636730640278898634\u000114=3500.0000000000\u000115=USD\u000117=20636730646335310000\u000121=2\u000131=113.35\u000132=3500\u000137=20636730646335310000\u000138=7000\u000139=1\u000140=1\u000154=1\u000155=MSFT\u000160=20180920-18:23:53.531\u0001150=F\u0001151=3500\u0001453=1\u0001448=BRK2\u0001447=D\u0001452=1\u000110=151\u0001";
        Message message=getFixMessage(fixMsg);
        System.out.println(" fixMsg "+fixMsg);
        Session.lookupSession(sessionID).send(message);
    }
    private Message getFixMessage(String message){
        Message fixMsg=null;
        message=message.replace(System.getProperty("line.separator"),"");
        try {
            fixMsg=new Message(message,dataDictionary,false);
        } catch (InvalidMessage invalidMessage) {
            invalidMessage.printStackTrace();
        }
        return fixMsg;
    }
}
