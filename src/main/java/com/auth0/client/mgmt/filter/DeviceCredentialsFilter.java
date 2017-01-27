package com.auth0.client.mgmt.filter;

/**
 * Class used to filter the results received when calling the Device Credentials endpoint. Related to the {@link com.auth0.client.mgmt.DeviceCredentialsEntity()} entity.
 */
public class DeviceCredentialsFilter extends BaseFilter<DeviceCredentialsFilter> {

    /**
     * Filter by user id
     *
     * @param userId only retrieve items with this user id.
     */
    public DeviceCredentialsFilter withUserId(String userId) {
        parameters.put("user_id", userId);
        return this;
    }

    /**
     * Filter by client id
     *
     * @param clientId only retrieve items with this client id.
     */
    public DeviceCredentialsFilter withClientId(String clientId) {
        parameters.put("client_id", clientId);
        return this;
    }

    /**
     * Filter by type
     *
     * @param type only retrieve items with this type.
     */
    public DeviceCredentialsFilter withType(String type) {
        parameters.put("type", type);
        return this;
    }

}