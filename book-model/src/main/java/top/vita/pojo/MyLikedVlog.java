package top.vita.pojo;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "my_liked_vlog")
@Data
public class MyLikedVlog implements Serializable {
    @Id
    private String id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 喜欢的短视频id
     */
    @Column(name = "vlog_id")
    private String vlogId;

    private static final long serialVersionUID = 1L;

}