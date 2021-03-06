package com.kubasz561.roulette.common;


import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class JSONMessageBuilder implements Serializable{
    private static final Map<MessageType, String> typeToStringTemplateMap = new HashMap<>();
    private JSONMessageBuilder(){}

    //TODO: Wprowadzić template'y do fomratowania stringów z komunikatami
    static
    {
        typeToStringTemplateMap.put(MessageType.SIGN_UP, "'login':{0} ,'password':{1}");
        typeToStringTemplateMap.put(MessageType.SIGN_UP_OK, "'response': 'sing_up_ok','account_balance':{0}");
        typeToStringTemplateMap.put(MessageType.SIGN_UP_UNABLE, "'response': 'sing_up_unable'");
        typeToStringTemplateMap.put(MessageType.LOGIN_OK, "'response': 'login_ok','account_balance':{0}");
        typeToStringTemplateMap.put(MessageType.LOGIN_DUPLICATE, "'response': 'login_duplicate'");
        typeToStringTemplateMap.put(MessageType.LOG_IN, "'login':{0}, 'password':{1}");
        typeToStringTemplateMap.put(MessageType.LOGIN_INVALID, "'response': 'login_invalid'");
        typeToStringTemplateMap.put(MessageType.WRONG_PASS, "'response':'wrong_password'");
        typeToStringTemplateMap.put(MessageType.BLOCKED, "'response':'blocked_user'");
        typeToStringTemplateMap.put(MessageType.LOG_OUT, "'request': 'log_out'");
        typeToStringTemplateMap.put(MessageType.LOG_OUT_OK, "'response': 'log_out_ok'");
        typeToStringTemplateMap.put(MessageType.SET_BET, "'bet':{0},'value':{1},'session_number':{2}");
        typeToStringTemplateMap.put(MessageType.BET_WON, "'response':'bet_won','account_balance':{0}");
        typeToStringTemplateMap.put(MessageType.BET_LOST, "'response':'bet_lost','account_balance':{0}");
        typeToStringTemplateMap.put(MessageType.BET_OK, "'response': 'bet_accepted'");
        typeToStringTemplateMap.put(MessageType.BET_UNABLE, "'response': 'bet_unable'");
        typeToStringTemplateMap.put(MessageType.BAD_SESSION_ID, "'response': 'bad_session_id'");
        typeToStringTemplateMap.put(MessageType.TIMESTAMP_TO_BET, "'timestamp':{0},'round_time':{1}");
        typeToStringTemplateMap.put(MessageType.TIMESTAMP_TO_RESULT, "'timestamp':{0},'round_time':{1},'result':{2}");
        typeToStringTemplateMap.put(MessageType.TIMESTAMP_TO_ROLL, "'timestamp':{0},'round_time':{1}");
    }

    public static JSONMessage create_message(MessageType msgType, String ... dataForJSON)
    {
        String rawStringJSON = typeToStringTemplateMap.get(msgType);
        //TODO: Sprawdzić czy to poniżej normalnie sformatuje stringa z róznymi parametrami
        rawStringJSON ="{" + MessageFormat.format(rawStringJSON, dataForJSON) + "}";
        System.out.println("CLIENT: " + rawStringJSON);
        return new JSONMessage(rawStringJSON, msgType);
    }

}