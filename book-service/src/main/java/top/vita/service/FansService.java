package top.vita.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.vita.pojo.Fans;
import top.vita.utils.PagedGridResult;
import top.vita.vo.FansVO;

import java.util.List;

/**
 * 粉丝表

(Fans)表服务接口
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
public interface FansService extends IService<Fans> {

    void doFollow(String myId, String vlogerId);

    void doCancel(String myId, String toId);

    boolean isFollowingMe(String myId, String toId);

    PagedGridResult queryMyFollows(String myId, Integer page, Integer pageSize);

    PagedGridResult queryMyFans(String myId, Integer page, Integer pageSize);

}

