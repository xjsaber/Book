package com.xjsaber.java.network.ch9;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * 大多数工作都在处理一个数据格式（32位无符号整数）
 * @author xjsaber
 */
public class TimeServer {

    private final static int PORT = 37;

    public static void main(String[] args){

        /**
         * 时间协议将时间起点设置为1900年，
         * 而Date类设置为从1970年开始计算。利用下面的这个数
         * 可以在二者之间进行转换
         */
        long differenceBetweenEpochs = 2208988800L;

        try (ServerSocket server = new ServerSocket(PORT)){
            while (true){
                try (Socket connection = server.accept()){
                    OutputStream out = connection.getOutputStream();
                    Date now = new Date();
                    long msSince1970 = now.getTime();
                    long secondsSince1970 = msSince1970/1000;
                    long secondsSince1900 = secondsSince1970 + differenceBetweenEpochs;
                    byte[] time = new byte[4];
                    time[0] = (byte)((secondsSince1900 & 0x00000000FF000000L) >> 24);
                    time[1] = (byte)((secondsSince1900 & 0x0000000000FF0000L) >> 16);
                    time[2] = (byte)((secondsSince1900 & 0x000000000000FF00L) >> 8);
                    time[3] = (byte)((secondsSince1900 & 0x00000000000000FFL));
                    out.write(time);
                    out.flush();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
