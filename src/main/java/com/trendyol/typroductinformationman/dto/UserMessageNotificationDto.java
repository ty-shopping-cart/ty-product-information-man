package com.trendyol.typroductinformationman.dto;

import com.trendyol.typroductinformationman.domain.UserNotificationType;
import lombok.Data;

import java.util.List;

@Data
public class UserMessageNotificationDto {
    private List<UserDto> userList;

    private UserNotificationType userNotificationType;
}
