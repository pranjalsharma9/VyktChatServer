package com.pranjal.data;

import com.nsit.pranjals.vykt.models.Message;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Pranjal on 31-05-2017.
 * The class that stores all the data for the chat between clients.
 */
public class Data {

    public static ConcurrentHashMap<String, ConcurrentLinkedQueue<Message>> messageMap;
    public static Set<String> onlineClients;

    static {
        messageMap = new ConcurrentHashMap<>();
        onlineClients = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public static String[] getClientList () {
        Set<String> set = messageMap.keySet();
        Object[] objects = set.toArray();
        return Arrays.copyOf(objects, objects.length, String[].class);
    }

    public static ConcurrentLinkedQueue<Message> getMessages (String client) {
        if (!messageMap.containsKey(client)) {
            messageMap.put(client, new ConcurrentLinkedQueue<>());
        }
        return messageMap.get(client);
    }

    public static void addMessage (Message message) {
        getMessages(message.receiver).add(message);
    }

    public static void connectClient (String client) {
        if (!messageMap.containsKey(client)) {
            messageMap.put(client, new ConcurrentLinkedQueue<>());
        }
        onlineClients.add(client);
    }

    public static void disconnectClient (String client) {
        onlineClients.remove(client);
    }

}
