package top.vita.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.vita.pojo.Vlog;
import top.vita.vo.IndexVlogVO;

import java.util.List;
import java.util.Map;

/**
 * 短视频表(Vlog)表数据库访问层
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
@Mapper
public interface VlogMapper extends BaseMapper<Vlog> {

    List<IndexVlogVO> getIndexVlogList(@Param("paramMap") Map<String, Object> map);

    List<IndexVlogVO> getVlogDetailById(@Param("paramMap") Map<String, Object> map);

    List<IndexVlogVO> getMyLikedVlogList(@Param("paramMap") Map<String, Object> map);

    List<IndexVlogVO> getMyFollowVlogList(@Param("paramMap") Map<String, Object> map);
}

