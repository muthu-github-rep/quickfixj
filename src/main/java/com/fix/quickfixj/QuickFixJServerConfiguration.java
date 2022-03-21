package com.fix.quickfixj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import quickfix.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import  quickfix.Session;

@Component
public class QuickFixJServerConfiguration {

    private SessionSettings sessionSettings;
    private SocketAcceptor socketAcceptor;
    private MessageFactory messageFactory;
    private SessionID  sessionID;
    private DataDictionary dataDictionary;
    @Value("${fixDataDictionary}")
    private String dictionaryPath;

    @Autowired
    public Application application;

    @PostConstruct
    public void initialize(){
        try {
            dataDictionary=new DataDictionary(dictionaryPath);
        } catch (ConfigError configError) {
            configError.printStackTrace();
        }
    }
    @PreDestroy
    public void postDestroy(){
        if (sessionID !=null){

        }
    }
    public void loadConfiguration(){
        try {
            sessionSettings=new SessionSettings("src/main/resources/config/acceptor.cfg");
        } catch (ConfigError configError) {
            configError.printStackTrace();
        }
    }
    public void logon(){
        if(isValidConfiguration()){
            FileStoreFactory fileStoreFactory = new FileStoreFactory(sessionSettings);
            FileLogFactory fileLogFactory = new FileLogFactory(sessionSettings);
            if(messageFactory==null){
                messageFactory = new DefaultMessageFactory();
            }
            Application application = new TradeAppAcceptor();
            try {
                socketAcceptor= new SocketAcceptor(application,fileStoreFactory,sessionSettings,fileLogFactory,messageFactory);
                socketAcceptor.start();
                sessionID=socketAcceptor.getSessions().get(0);
                Session.lookupSession(sessionID).logon();
            } catch (ConfigError configError) {
                configError.printStackTrace();
            }
        }
    }
    private boolean isValidConfiguration(){
        boolean flag=false;

        if(sessionSettings!=null && application !=null){
            flag=true;
        }
        return flag;
    }
    public void sendMessage(){
        String fixMsg="8=FIX.4.4\u00019=289\u000135=8\u000134=1090\u000149=TESTSELL1\u000152=20180920-18:23:53.671\u000156=TESTBUY1\u00016=113.35\u000111=636730640278898634\u000114=3500.0000000000\u000115=USD\u000117=20636730646335310000\u000121=2\u000131=113.35\u000132=3500\u000137=20636730646335310000\u000138=7000\u000139=1\u000140=1\u000154=1\u000155=MSFT\u000160=20180920-18:23:53.531\u0001150=F\u0001151=3500\u0001453=1\u0001448=BRK2\u0001447=D\u0001452=1\u000110=151\u0001";
        Message message=getFixMessage(fixMsg);
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
    public void logOut(){
        Session.lookupSession(sessionID).logout();
    }
}
