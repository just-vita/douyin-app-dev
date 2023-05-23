package top.vita.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.vita.pojo.Comment;

/**
 * 评论表(Comment)表数据库访问层
 *
 * @author vita
 * @since 2023-05-24 00:57:34
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}

