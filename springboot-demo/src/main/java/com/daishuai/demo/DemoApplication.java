package com.daishuai.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.daishuai.demo.dto.WaterAllocationDto;
import com.daishuai.demo.dto.WaterAllocationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Daishuai
 * @date 2020/2/11 12:30
 */
@Slf4j
@EnableAspectJAutoProxy
@RestController
@SpringBootApplication
public class DemoApplication {
    private ConcurrentMap<String, List> cacheMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping(value = "/firefighting/api/dispatch/rest/q/externals/jingqing/{incidentCode}/cmd-watersources")
    public WaterAllocationResponse getAllocationInfo(@PathVariable(value = "incidentCode") String incidentCode) {

        List list = cacheMap.get(incidentCode);

        WaterAllocationResponse response = new WaterAllocationResponse();
        response.setErrorCode(200);
        response.setSuccess(true);
        response.setContents(list);
        response.setTotal(list == null ? 0 : list.size());

        return response;
    }


    @PostMapping(value = "/firefighting/api/dispatch/rest/c/externals/jingqing/{incidentCode}/cmd-watersources")
    public WaterAllocationResponse allocateWater(@PathVariable(value = "incidentCode") String incidentCode,
                                                 @RequestBody WaterAllocationDto waterAllocationDto) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String current = format.format(new Date());
        List<Object> data = new ArrayList<>();
        String waterSources = waterAllocationDto.getWaterSources();
        JSONArray waters = JSON.parseArray(waterSources);
        for (int i = 0; i < waters.size(); i++) {
            String uuid = UUID.randomUUID().toString();
            int id = new Random().nextInt(100);
            JSONObject water = waters.getJSONObject(i);
            water.put("id", id);
            water.put("uuid", uuid);
            water.put("jingqingUuid", incidentCode);
            water.put("creator", waterAllocationDto.getAccountName());
            water.put("createDate", current);
            water.put("updateDate", current);
            /*String nickName = water.getString("nickName");
            Integer index = water.getInteger("index");
            String wsType = water.getString("wsType");
            String wsUuid = water.getString("wsUuid");*/
            List<Object> newStations = new ArrayList<>();
            JSONArray stations = water.getJSONArray("cmdStationForms");
            for (int i1 = 0; i1 < stations.size(); i1++) {
                JSONObject station = stations.getJSONObject(i1);
                station.put("id", id);
                station.put("uuid", UUID.randomUUID().toString());
                station.put("cmdWsUuid", uuid);
                station.put("jingqingUuid", incidentCode);
                station.put("creator", waterAllocationDto.getAccountName());
                station.put("createDate", current);
                station.put("updateDate", current);
                newStations.add(station);
                /*String stationName = station.getString("stationName");
                String stationUuid = station.getString("stationUuid");*/
            }
            water.put("jingqingCmdWatersourceZqzdDTOs", newStations);
            water.remove("cmdStationForms");
            data.add(water);
        }

        cacheMap.put(incidentCode, data);
        log.info("incidentCode:{}", incidentCode);
        log.info("waterAllocationDto:{}", JSON.toJSONString(waterAllocationDto));
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

    @GetMapping(value = "/demo")
    public WaterAllocationResponse demo() {
        return new WaterAllocationResponse();
    }
}
