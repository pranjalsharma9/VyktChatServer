package com.pranjal.models;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by Pranjal on 23-10-2016.
 * The message class.
 */
public class Message implements Externalizable {

    public enum Expression {
        HAPPINESS,
        SADNESS,
        ANGER,
        DISGUST,
        SURPRISE,
        FEAR,
        NEUTRAL
    }

    public enum MessageType {
        TERMINATOR,
        CONNECTION_REQUEST,
        SEND_REQUEST,
        RECEIVE_REQUEST,
        GET_CLIENT_LIST_REQUEST,
        CLIENT_LIST_RESPONSE,
        DISCONNECTION_REQUEST
    }

    private static final long serialVersionUID = 12345L;
    public long timestamp;
    public String sender;
    public String receiver;
    public MessageType type;
    public String text;
    public Expression expression;

    public Message(long timestamp, String sender, String receiver,
                   MessageType type, String text, Expression expression) {
        this.timestamp = timestamp;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.text = text;
        this.expression = expression;
    }

    public Message() {
        this.timestamp = 0L;
        this.sender = null;
        this.receiver = null;
        this.type = null;
        this.text = null;
        this.expression = null;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(timestamp);
        out.writeUTF(sender);
        out.writeUTF(receiver);
        out.writeInt(type.ordinal());
        out.writeUTF(text);
        out.writeInt(expression.ordinal());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        timestamp = in.readLong();
        sender = in.readUTF();
        receiver = in.readUTF();
        type = MessageType.values()[in.readInt()];
        text = in.readUTF();
        expression = Expression.values()[in.readInt()];
    }

}
