package com.example.theperegrinefund;

import android.telephony.SmsManager;

public class SmsSender {

    public void send(UserApp message) throws Exception {
        if (!message.isValid()) {
            throw new Exception("Numéro ou message invalide");
        }
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(
                message.getPhoneNumber(),
                null,
                message.getContent(),
                null,
                null
        );
    }
}
