package top.vita.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.vita.pojo.Vlog;

/**
 * 短视频表(Vlog)表数据库访问层
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
@Mapper
public interface VlogMapper extends BaseMapper<Vlog> {

}

