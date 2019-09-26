package com.ad.message.api.impl;

import com.ad.message.api.ChatMessageAPI;
import com.ad.message.comm.EasemobToken;
import com.ad.message.comm.ResponseHandler;
import com.ad.message.config.EasemobProperties;
import io.swagger.client.api.ChatHistoryApi;


public class EasemobChatMessage  implements ChatMessageAPI {

    private final EasemobProperties easemobProperties=EasemobProperties.EASEMOB_PROPERTIES;
    private ResponseHandler responseHandler = new ResponseHandler();
    private ChatHistoryApi api = new ChatHistoryApi();


    @Override
    public Object exportChatMessages(final Long limit,final String cursor,final String query) {
        return responseHandler.handle(()->api.orgNameAppNameChatmessagesGet(easemobProperties.getOrgName(),
                easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken(),query,limit+"",cursor));
    }

    @Override
    public Object exportChatMessages(final String timeStr) {
        return responseHandler.handle(()->api.orgNameAppNameChatmessagesTimeGet(easemobProperties.getOrgName(),easemobProperties.getAppName(),
                EasemobToken.forAccessToken(easemobProperties).getToken(),timeStr));
    }
}
