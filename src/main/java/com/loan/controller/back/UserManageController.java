package com.loan.controller.back;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageInfo;
import com.loan.annotation.RequireLogin;
import com.loan.controller.BaseController;
import com.loan.enums.CodeEnum;
import com.loan.jsonFilter.JSON;
import com.loan.jsonFilter.JSONS;
import com.loan.model.JsonResult;
import com.loan.model.User;
import com.loan.model.UserDetail;
import com.loan.service.ILoanService;
import com.loan.service.IUserDetailService;
import com.loan.service.IUserService;
import com.loan.util.ThreadLocalCache;
import com.loan.service.ILoanService;
import com.loan.service.IUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/11/20 22:27
 */
@Controller
@RequestMapping("/api/back/userManage")
@RequireLogin
public class UserManageController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IUserDetailService userDetailService;
    @Autowired
    private ILoanService loanService;

    @RequestMapping("/list")
    @JSONS({@JSON(type = User.class, include = "id,ownerId,userName,state,createTime,detail"), @JSON(type = UserDetail.class, include = "name,bankCardNumber")})
    public JsonResult<PageInfo<User>> list(String searchName, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "2") Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", 0);
        params.put("searchName", searchName);
        User user = (User) ThreadLocalCache.get(ThreadLocalCache.KeyEnum.USER);
        if (user.getType() == 1) {
            params.put("ownerId", user.getId());
        }
        PageInfo<User> pageInfo = userService.getUserByPage(pageNum, pageSize, params);
        return success(pageInfo);
    }

    @RequestMapping("/{id}/update")
    @ResponseBody
    public JsonResult update(@PathVariable Integer id, String password, Integer state) {
        if (StrUtil.isBlank(password) && state == null) {
            return fail(CodeEnum.PARAMS_ILLEGAL);
        }
        Map<String, Object> fields = new HashMap<>();
        fields.put("id", id);
        if (!StrUtil.isBlank(password)) {
            fields.put("password", SecureUtil.md5(password));
        }
        if (state != null) {
            if (state == -1) {
                final int loanCount = loanService.getCountByUserId(id);
                if (loanCount > 0) {
                    return fail(CodeEnum.FORBIDDEN_DELETE_USER);
                }
            }
            fields.put("state", state);
        }
        return userService.update(fields) ? success() : fail();
    }

    @RequestMapping("/{id}/detail/view")
    @JSON(type = User.class, filter = "password")
    public JsonResult<User> viewDetail(@PathVariable Integer id) {
        final User user = userService.getUserById(id);
        return success(user);
    }

    @RequestMapping("/{id}/detail/update")
    @ResponseBody
    public JsonResult updateDetail(@PathVariable Integer id, UserDetail detail) {
        detail.setUserId(id);
        return userDetailService.update(detail) ? success() : fail();
    }
}
