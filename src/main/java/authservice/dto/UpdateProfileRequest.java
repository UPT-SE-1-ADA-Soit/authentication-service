package authservice.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private String location;
    private String avatarUrl;
}
