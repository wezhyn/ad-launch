package com.ad.admain.message.api.impl;


import com.ad.admain.message.api.AuthTokenAPI;
import com.ad.admain.message.common.EasemobProperties;
import com.ad.admain.message.common.EasemobToken;

public class EasemobAuthToken implements AuthTokenAPI {

    private final EasemobProperties easemobProperties=EasemobProperties.EASEMOB_PROPERTIES;


    @Override
    public Object getAuthToken() {
        return EasemobToken.forAccessToken(easemobProperties).getToken();
    }
}
