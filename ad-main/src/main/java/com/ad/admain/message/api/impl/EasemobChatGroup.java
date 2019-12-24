package com.ad.admain.message.api.impl;

import com.ad.admain.message.api.ChatGroupAPI;
import com.ad.admain.message.common.EasemobProperties;
import com.ad.admain.message.common.EasemobToken;
import com.ad.admain.message.common.ResponseHandler;
import io.swagger.client.StringUtil;
import io.swagger.client.api.GroupsApi;
import io.swagger.client.model.*;


public class EasemobChatGroup implements ChatGroupAPI {

    private final EasemobProperties easemobProperties=EasemobProperties.EASEMOB_PROPERTIES;
    private ResponseHandler responseHandler=new ResponseHandler();
    private GroupsApi api=new GroupsApi();

    @Override
    public Object getChatGroups(final Long limit, final String cursor) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGet(easemobProperties.getOrgName(), easemobProperties.getAppName(),
                EasemobToken.forAccessToken(easemobProperties).getToken(), limit + "", cursor));
    }

    @Override
    public Object getChatGroupDetails(final String[] groupIds) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupIdsGet(easemobProperties.getOrgName(),
                easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken(),
                StringUtil.join(groupIds, ",")));
    }

    @Override
    public Object createChatGroup(final Object payload) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsPost(easemobProperties.getOrgName(), easemobProperties.getAppName(),
                EasemobToken.forAccessToken(easemobProperties).getToken(), (Group) payload));
    }

    @Override
    public Object modifyChatGroup(final String groupId, final Object payload) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupIdPut(easemobProperties.getOrgName(),
                easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken(),
                groupId, (ModifyGroup) payload));
    }

    @Override
    public Object deleteChatGroup(final String groupId) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupIdDelete(easemobProperties.getOrgName(),
                easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken(), groupId));
    }

    @Override
    public Object getChatGroupUsers(final String groupId) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupIdUsersGet(easemobProperties.getOrgName(),
                easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken(), groupId));
    }

    @Override
    public Object addSingleUserToChatGroup(final String groupId, final String userId) {
        final UserNames userNames=new UserNames();
        UserName userList=new UserName();
        userList.add(userId);
        userNames.usernames(userList);
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupIdUsersPost(easemobProperties.getOrgName(),
                easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken(),
                groupId, userNames));
    }

    @Override
    public Object addBatchUsersToChatGroup(final String groupId, final Object payload) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupIdUsersPost(easemobProperties.getOrgName(),
                easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken(),
                groupId, (UserNames) payload));
    }

    @Override
    public Object removeSingleUserFromChatGroup(final String groupId, final String userId) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupIdUsersUsernameDelete(easemobProperties.getOrgName(),
                easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken(),
                groupId, userId));
    }

    @Override
    public Object removeBatchUsersFromChatGroup(final String groupId, final String[] userIds) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupIdUsersMembersDelete(easemobProperties.getOrgName(), easemobProperties.getAppName(),
                EasemobToken.forAccessToken(easemobProperties).getToken(),
                groupId, StringUtil.join(userIds, ",")));
    }

    @Override
    public Object transferChatGroupOwner(final String groupId, final Object payload) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupidPut(easemobProperties.getOrgName(), easemobProperties.getAppName(),
                EasemobToken.forAccessToken(easemobProperties).getToken(), groupId, (NewOwner) payload));
    }

    @Override
    public Object getChatGroupBlockUsers(final String groupId) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupIdBlocksUsersGet(easemobProperties.getOrgName(), easemobProperties.getAppName(),
                EasemobToken.forAccessToken(easemobProperties).getToken(), groupId));
    }

    @Override
    public Object addSingleBlockUserToChatGroup(final String groupId, final String userId) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamePost(easemobProperties.getOrgName(),
                easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken()
                , groupId, userId));
    }

    @Override
    public Object addBatchBlockUsersToChatGroup(final String groupId, final Object payload) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupIdBlocksUsersPost(easemobProperties.getOrgName(),
                easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken(),
                groupId, (UserNames) payload));
    }

    @Override
    public Object removeSingleBlockUserFromChatGroup(final String groupId, final String userId) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernameDelete(easemobProperties.getOrgName(),
                easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken(),
                groupId, userId));
    }

    @Override
    public Object removeBatchBlockUsersFromChatGroup(final String groupId, final String[] userIds) {
        return responseHandler.handle(()->api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamesDelete(easemobProperties.getOrgName(),
                easemobProperties.getAppName(), EasemobToken.forAccessToken(easemobProperties).getToken(),
                groupId, StringUtil.join(userIds, ",")));
    }
}
