package com.loan.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import com.loan.enums.CodeEnum;
import com.loan.exception.BizException;
import com.loan.model.JsonResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
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
 * @time: 2019/11/20 20:36
 */
@RestController
@RequestMapping("/api/common")
public class FileController extends BaseController {

    /**
     * description: 以 MultipartFile 形式传参上传图片
     *
     * @param file
     * @return
     * @author
     * @date 2020/3/9 9:30
     */
    @PostMapping("/upload")
    public JsonResult fileUpload(@RequestParam("file") MultipartFile file) {
        logger.info("图片上传[MultipartFile]，file={}", file);
        if (file == null || file.isEmpty()) {
            return fail(CodeEnum.EMPTY_FILE);
        }
        try {
            String url = uploadFile(file);
            logger.info("上传返回值：url={}", url);
            return success(url);
        } catch (IOException e) {
            logger.warn("【上传文件失败】", e);
            throw new BizException(CodeEnum.UPLOAD_FILE_FAIL);
        }
    }

    /**
     * description: 以 base64 编码形式传参上传图片
     *
     * @param jsonObject
     * @return
     * @author
     * @date 2020/3/9 9:30
     */
    @PostMapping("/upload2")
    public JsonResult fileUpload2(@RequestBody JSONObject jsonObject) {
        String fileName = jsonObject.getStr("fileName");
        String base64Str = jsonObject.getStr("base64Str");

        if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(base64Str)) {
            return fail(CodeEnum.EMPTY_FILE);
        }
        logger.info("图片上传[base64]，fileName={}", fileName);
        try {
            String url = uploadFile(base64Str, fileName);
            logger.info("上传返回值：url={}", url);
            return success(url);
        } catch (IOException e) {
            logger.warn("【上传文件失败】", e);
            throw new BizException(CodeEnum.UPLOAD_FILE_FAIL);
        }
    }

    @RequestMapping("/download/{url}")
    public JsonResult fileDownload(@PathVariable String url, HttpServletResponse response) throws IOException {
        logger.info("图片加载，url={}", url);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = decoder.decodeBuffer(url);
        File file = new File(new String(bytes));
        if (!file.exists()) {
            return fail(CodeEnum.SOURCE_NOT_EXIST);
        }
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(FileUtil.readBytes(file));
            outputStream.flush();
        }
        return success();
    }

}
