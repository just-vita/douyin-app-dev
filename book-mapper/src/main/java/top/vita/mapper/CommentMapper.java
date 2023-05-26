package top.vita.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.vita.pojo.Comment;
import top.vita.vo.CommentVO;
import top.vita.vo.IndexVlogVO;

import java.util.List;
import java.util.Map;

/**
 * 评论表(Comment)表数据库访问层
 *
 * @author vita
 * @since 2023-05-24 00:57:34
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentVO> getCommentList(@Param("paramMap") Map<String, Object> map);
}

