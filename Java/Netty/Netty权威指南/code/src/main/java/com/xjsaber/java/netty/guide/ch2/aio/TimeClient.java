package com.xjsaber.java.netty.guide.ch2.aio;

public class TimeClient {

    public static void main(String[] args){
        int port = 8080;
        if (args != null && args.length > 0){
            try{
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException ignored){}
        }
        new Thread(new AsyncTimeClientHandler("", port)).start();
    }
}
