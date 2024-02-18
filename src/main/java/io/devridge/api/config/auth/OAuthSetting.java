package io.devridge.api.config.auth;


public class OAuthSetting {
    public static final String DEV_ROOT_URL = "https://localhost:3000/devridge";
    public static final String PUBLISH_ROOT_URL = "https://devridge.dev";
    public static final String PUBLISH_AWS_URL = "http://ec2-3-38-125-102.ap-northeast-2.compute.amazonaws.com";
    public static final String OAUTH2_LOGIN_AFTER_PAGE = PUBLISH_AWS_URL + "/loginRedirectPage";
    public static final String OAUTH2_LOGIN_FAIL_PAGE = PUBLISH_AWS_URL + "/loginFailRedirectPage";
}
