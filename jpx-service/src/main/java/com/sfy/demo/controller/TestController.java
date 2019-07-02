package com.sfy.demo.controller;

import com.sfy.boot.bus.Configs;
import com.sfy.boot.exception.SFYException;
//import com.sfy.demo.config.Configs;
import com.sfy.demo.service.IAuthTestServiceClient;
import com.sfy.demo.service.impl.TxServiceImpl;
import com.sfy.demo.vo.SysDictVO;
import com.sfy.demo.service.ISysDictService;
import com.sfy.demo.service.ISysVoiceDictService;
import com.sfy.utils.constant.ConstantSFY;
import com.sfy.utils.entity.MessageResult;
import com.sfy.utils.tools.apiResult.ApiResult;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * Created by SuperS on 2017/9/25.
 *
 * @author SuperS
 */
@Slf4j
@RestController
@Api(description = "测试服务；可不输令牌验证")
@RequestMapping(value = "/test", produces = APPLICATION_JSON_UTF8_VALUE)
//@RefreshScope
public class TestController {

    @Autowired
    private ISysVoiceDictService sysVoiceDictService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private TxServiceImpl txService;
//    @Autowired
//    private AmqpTemplate rabbitmqTemplate;
    @Resource
    private IAuthTestServiceClient authTestServiceClient;

    @Resource
    private Configs configs;
//    @Resource
//    private DemoSender demoSender;
//    @Value("${defaultZone-str}")
//    private String profile;

    @ApiResponses(value = {
            @ApiResponse(code = ConstantSFY.CODE_401, message = ConstantSFY.MESSAGE_401),
            @ApiResponse(code = ConstantSFY.CODE_500, message = ConstantSFY.MESSAGE_500)
    })
    @ApiImplicitParams(
            @ApiImplicitParam(name="name", value = "name", dataType = "String", paramType = "query")
    )
    @ApiOperation(value="测试GFT", notes="测试TOKEN", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(method = RequestMethod.GET, value = "/v1/testGet")
    @ResponseBody
    public ApiResult<String> testGet(String name) throws InvocationTargetException, IllegalAccessException {
        log.info("====" + configs.getEureka());
//        rabbitmqTemplate.convertAndSend("queue11111","1message from web");
//        authTestServiceClient.save();
//        throw new SFYException(ConstantSFY.CODE_500, "aaaa");
//        rabbitmqTemplate.convertAndSend("exchange","topic.messages","2message from web for exchage");
//        rabbitmqTemplate.convertAndSend(RabbitConstants.EXCHANGE,RabbitConstants.ROUTINGKEY,"3message from web for fanoutExchange");
        //主要是下面这个
//        demoSender.send("message from web for fanoutExchange1234234");


//        rabbitmqTemplate.convertAndSend("test1", "aaaaa啊大大阿瑟东ccccc");
        txService.testSave();
//        log.info(profile);
//        sysDictService.testFindAll();
//        Map<String, Object> map = new HashMap<>();
//        map.put("dict_type", "server");
//        int page=2;//当前页
//        int pageSize=4;//页面接收数据大小
//        IPage<SysDict> iPage = sysDictService.findPage(map, page, pageSize);
//        List<SysDict> sysDictList = iPage.getRecords();
//        List<SysDictVO> sysDictListForm = new ArrayList<>();
//        for (SysDict sysDict : sysDictList){
//            SysDictVO vo = new SysDictVO();
//            BeanUtils.copyProperties(vo, sysDict);
//            sysDictListForm.add(vo);
//        }
        return ApiResult.success(configs.getEureka());
    }


    @ApiResponses(value = {
            @ApiResponse(code = ConstantSFY.CODE_401, message = ConstantSFY.MESSAGE_401)
    })
    @ApiOperation(value="测试POST", notes="测试POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(method = RequestMethod.POST, value="/v1/testPost")
    @ResponseBody
    public MessageResult testPost(@ApiParam("请求体") @RequestBody MessageResult messageResult){
        return messageResult;
    }
}
