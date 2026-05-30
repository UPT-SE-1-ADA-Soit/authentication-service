package authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateResponse {
    private Integer userId;
    private String email;
    private String name;
    private String location;
    private String avatarUrl;
}
