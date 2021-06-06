package com.test.authorizationserver.tokenenhancer;

import com.test.authorizationserver.model.userdetail.JpaUserDetail;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class SubjectSettingTokenEnhancer implements TokenEnhancer {

    public static final String subjectKey = "sub";

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Authentication userAuthentication = authentication.getUserAuthentication();

        if(userAuthentication != null) {
            JpaUserDetail userDetail = (JpaUserDetail) userAuthentication
                    .getPrincipal();
            String username = userDetail.getUsername();

            Map<String, Object> additionalInformation =
                    new HashMap<>(accessToken.getAdditionalInformation());
            additionalInformation.put(subjectKey, username);

            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
        }

        return accessToken;
    }
}
