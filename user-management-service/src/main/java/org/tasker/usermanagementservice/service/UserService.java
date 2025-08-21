package org.tasker.usermanagementservice.service;

import org.tasker.usermanagementservice.web.dto.user.GetUserInfoResponse;

public interface UserService {

    GetUserInfoResponse getUserInfo(String userId);
}
