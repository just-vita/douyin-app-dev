package top.vita.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.vita.pojo.MyLikedVlog;

/**
 * 点赞短视频关联表
(MyLikedVlog)表数据库访问层
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
@Mapper
public interface MyLikedVlogMapper extends BaseMapper<MyLikedVlog> {

}

