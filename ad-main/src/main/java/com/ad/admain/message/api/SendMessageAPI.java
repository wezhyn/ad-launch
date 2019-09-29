package com.ad.admain.message.api;


import io.swagger.client.model.Msg;

/**
 * This interface is created for RestAPI of Sending Message, it should be
 * synchronized with the API list.
 * 
 * @author Eric23 2016-01-05
 */
public interface SendMessageAPI {

    Object sendMessage(Msg payload);
}
