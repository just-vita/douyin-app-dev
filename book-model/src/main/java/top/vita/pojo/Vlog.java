package top.vita.pojo;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 短视频表(Vlog)表实体类
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("vlog")
public class Vlog  {
    @TableId
    private String id;
    //对应用户表id，vlog视频发布者
    private String vlogerId;
    //视频播放地址
    private String url;
    //视频封面
    private String cover;
    //视频标题，可以为空
    private String title;
    //视频width
    private Integer width;
    //视频height
    private Integer height;
    //点赞总数
    private Integer likeCounts;
    //评论总数
    private Integer commentsCounts;
    //是否私密，用户可以设置私密，如此可以不公开给比人看
    private Integer isPrivate;
    //创建时间 创建时间
    private Date createdTime;
    //更新时间 更新时间
    private Date updatedTime;

}

