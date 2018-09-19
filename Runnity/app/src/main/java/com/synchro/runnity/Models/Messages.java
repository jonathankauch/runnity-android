package com.synchro.runnity.Models;

/**
 * Created by Shirco on 01/03/2016.
 */
public class Messages {
    private Message[] messages;

    public Messages() {
        this.messages = new Message[]{};
    }

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }
}

