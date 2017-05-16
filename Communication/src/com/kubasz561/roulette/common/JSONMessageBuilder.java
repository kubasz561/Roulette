/**
 * Created by sackhorn on 16.05.17.
 */
package com.kubasz561.roulette.common;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JSONMessageBuilder implements Serializable{
    public static JSONMessageBuilder instance;
    public static final Map<MessageType, String> typeToStringTemplateMap = new HashMap<MessageType,String>();

    public static JSONMessageBuilder getInstance()
    {
        if(instance==null)
        {
            instance = new JSONMessageBuilder();
            return instance;
        }
        else
            return instance;
    }

    //TODO: Wprowadzić template'y do fomratowania stringów z komunikatami
    private JSONMessageBuilder()
    {
        typeToStringTemplateMap.put(MessageType.SIGN_UP, "");
        typeToStringTemplateMap.put(MessageType.SIGN_UP_OK, "");
        typeToStringTemplateMap.put(MessageType.LOGIN_DUPLICATE, "");
        typeToStringTemplateMap.put(MessageType.LOG_IN, "");
        typeToStringTemplateMap.put(MessageType.WRONG_PASS, "");
        typeToStringTemplateMap.put(MessageType.BLOCKED, "");
        typeToStringTemplateMap.put(MessageType.LOG_OUT, "");
        typeToStringTemplateMap.put(MessageType.SET_BET, "");
        typeToStringTemplateMap.put(MessageType.BET_OK, "");
        typeToStringTemplateMap.put(MessageType.BET_UNABLE, "");
        typeToStringTemplateMap.put(MessageType.TIMESTAMP_TO_BET, "");
        typeToStringTemplateMap.put(MessageType.TIMESTAMP_TO_RESULT, "");
        typeToStringTemplateMap.put(MessageType.TIMESTAMP_TO_ROLL, "");
    }

    public static JSONMessage create_message(MessageType msgType, String ... dataForJSON)
    {
        String rawStringJSON = typeToStringTemplateMap.get(msgType);
        //TODO: Sprawdzić czy to poniżej normalnie sformatuje stringa z róznymi parametrami
        rawStringJSON = String.format(rawStringJSON, (Object[]) dataForJSON);
        return new JSONMessage(rawStringJSON, msgType);
    }

}

