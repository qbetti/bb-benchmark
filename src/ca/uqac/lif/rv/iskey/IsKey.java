package ca.uqac.lif.rv.iskey;

import ca.uqac.lif.cep.functions.UnaryFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IsKey extends UnaryFunction<String[], Boolean> {

    private boolean isKey;
    private Map<String, List<String>> messagesByAESKey;

    public IsKey() {
        super(String[].class, Boolean.class);
        this.messagesByAESKey = new HashMap<>();
        isKey = true;
    }

    @Override
    public Boolean getValue(String[] paramValues) {
        if (!isKey)
            return false;

        String encodedAESKey = (String) paramValues[0];
        String messageToEncrypt = (String) paramValues[1];

        List<String> messages = messagesByAESKey.get(encodedAESKey);

        if (messages != null && messages.contains(messageToEncrypt)) {
            isKey = false;

        } else {
            if (messages == null) {
                messages = new ArrayList<>();
                messagesByAESKey.put(encodedAESKey, messages);
            }
            messages.add(messageToEncrypt);
        }

        return isKey;
    }
}
