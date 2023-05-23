package top.vita.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.vita.pojo.Fans;

/**
 * 粉丝表

(Fans)表数据库访问层
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
@Mapper
public interface FansMapper extends BaseMapper<Fans> {

}

