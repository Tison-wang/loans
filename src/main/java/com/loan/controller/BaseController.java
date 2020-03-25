package com.loan.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.loan.enums.CodeEnum;
import com.loan.model.JsonResult;
import com.loan.util.JsonResultUtil;
import com.loan.model.JsonResult;
import com.loan.util.JsonResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @description:
 * @author:
 * @time: 2019/11/20 20:35
 */
public class BaseController {
    /**
     * 上传目录
     */
    private static final String UPLOAD_DIR = File.separator + "opt" + File.separator + "loan" + File.separator + "upload";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected <T> JsonResult<T> success() {
        return JsonResultUtil.success();
    }

    protected <T> JsonResult<T> success(T data) {
        return JsonResultUtil.success(data);
    }

    protected <T> JsonResult<T> fail() {
        return JsonResultUtil.fail();
    }

    protected <T> JsonResult<T> fail(int code, String msg) {
        return JsonResultUtil.fail(code, msg);
    }

    protected <T> JsonResult<T> fail(CodeEnum codeEnum) {
        return fail(codeEnum.getCode(), codeEnum.getMsg());
    }

    protected <T> JsonResult<T> fail(CodeEnum codeEnum, String customMsg) {
        return fail(codeEnum.getCode(), codeEnum.getMsg() + customMsg);
    }

    /**
     * 返回图片验证码
     *
     * @return 验证码
     * @throws IOException
     */
    protected String imageResponse(HttpServletResponse response) throws IOException {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(80, 30, 4, 30);
        response.setContentType("image/jpeg");
        // 发头控制浏览器不要缓存
        response.setDateHeader("expires", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            lineCaptcha.write(outputStream);
            outputStream.flush();
        }
        return lineCaptcha.getCode();
    }

    protected String uploadFile(MultipartFile file) throws IOException {
        //拼接子路径
        File upload = new File(UPLOAD_DIR, File.separator + LocalDate.now().toString());
        //若目标文件夹不存在，则创建
        if (!upload.exists()) {
            upload.mkdirs();
        }
        //根据file大小，准备一个字节数组
        byte[] bytes = file.getBytes();
        //======拼接上传路径=======
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 获得文件原始名称
        String fileName = file.getOriginalFilename();
        //后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        String path = upload.getAbsolutePath() + File.separator + uuid + "." + suffixName;
        logger.info("文件上传路径：fileName={},path={}", fileName, path);
        //开始将源文件写入目标地址
        Files.write(Paths.get(path), bytes);
        //加密文件路径
        BASE64Encoder encoder = new BASE64Encoder();
        String url = encoder.encode(path.getBytes());
        return url.replaceAll("\r|\n", "");
    }

    protected String uploadFile(String base64Str, String fileName) throws IOException {
        //拼接子路径
        File upload = new File(UPLOAD_DIR, File.separator + LocalDate.now().toString());
        //若目标文件夹不存在，则创建
        if (!upload.exists()) {
            upload.mkdirs();
        }
        //根据file大小，准备一个字节数组
        byte[] bytes = Base64.decodeBase64(base64Str.substring(base64Str.indexOf(",") + 1));
        //======拼接上传路径=======
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        String path = upload.getAbsolutePath() + File.separator + uuid + "." + suffixName;
        logger.info("文件上传路径：fileName={},path={}", fileName, path);
        //开始将源文件写入目标地址
        Files.write(Paths.get(path), bytes);
        //加密文件路径
        BASE64Encoder encoder = new BASE64Encoder();
        String url = encoder.encode(path.getBytes());
        return url.replaceAll("\r|\n", "");
    }
}
