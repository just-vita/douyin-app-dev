package top.vita.mo;

import lombok.Data;

@Data
public class MessageContent {
    private String vlogId;
    private String vlogCover;
    private String commentId;
    private String commentContent;
    private boolean isFriend;
}