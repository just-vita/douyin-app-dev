package top.vita.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.vita.pojo.Users;

/**
 * @Author vita
 * @Date 2023/5/24 15:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo extends Users {
    private String userToken;
}
