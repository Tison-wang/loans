package com.loan.controller.front;

import com.loan.controller.BaseController;
import com.loan.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 空接口，给前端网站关闭页面使用
 * @author:
 * @time: 2019/12/19 20:58
 */
@RestController
public class EmptyController extends BaseController {

    @GetMapping("/api/front/empty")
    public JsonResult empty() {
        return success();
    }
}
