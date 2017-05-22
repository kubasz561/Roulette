/**
 * Created by sackhorn on 16.05.17.
 */
package com.kubasz561.roulette.common;


import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class JSONMessageBuilder implements Serializable{
    private static final Map<MessageType, String> typeToStringTemplateMap = new HashMap<>();
    private JSONMessageBuilder(){}

    //private String signUpTemplate =
    //TODO: Wprowadzić template'y do fomratowania stringów z komunikatami
    static
    {
    typeToStringTemplateMap.put(MessageType.SIGN_UP, "{'login':{0},password:{1}}");
        typeToStringTemplateMap.put(MessageType.SIGN_UP_OK, "{'response': 'sing_up_ok'}");
        typeToStringTemplateMap.put(MessageType.LOGIN_DUPLICATE, "{'response': 'login_duplicate'}");
        typeToStringTemplateMap.put(MessageType.LOG_IN, "{'login':{0}, 'password_hash':{1}}");
        typeToStringTemplateMap.put(MessageType.WRONG_PASS, "{'response':'wrong_password'}");
        typeToStringTemplateMap.put(MessageType.BLOCKED, "{'response':'blocked_user'}");
        typeToStringTemplateMap.put(MessageType.LOG_OUT, "{'request': 'log_out'}");
        typeToStringTemplateMap.put(MessageType.SET_BET, "{'login':{0},'bet':{1},'value':{2},'session_number':{3},'password':{4}}");
        typeToStringTemplateMap.put(MessageType.BET_OK, "{'response': 'bet_accepted'}");
        typeToStringTemplateMap.put(MessageType.BET_UNABLE, "{'response': 'bet_unable'}");
        typeToStringTemplateMap.put(MessageType.TIMESTAMP_TO_BET, "{'timestamp':{0},'round_time':{1},'account_balance':{2}}");
        typeToStringTemplateMap.put(MessageType.TIMESTAMP_TO_RESULT, "{'timestamp':{0},'round_time':{1},'result':{2}}");
        typeToStringTemplateMap.put(MessageType.TIMESTAMP_TO_ROLL, "{'timestamp':{0},'round_time':{1},'bet_list':{2}}");
    }

    public static JSONMessage create_message(MessageType msgType, String ... dataForJSON)
    {
        String rawStringJSON = typeToStringTemplateMap.get(msgType);
        //TODO: Sprawdzić czy to poniżej normalnie sformatuje stringa z róznymi parametrami
        rawStringJSON = MessageFormat.format(rawStringJSON, (Object[]) dataForJSON);
        return new JSONMessage(rawStringJSON, msgType);
    }

}

