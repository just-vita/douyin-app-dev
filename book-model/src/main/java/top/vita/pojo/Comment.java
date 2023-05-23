package top.vita.pojo;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 评论表(Comment)表实体类
 *
 * @author vita
 * @since 2023-05-24 00:57:34
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment")
public class Comment  {
    @TableId
    private String id;
    //评论的视频是哪个作者（vloger）的关联id
    private String vlogerId;
    //如果是回复留言，则本条为子留言，需要关联查询
    private String fatherCommentId;
    //回复的那个视频id
    private String vlogId;
    //发布留言的用户id
    private String commentUserId;
    //留言内容
    private String content;
    //留言的点赞总数
    private Integer likeCounts;
    //留言时间
    private Date createTime;

}

