package top.vita.pojo;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 点赞短视频关联表
(MyLikedVlog)表实体类
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("my_liked_vlog")
public class MyLikedVlog  {
    @TableId
    private String id;
    //用户id
    private String userId;
    //喜欢的短视频id
    private String vlogId;

}

