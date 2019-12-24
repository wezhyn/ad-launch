package com.ad.admain.message.api.impl;

import com.ad.admain.message.api.SendMessageAPI;
import com.ad.admain.message.common.EasemobProperties;
import com.ad.admain.message.common.EasemobToken;
import com.ad.admain.message.common.ResponseHandler;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;

public class EasemobSendMessage implements SendMessageAPI {

    private final EasemobProperties easemobProperties=EasemobProperties.EASEMOB_PROPERTIES;

    private ResponseHandler responseHandler=new ResponseHandler();
    private MessagesApi api=new MessagesApi();

    @Override
    public Object sendMessage(final Msg payload) {
        return responseHandler.handle(()->api.orgNameAppNameMessagesPost(easemobProperties.getOrgName(), easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken(), (Msg) payload));
    }
}
