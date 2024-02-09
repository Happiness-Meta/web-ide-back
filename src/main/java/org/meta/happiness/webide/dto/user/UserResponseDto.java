package org.meta.happiness.webide.dto.user;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.entity.user.User;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String nickname;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public static UserResponseDto convertUserToDto(User user) {
        return new UserResponseDto(user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getCreatedDate(),
                user.getLastModifiedDate());
    }
}
