package com.loan.controller.front;

import com.loan.controller.BaseController;
import com.loan.util.Constants;
import com.loan.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description:
 * @author:
 * @time: 2019/11/30 17:39
 */
@Controller
@RequestMapping("/api/front")
public class CodeGenerateController extends BaseController {

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/imageCode")
    public void imageCode(HttpServletResponse response) throws IOException {
        String code = imageResponse(response);
        redisUtil.set(Constants.IMAGE_CODE_PREFIX + code.toLowerCase(), code.toLowerCase(), Constants.IMAGE_CODE_TIME_OUT);
    }
}
