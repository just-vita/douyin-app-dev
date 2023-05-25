package top.vita.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.vita.base.BaseInfoProperties;
import top.vita.config.MinIOConfig;
import top.vita.grace.result.GraceJSONResult;
import top.vita.utils.MinIOUtils;

/**
 * 用户表(Users)表控制层
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
@Api(tags = "文件模块")
@RestController
public class FileController extends BaseInfoProperties {

    @Autowired
    private MinIOConfig minIOConfig;

    @ApiOperation("上传文件接口")
    @PostMapping("/upload")
    public GraceJSONResult upload(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                                    filename,
                                    file.getInputStream());
        String imgUrl = minIOConfig.getFileHost() + "/" +
                        minIOConfig.getBucketName() + "/" +
                        filename;
        return GraceJSONResult.ok(imgUrl);
    }
}
