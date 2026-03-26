package com.blockchain.aigc.aspect;

import com.blockchain.aigc.annotation.Log;
import com.blockchain.aigc.entity.OperationLog;
import com.blockchain.aigc.entity.User;
import com.blockchain.aigc.service.OperationLogService;
import com.blockchain.aigc.utils.LogContextUtil;
import com.blockchain.aigc.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 操作日志记录切面
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private OperationLogService operationLogService;

    // 配置织入点
    @Pointcut("@annotation(com.blockchain.aigc.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     * @param result    返回值
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        handleLog(joinPoint, null, result);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object result) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }

            // 获取当前的用户
            User currentUser = UserUtil.get();
            if (currentUser == null) {
                return;
            }

            //========数据库日志=========//
            OperationLog operLog = new OperationLog();
            operLog.setCreateTime(LocalDateTime.now());
            operLog.setUserId(currentUser.getId());
            operLog.setUsername(currentUser.getUsername());

            // 获取请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                operLog.setRequestUrl(request.getRequestURI());
                operLog.setRequestMethod(request.getMethod());
                operLog.setIp(getIpAddr(request));
                operLog.setUserAgent(request.getHeader("User-Agent"));
            }

            operLog.setOperationType(controllerLog.operationType());
            operLog.setModule(controllerLog.module());
            operLog.setDescription(controllerLog.description());
            operLog.setTargetType(controllerLog.targetType());
            operLog.setTargetId(LogContextUtil.get());

            LogContextUtil.clear();

            // 保存数据库
            operationLogService.save(operLog);
        } catch (Exception exp) {
            log.error("操作日志记录异常: {}", exp.getMessage());
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(Log.class);
    }

    /**
     * 获取IP地址
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
