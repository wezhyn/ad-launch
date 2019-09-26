package com.ad.message.api.impl;


import com.ad.message.api.AuthTokenAPI;
import com.ad.message.comm.EasemobToken;
import com.ad.message.config.EasemobProperties;

public class EasemobAuthToken implements AuthTokenAPI {

	private final EasemobProperties easemobProperties=EasemobProperties.EASEMOB_PROPERTIES;


	@Override
	public Object getAuthToken(){
		return EasemobToken.forAccessToken(easemobProperties).getToken();
	}
}
