package com.appdsn.commonbase.scheme.config;

import java.util.ArrayList;

public class ChatInfoEntity {
    private ArrayList<ChatInfo> chatInfos;

    public static class ChatInfo {
        public int chatId;
        public String deviceId;

        public int getChatId() {
            return chatId;
        }

        public void setChatId(int chatId) {
            this.chatId = chatId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }

    public ArrayList<ChatInfo> getChatInfos() {
        return chatInfos;
    }

    public void setChatInfos(ArrayList<ChatInfo> chatInfos) {
        this.chatInfos = chatInfos;
    }
}
