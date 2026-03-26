package com.blockchain.aigc.service;

import com.blockchain.aigc.entity.OperationLog;
import com.blockchain.aigc.mapper.OperationLogMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 操作日志服务实现类
 */
@Service
public class OperationLogService extends ServiceImpl<OperationLogMapper, OperationLog> {
}
