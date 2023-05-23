package top.vita.pojo;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 粉丝表

(Fans)表实体类
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("fans")
public class Fans  {
    @TableId
    private String id;
    //作家用户id
    private String vlogerId;
    //粉丝用户id
    private String fanId;
    //粉丝是否是vloger的朋友，如果成为朋友，则本表的双方此字段都需要设置为1，如果有一人取关，则两边都需要设置为0
    private Integer isFanFriendOfMine;

}

