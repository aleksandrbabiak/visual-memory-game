package com.visualmemory;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.InvalidProtocolBufferException;

import com.protocol.VisualMemoryProtocol;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import com.visualmemory.database.DBManager;
import com.visualmemory.database.entity.GameResult;
import com.visualmemory.database.entity.ServerRecord;
import com.visualmemory.game.DataCurrentUser;


public class VisualMemoryClient extends Thread {

    private static final String LOG_TAG = "ClientLogs";
    private Handler handler;
    private static final byte INSERT_RECORD_MESSAGES = 0x02;
    private static final byte UPDATE_RECORD_MESSAGES = 0x03;
    private static final byte GET_TOP_100_MESSAGES = 0x04;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private BufferedInputStream in;
    private DataInputStream dataInputStream;
    private DBManager dbManager;
    private Context context;


    public VisualMemoryClient(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        dbManager = new DBManager(context);

        Log.d(LOG_TAG, " VisualMemoryClient constractor");
        start();
    }


    private void setConnecting() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName("xx.xx.xxx.xx");
        } catch (UnknownHostException e) {

            e.printStackTrace();

        }
        SocketAddress socketAddress = new InetSocketAddress(inetAddress, 29999);
        socket = new Socket();
        try {
            socket.connect(socketAddress, 5000);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            in = new BufferedInputStream(socket.getInputStream());
            dataInputStream = new DataInputStream(in);
            Log.d(LOG_TAG, "client connecting");
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    private void sendPacket(byte messageType, AbstractMessageLite abstractMessageLite) {
        try {
            byte[] data = abstractMessageLite.toByteArray();
            int length = data.length;
            dataOutputStream.writeByte(messageType);
            dataOutputStream.writeInt(length);
            dataOutputStream.write(data);
            dataOutputStream.flush();
            Log.d(LOG_TAG, "client send Packet");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRecordMessages() {
        GameResult record = dbManager.getUserRecord(DataCurrentUser.getInstans().userID);

        String name = DataCurrentUser.getInstans().userName;
        int durationGame = record.getDurationGame();
        String date = record.getDate();
        double k_factor = record.getkFactor();

        VisualMemoryProtocol.InsertRecord insertRecord = VisualMemoryProtocol.InsertRecord.newBuilder()
                .setUsreName(name)
                .setDate(date)
                .setKFactor(k_factor).build();
        dbManager.insertSendServerRecord(date, durationGame, k_factor, DataCurrentUser.getInstans().userID);
        Log.d(LOG_TAG, "client build RecordMessages");
        sendPacket(INSERT_RECORD_MESSAGES, insertRecord);
    }

    private void sendUpdateRecordMessages() {
        GameResult record = dbManager.getUserRecord(DataCurrentUser.getInstans().userID);
        VisualMemoryProtocol.UpdateRecord updateRecordMessages = VisualMemoryProtocol.UpdateRecord.newBuilder()
                .setGlobalUserID(DataCurrentUser.getInstans().globalUserID)
                .setDate(record.getDate())
                .setKFactor(record.getkFactor()).build();
        Log.d(LOG_TAG, "client build UpdateRecordMessages");
        dbManager.updateSendServerRecord(DataCurrentUser.getInstans().userID, record.getkFactor());
        sendPacket(UPDATE_RECORD_MESSAGES, updateRecordMessages);
    }

    private void sendGetTop100Messages() {
        VisualMemoryProtocol.GetTop100 getTop100 = VisualMemoryProtocol.GetTop100.newBuilder().build();
        Log.d(LOG_TAG, "client build GetTop100Messages");
        sendPacket(GET_TOP_100_MESSAGES, getTop100);
    }

    @Override
    public void run() {
        setConnecting();

        GameResult record = dbManager.getUserRecord(DataCurrentUser.getInstans().userID);
        GameResult sendRecord = dbManager.getSendServerUserRecord(DataCurrentUser.getInstans().userID);
       int globalID = DataCurrentUser.getInstans().globalUserID;
        Log.d(LOG_TAG, "DataCurrentUser.getInstans().globalUserID: "+globalID);

       if (globalID==0 && record!=null){
           Log.d(LOG_TAG, "DataCurrentUser.getInstans().globalUserID==0 && record!=null");
           sendRecordMessages();
       }else{
           if(record!=null && sendRecord!=null && record.getkFactor()>sendRecord.getkFactor()){
               Log.d(LOG_TAG, "record.getkFactor(): "+record.getkFactor()+" sendRecord.getkFactor() :"+sendRecord.getkFactor());
           sendUpdateRecordMessages();
               Log.d(LOG_TAG, "record!=null && sendRecord!=null && record.getkFactor()>sendRecord.getkFactor()");
           }else {
               sendGetTop100Messages();
               Log.d(LOG_TAG, "  }else {");
           }
       }

        try {

            Log.d(LOG_TAG, "client run");
            byte b = dataInputStream.readByte();
            int length = dataInputStream.readInt();
            byte[] data = new byte[length];
            dataInputStream.read(data);
            decodeMessage(data);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void decodeMessage(byte[] data) {
        try {
            VisualMemoryProtocol.Top100 top100Message = VisualMemoryProtocol.Top100.parseFrom(data);

            int globalID = top100Message.getGlobalUserID();
            Log.d(LOG_TAG, "client decoder globalID: "+globalID);

            if(globalID>0){
                DataCurrentUser.getInstans().setGlobalUserID(globalID);
                dbManager.inserGlobalId(DataCurrentUser.getInstans().userID,globalID);
            }

            List<VisualMemoryProtocol.InsertRecord> recordListProto = top100Message.getNewRecordList();
            Log.d(LOG_TAG, "record List Proto size : "+recordListProto.size());
            List<ServerRecord> glogalRecordList = new ArrayList<ServerRecord>();

            for (VisualMemoryProtocol.InsertRecord record : recordListProto) {
            ServerRecord gameResultTop100 = new ServerRecord();
                gameResultTop100.setGlobalUserID(record.getGlobalUserID());
                gameResultTop100.setName(record.getUsreName());
                gameResultTop100.setDate(record.getDate());
                gameResultTop100.setK_factor(record.getKFactor());
                Log.d(LOG_TAG, " for (VisualMemoryProtocol.InsertRecord record : recordListProto) ");
                glogalRecordList.add(gameResultTop100);
            }

            Message message = handler.obtainMessage(0, glogalRecordList);
            handler.sendMessage(message);



        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

}
