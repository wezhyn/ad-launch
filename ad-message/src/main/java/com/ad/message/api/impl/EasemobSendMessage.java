package com.ad.message.api.impl;

import com.ad.message.api.SendMessageAPI;
import com.ad.message.comm.EasemobToken;
import com.ad.message.comm.ResponseHandler;
import com.ad.message.config.EasemobProperties;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;

public class EasemobSendMessage implements SendMessageAPI {

    private final EasemobProperties easemobProperties=EasemobProperties.EASEMOB_PROPERTIES;

    private ResponseHandler responseHandler = new ResponseHandler();
    private MessagesApi api = new MessagesApi();
    @Override
    public Object sendMessage(final Object payload) {
        return responseHandler.handle(()->api.orgNameAppNameMessagesPost(easemobProperties.getOrgName(),easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken(), (Msg) payload));
    }
}
