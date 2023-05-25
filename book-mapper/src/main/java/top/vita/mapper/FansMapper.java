package top.vita.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.vita.pojo.Fans;
import top.vita.vo.FansVO;
import top.vita.vo.VlogerVO;

import java.util.List;
import java.util.Map;

/**
 * 粉丝表

(Fans)表数据库访问层
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
@Mapper
public interface FansMapper extends BaseMapper<Fans> {

    List<VlogerVO> queryMyFollows(@Param("paramMap") Map<String, Object> map);

    List<FansVO> queryMyFans(@Param("paramMap") Map<String, Object> map);

}

