package com.kubasz561.roulette.common;

import java.io.Serializable;

//TODO: Opakować ładnie obiektowo jsona i metodki do niego do jakiegoś parsowania z raw_stringa do słownika Javowego
public class JSONMessage implements Serializable{
    public String rawJSONString;
    public MessageType msgType;
    public JSONMessage(String rawMessage, MessageType type)
    {
        rawJSONString = rawMessage;
        msgType = type;
    }
}
