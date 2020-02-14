package com.daishuai.demo;

import com.alibaba.fastjson.JSON;
import com.daishuai.demo.dto.WaterAllocationDto;
import com.daishuai.demo.dto.WaterAllocationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

/**
 * @author Daishuai
 * @date 2020/2/11 12:30
 */
@Slf4j
@RestController
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/firefighting/api/dispatch/rest/q/jingqings/{incidentCode}/cmd-watersources")
    public WaterAllocationResponse getAllocationInfo(@PathVariable(value = "incidentCode") String incidentCode) {
        String result = "{\n" +
                "    \"success\": true,\n" +
                "    \"targetId\": null,\n" +
                "    \"errorCode\": 200,\n" +
                "    \"message\": null,\n" +
                "    \"total\": 2,\n" +
                "    \"contents\": [\n" +
                "        {\n" +
                "            \"id\": 62,\n" +
                "            \"uuid\": \"c13f92d1-f866-46b8-9d90-8ab71a4e460b\",\n" +
                "            \"jingqingUuid\": \"e69a0960-ad44-40db-8123-8c4a49364d5b\",\n" +
                "            \"wsType\": \"WATERSOURCE_FIRE_HYDRANT\",\n" +
                "            \"wsUuid\": \"01536cff7b-013a-413a-9266-d2edf85028cf\",\n" +
                "            \"nickName\": \"1号\",\n" +
                "\"index\": 1,\n" +
                "            \"command\": null,\n" +
                "            \"creator\": \"大连支队姓名\",\n" +
                "            \"createDate\": \"2019-10-23 17:14:55\",\n" +
                "            \"updateDate\": \"2019-10-23 17:14:55\",\n" +
                "            \"remark\": null,\n" +
                "            \"jingqingCmdWatersourceZqzdDTOs\": [\n" +
                "                {\n" +
                "                    \"id\": 62,\n" +
                "                    \"uuid\": \"42daec49-c10f-49ab-82f8-f9d52bbe302a\",\n" +
                "                    \"jingqingUuid\": \"e69a0960-ad44-40db-8123-8c4a49364d5b\",\n" +
                "                    \"cmdWsUuid\": \"c13f92d1-f866-46b8-9d90-8ab71a4e460b\",\n" +
                "                    \"stationUuid\": \"2ddfae33-e677-4d99-9871-c8b608583d84\",\n" +
                "                    \"stationName\": \"台山中队\",\n" +
                "                    \"nickName\": null,\n" +
                "                    \"devId\": null,\n" +
                "                    \"command\": null,\n" +
                "                    \"creator\": \"大连支队姓名\",\n" +
                "                    \"createDate\": \"2019-10-23 17:14:55\",\n" +
                "                    \"updateDate\": \"2019-10-23 17:14:55\",\n" +
                "                    \"remark\": null\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 63,\n" +
                "            \"uuid\": \"c68705a8-1822-40fb-8fdb-0822b9a073b0\",\n" +
                "            \"jingqingUuid\": \"e69a0960-ad44-40db-8123-8c4a49364d5b\",\n" +
                "            \"wsType\": \"WATERSOURCE_FIRE_HYDRANT\",\n" +
                "            \"wsUuid\": \"01ad7fcdbb-2a9a-4a6c-9c32-cae9ad887080\",\n" +
                "            \"nickName\": \"5号\",\n" +
                "\"index\": 5,\n" +
                "            \"command\": null,\n" +
                "            \"creator\": \"大连支队姓名\",\n" +
                "            \"createDate\": \"2019-10-23 17:14:55\",\n" +
                "            \"updateDate\": \"2019-10-23 17:14:55\",\n" +
                "            \"remark\": null,\n" +
                "            \"jingqingCmdWatersourceZqzdDTOs\": [\n" +
                "                {\n" +
                "                    \"id\": 68,\n" +
                "                    \"uuid\": \"b65bdf35-c54f-48ac-9e22-bfed5a72568d\",\n" +
                "                    \"jingqingUuid\": \"e69a0960-ad44-40db-8123-8c4a49364d5b\",\n" +
                "                    \"cmdWsUuid\": \"c68705a8-1822-40fb-8fdb-0822b9a073b0\",\n" +
                "                    \"stationUuid\": \"2ddfae33-e677-4d99-9871-c8b608583d84\",\n" +
                "                    \"stationName\": \"台山中队\",\n" +
                "                    \"nickName\": null,\n" +
                "                    \"devId\": null,\n" +
                "                    \"command\": null,\n" +
                "                    \"creator\": \"大连支队姓名\",\n" +
                "                    \"createDate\": \"2020-02-10 14:49:45\",\n" +
                "                    \"updateDate\": \"2020-02-10 14:49:45\",\n" +
                "                    \"remark\": null\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        return JSON.parseObject(result, WaterAllocationResponse.class);
    }


    @PostMapping(value = "/firefighting/api/dispatch/rest/c/jingqings/{incidentCode}/cmd-watersources")
    public WaterAllocationResponse allocateWater(@PathVariable(value = "incidentCode") String incidentCode,
                                                 @RequestBody WaterAllocationDto waterAllocationDto) {
        log.info("incidentCode:{}", incidentCode);
        log.info("waterAllocationDto:" + JSON.toJSONString(waterAllocationDto));
        String result = "{\n" +
                "    \"success\": true,\n" +
                "    \"targetId\": null,\n" +
                "    \"errorCode\": 200,\n" +
                "    \"message\": null,\n" +
                "    \"total\": 0,\n" +
                "    \"contents\": []\n" +
                "}";
        return JSON.parseObject(result, WaterAllocationResponse.class);
    }
}
