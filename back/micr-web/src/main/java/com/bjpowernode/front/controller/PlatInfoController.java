package com.bjpowernode.front.controller;
import com.bjpowernode.api.pojo.BaseInfo;
import com.bjpowernode.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "平台信息功能")
@RestController
@RequestMapping("/v1")
public class PlatInfoController extends BaseController{

    @ApiOperation(value = "平台三项基本信息",notes = "注册人数，平均的利率 ，总投资金额")
    @GetMapping("/plat/info")
    public RespResult queryPlatBaseInfo(){
        BaseInfo baseInfo = platBaseInfoService.queryPlatBaseInfo();

        RespResult result = new RespResult();
        result.setCode(1000);
        result.setMsg("查询平台信息成功");
        result.setData(baseInfo);
        return result;
    }
}
