package com.zhangweiwhim.es.controller;

import com.zhangweiwhim.es.document.CarInfoDocument;
import com.zhangweiwhim.es.service.CarInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Description: sprinboot-es
 * Created by zhangwei on 2021/2/1 16:53
 */
@RestController
@RequestMapping("/carInfo")
public class CarInfoController {
    private CarInfoService carInfoService;

    @Autowired
    public CarInfoController(CarInfoService carInfoService) {
        this.carInfoService = carInfoService;
    }

    @GetMapping("test")
    public String test() {
        return "sucess";
    }

    @PostMapping("/searchCarInfo/{input}")
    public List<CarInfoDocument> searchCarInfo(@PathVariable String input) throws Exception {
        return carInfoService.findByInput(input);
    }

    @PostMapping("/searchCarInfo/{input}/{pageSize}/{pageNo}")
    public List<CarInfoDocument> searchCarInfoByPage(@PathVariable String input, @PathVariable int pageNo, @PathVariable int pageSize) throws Exception {
        return carInfoService.findByInputAndPage(input, pageNo, pageSize);
    }
}
