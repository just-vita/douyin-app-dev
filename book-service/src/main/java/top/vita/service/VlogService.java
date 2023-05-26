package top.vita.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.vita.bo.VlogBO;
import top.vita.pojo.Vlog;
import top.vita.utils.PagedGridResult;

/**
 * 短视频表(Vlog)表服务接口
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
public interface VlogService extends IService<Vlog> {

    void createVlog(VlogBO vlogBO);

    PagedGridResult getIndexVlogList(String userId, String search, Integer page, Integer pageSize);

    Object getVlogDetailById(String userId, String vlogId);

    void changeToPublicOrPrivate(String userId, String vlogId, Integer type);

    PagedGridResult getMyVlogList(String userId, Integer page, Integer pageSize, Integer type);

    void like(String userId, String vlogerId, String vlogId);

    void unlike(String userId, String vlogerId, String vlogId);

    Integer getVlogBeLikedCounts(String vlogId);

    PagedGridResult getMyLikedVlogList(String userId, Integer page, Integer pageSize);

    PagedGridResult getMyFollowVlogList(String myId, Integer page, Integer pageSize);

    PagedGridResult getMyFriendVlogList(String myId, Integer page, Integer pageSize);


}

