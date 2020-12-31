package com.example.demo.service.impl;

import com.example.demo.dao.OperateLogDao;
import com.example.demo.model.OperateLog;
import com.example.demo.model.OperateLogExample;
import com.example.demo.service.OperateLogService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.xml.ws.soap.Addressing;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/23
 * @Time 15:14
 * @Week 周三
 **/
@Component
@Service
public class OperateLogServiceImpl implements OperateLogService {

    @Autowired
    private OperateLogDao operateLogDao;


    @Override
    public Page<OperateLog> getAllOperateLog(OperateLog operateLog) {
        OperateLogExample operateLogExample = new OperateLogExample();
        return operateLogDao.selectByExample(operateLogExample);
    }
}
