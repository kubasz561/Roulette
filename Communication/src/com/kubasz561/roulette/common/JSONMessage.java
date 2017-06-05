package com.kubasz561.roulette.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


// tak robimy i wyciągamy jsony
/*      JSONMessage json = JSONMessageBuilder.create_message(SET_BET, "noob", "red","200","xx123","hasdDDD1234");
        String login = json.getDictionary().get("login");
        String password = json.getDictionary().get("password");
        String bet = json.getDictionary().get("bet");
        String value = json.getDictionary().get("value");
*/


//TODO: Opakować ładnie obiektowo jsona i metodki do niego do jakiegoś parsowania z raw_stringa do słownika Javowego
public class JSONMessage implements Serializable{
    //weryfikacja przy odbieraniu wiadomosci
    //czytanie jsona bezposrednio ze streama i weryfikacja - biblioteka
    //skad wiadomo ze json sie skonczyl w readobject
    public String rawJSONString;
    public MessageType msgType;
    private Map<String, String> dictionary = new HashMap<>();
    public JSONMessage(String rawMessage, MessageType type)
    {
        rawJSONString = rawMessage;
        msgType = type;
        try{
            parseJSONString(rawJSONString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJSONString(String JSONtoParse) throws Exception{
        String delims = "[ \\{ ' , : \\}]+";
        String[] tokens = JSONtoParse.split(delims);
        int i = 0;
        if(tokens[0].equals(""))
            i++;
        while(i<tokens.length) {
            try{
                if (tokens[i + 1] != null) {
                    switch (tokens[i]) {
                        case "login":
                            dictionary.put("login", tokens[i + 1]);
                            break;
                        case "password":
                            dictionary.put("password", tokens[i + 1]);
                            break;
                        case "response":
                            dictionary.put("response", tokens[i + 1]);
                            break;
                        case "request":
                            dictionary.put("request", tokens[i+1]);
                            break;
                        case "bet":
                            dictionary.put("bet", tokens[i+1]);
                            break;
                        case "value":
                            dictionary.put("value", tokens[i+1]);
                            break;
                        case "session_number":
                            dictionary.put("session_number", tokens[i+1]);
                            break;
                        case "timestamp":
                            dictionary.put("timestamp", tokens[i+1]);
                            break;
                        case "round_time":
                            dictionary.put("round_time", tokens[i+1]);
                            break;
                        case "account_balance":
                            dictionary.put("account_balance", tokens[i+1]);
                            break;
                        case "result":
                            dictionary.put("result", tokens[i+1]);
                            break;
                        case "bet_list":
                            dictionary.put("bet_list", tokens[i+1]);
                            break;
                        default:
                            break;//throw new Exception("Json invalid");

                    }
                }
            }catch(ArrayIndexOutOfBoundsException e){
                e.printStackTrace(System.out);
            }
            i = i+2;
        }

    }
    public String getRawJSONString() {
        return rawJSONString;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public Map<String, String> getDictionary() {
        return dictionary;
    }

}