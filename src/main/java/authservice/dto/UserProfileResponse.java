package authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private Integer userId;
    private String name;
    private String location;
    private String avatarUrl;
}
