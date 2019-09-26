package com.ad.message.api.impl;


import com.ad.message.api.IMUserAPI;
import com.ad.message.comm.EasemobAPI;
import com.ad.message.comm.EasemobToken;
import com.ad.message.comm.ResponseHandler;
import com.ad.message.config.EasemobProperties;
import io.swagger.client.ApiException;
import io.swagger.client.api.UsersApi;
import io.swagger.client.model.NewPassword;
import io.swagger.client.model.Nickname;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.UserNames;


/**
 * 注册环信用户
 */
public class EasemobIMUsers  implements IMUserAPI {

	private final EasemobProperties easemobProperties=EasemobProperties.EASEMOB_PROPERTIES;

	private UsersApi api = new UsersApi();
	private ResponseHandler responseHandler = new ResponseHandler();
	@Override
	public Object createNewIMUserSingle(final Object payload) {
		return responseHandler.handle(()->api.orgNameAppNameUsersPost(easemobProperties.getOrgName(),easemobProperties.getAppName(),
				(RegisterUsers) payload,
				EasemobToken.forAccessToken(easemobProperties).getToken()));
	}

	@Override
	public Object createNewIMUserBatch(final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersPost(easemobProperties.getOrgName(),easemobProperties.getAppName(), (RegisterUsers) payload,EasemobToken.forAccessToken(easemobProperties).getToken());
			}
		});
	}

	@Override
	public Object getIMUserByUserName(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersUsernameGet(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName);
		}
		});
	}

	@Override
	public Object getIMUsersBatch(final Long limit,final String cursor) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersGet(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),limit+"",cursor);
			}
		});
	}

	@Override
	public Object deleteIMUserByUserName(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersUsernameDelete(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName);
			}
		});
	}

	@Override
	public Object deleteIMUserBatch(final Long limit,final String cursor) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersDelete(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),limit+"",cursor);
			}
		});
	}

	@Override
	public Object modifyIMUserPasswordWithAdminToken(final String userName, final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersUsernamePasswordPut(easemobProperties.getOrgName(),easemobProperties.getAppName(),userName, (NewPassword) payload,EasemobToken.forAccessToken(easemobProperties).getToken());
			}
		});
	}

	@Override
	public Object modifyIMUserNickNameWithAdminToken(final String userName,final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersUsernamePut(easemobProperties.getOrgName(),easemobProperties.getAppName(),userName, (Nickname) payload,EasemobToken.forAccessToken(easemobProperties).getToken());
			}
		});
	}

	@Override
	public Object addFriendSingle(final String userName,final String friendName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernamePost(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName,friendName);
			}
		});
	}

	@Override
	public Object deleteFriendSingle(final String userName,final String friendName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernameDelete(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName,friendName);
			}
		});
	}

	@Override
	public Object getFriends(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersOwnerUsernameContactsUsersGet(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName);
			}
		});
	}

	@Override
	public Object getBlackList(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersOwnerUsernameBlocksUsersGet(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName);
			}
		});
	}

	@Override
	public Object addToBlackList(final String userName,final Object payload) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersOwnerUsernameBlocksUsersPost(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName, (UserNames) payload);
			}
		});
	}

	@Override
	public Object removeFromBlackList(final String userName,final String blackListName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersOwnerUsernameBlocksUsersBlockedUsernameDelete(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName,blackListName);
			}
		});
	}

	@Override
	public Object getIMUserStatus(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersUsernameStatusGet(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName);
			}
		});
	}

	@Override
	public Object getOfflineMsgCount(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersOwnerUsernameOfflineMsgCountGet(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName);
			}
		});
	}

	@Override
	public Object getSpecifiedOfflineMsgStatus(final String userName,final String msgId) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersUsernameOfflineMsgStatusMsgIdGet(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName,msgId);
			}
		});
	}

	@Override
	public Object deactivateIMUser(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersUsernameDeactivatePost(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName);
			}
		});
	}

	@Override
	public Object activateIMUser(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersUsernameActivatePost(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName);
			}
		});
	}

	@Override
	public Object disconnectIMUser(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersUsernameDisconnectGet(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName);
			}
		});
	}

	@Override
	public Object getIMUserAllChatGroups(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersUsernameJoinedChatgroupsGet(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName);
			}
		});
	}

	@Override
	public Object getIMUserAllChatRooms(final String userName) {
		return responseHandler.handle(new EasemobAPI() {
			@Override
			public Object invokeEasemobApi() throws ApiException {
				return api.orgNameAppNameUsersUsernameJoinedChatroomsGet(easemobProperties.getOrgName(),easemobProperties.getAppName(),EasemobToken.forAccessToken(easemobProperties).getToken(),userName);
			}
		});
	}
}
