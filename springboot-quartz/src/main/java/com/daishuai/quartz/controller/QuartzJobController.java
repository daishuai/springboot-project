package com.daishuai.quartz.controller;

import com.daishuai.quartz.dto.QuartzConfigDTO;
import com.daishuai.quartz.service.QuartzJobService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * QuartzJob管理
 *
 * @author Daishuai
 * @version 1.0.0
 * @description QuartzJob管理
 * @createTime 2023年05月12日 10:07:00
 */
@RestController
@RequestMapping(value = "/quartzJob")
public class QuartzJobController {

    @Resource
    private QuartzJobService quartzJobService;

    /**
     * 添加新任务
     *
     * @param configDTO 任务信息
     * @return  结果
     */
    @RequestMapping("/addJob")
    public Object addJob(@RequestBody QuartzConfigDTO configDTO) {
        quartzJobService.addJob(configDTO.getJobClass(), configDTO.getJobName(), configDTO.getGroupName(), configDTO.getCronExpression(), configDTO.getParam());
        return HttpStatus.OK;
    }

    /**
     * 暂停任务
     *
     * @param configDTO 任务信息
     * @return  结果
     */
    @RequestMapping("/pauseJob")
    public Object pauseJob(@RequestBody QuartzConfigDTO configDTO) {
        quartzJobService.pauseJob(configDTO.getJobName(), configDTO.getGroupName());
        return HttpStatus.OK;
    }

    /**
     * 恢复任务
     *
     * @param configDTO 添加结果
     * @return 结果
     */
    @RequestMapping("/resumeJob")
    public Object resumeJob(@RequestBody QuartzConfigDTO configDTO) {
        quartzJobService.resumeJob(configDTO.getJobName(), configDTO.getGroupName());
        return HttpStatus.OK;
    }

    /**
     * 立即运行一次定时任务
     *
     * @param configDTO 任务信息
     * @return 结果
     */
    @RequestMapping("/runOnce")
    public Object runOnce(@RequestBody QuartzConfigDTO configDTO) {
        quartzJobService.runOnce(configDTO.getJobName(), configDTO.getGroupName());
        return HttpStatus.OK;
    }

    /**
     * 更新任务
     *
     * @param configDTO 任务信息
     * @return  结果
     */
    @RequestMapping("/updateJob")
    public Object updateJob(@RequestBody QuartzConfigDTO configDTO) {
        quartzJobService.updateJob(configDTO.getJobName(), configDTO.getGroupName(), configDTO.getCronExpression(), configDTO.getParam());
        return HttpStatus.OK;
    }

    /**
     * 删除任务
     *
     * @param configDTO 任务信息
     * @return  结果
     */
    @RequestMapping("/deleteJob")
    public Object deleteJob(@RequestBody QuartzConfigDTO configDTO) {
        quartzJobService.deleteJob(configDTO.getJobName(), configDTO.getGroupName());
        return HttpStatus.OK;
    }

    /**
     * 启动所有任务
     *
     * @return  结果
     */
    @RequestMapping("/startAllJobs")
    public Object startAllJobs() {
        quartzJobService.startAllJobs();
        return HttpStatus.OK;
    }

    /**
     * 暂停所有任务
     *
     * @return  结果
     */
    @RequestMapping("/pauseAllJobs")
    public Object pauseAllJobs() {
        quartzJobService.pauseAllJobs();
        return HttpStatus.OK;
    }

    /**
     * 恢复所有任务
     *
     * @return  结果
     */
    @RequestMapping("/resumeAllJobs")
    public Object resumeAllJobs() {
        quartzJobService.resumeAllJobs();
        return HttpStatus.OK;
    }

    /**
     * 关闭所有任务
     *
     * @return  结果
     */
    @RequestMapping("/shutdownAllJobs")
    public Object shutdownAllJobs() {
        quartzJobService.shutdownAllJobs();
        return HttpStatus.OK;
    }
}
