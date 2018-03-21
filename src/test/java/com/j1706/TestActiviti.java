package com.j1706;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

import java.util.List;

/**
 * Created by wanghao on 2018/3/21.
 */
public class TestActiviti {
    /**
     * 配置activiti的环境（在数据库中完成所需表的创建）
     */
    @Test
    public void testCase1(){
        //加载配置文件
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        //创建流程引擎
        ProcessEngine processEngine = configuration.buildProcessEngine();
    }

    /**
     * 发布流程（将绘制的BPMN图部署到数据库中）
     */
    @Test
    public void testCase2(){
        //获取默认的流程引擎（就是testCase1里的processEngine，但不会再创建表，表只创建一次）
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取流程部署服务
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //流程部署
        Deployment deployment = repositoryService.createDeployment()
                .name("helloworld")
                //指定流程图
                .addClasspathResource("helloworld.bpmn")
                //部署
                .deploy();

        String key = deployment.getKey();
        String name = deployment.getName();
        System.out.println(key);
        System.out.println(name);
    }

    /**
     * 启动流程
     */
    @Test
    public void testCase3(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //runtimeService用来启动流程
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //启动流程，参数是bmpn晚间的id(点击空白处)
        //  启动流程含义：如果李四要请假，那么李四要走请假流程，也就是说，李四要启动请假流程
        runtimeService.startProcessInstanceByKey("myProcess_1");
    }

    /**
     * 查询用户名下的任务
     */
    @Test
    public void testCase4(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //taskService用来操作任务的服务
        TaskService taskService = defaultProcessEngine.getTaskService();
        //查询任务的工具类
        TaskQuery taskQuery = taskService.createTaskQuery();
        //指定要查询哪一个代理人的任务，参数：代理人的名称
        taskQuery.taskAssignee("刘备");
        List<Task> list = taskQuery.list();
        for (Task task:list) {
            System.out.println("ID: "+task.getId());
            System.out.println("任务名称： "+task.getName());
            System.out.println("分类： "+task.getCategory());
            System.out.println("受理人： "+task.getAssignee());
        }
    }

    /**
     * 任务受理（处理、完成）
     */
    @Test
    public void testCase5(){
        ProcessEngines.getDefaultProcessEngine()
                .getTaskService()
                .complete("2505");//任务id 完成任务
    }

}
