package com.lingohammer.aws.auth.service;

import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.data.SecurityToken;
import com.lingohammer.aws.auth.data.UserInfo;

import java.util.Map;

public interface IUserService {
    IsFlawed confirmRestorePassword(String email, String confirmationCode, String newPassword);

    IsFlawed requestPasswordRestoration(String email);

    IsFlawed registerUser(String email, String password, Map<String, String> customAttributes);

    UserInfo getUser(String accessToken);

    boolean checkUserExists(String email, boolean verifyEmail);

    SecurityToken confirmUserRegistration(String email, String confirmationCode);

    SecurityToken login(String email, String password);

    SecurityToken refreshToken(String userId, String refreshToken);
}
