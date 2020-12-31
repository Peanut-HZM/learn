package com.example.demo.service;

import com.example.demo.model.OperateLog;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;

/**
 * @author peanu
 */
@Service
public interface OperateLogService {
    Page<OperateLog> getAllOperateLog(OperateLog operateLog);
}
