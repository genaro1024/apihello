package com.comedica.apihello.aspect;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.comedica.apihello.common.Audit;

//import com.mroh.tcs.bulletin.bus.KafkaProducerService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;



@Aspect
@Component
@Slf4j
public class RequestAspect {

    //@Autowired
    //private KafkaProducerService kafkaProducerService;

    private static final ThreadLocal<String> requestID = new ThreadLocal<>();

    
    @Pointcut("execution(* com.mroh.tcs.bulletin.controller..*(..))")
    public void controllerPointcut() {
    }


    @Before("controllerPointcut()")
    public void beforeControllerMethod(JoinPoint joinPoint) throws IOException {
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        String uniqueRequestID = UUID.randomUUID().toString();
        requestID.set(uniqueRequestID); 
        String methodName = joinPoint.getSignature().getName();
        //String methodArgs = joinPoint.getArgs() != null ? joinPoint.getArgs().toString() : "Sin argumentos";
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();

        Object[] methodArgsArray = joinPoint.getArgs();
        String methodArgs = Arrays.stream(methodArgsArray)
            .map(arg -> arg != null ? arg.toString() : "null")
            .collect(Collectors.joining(", "));


        Audit audit = new Audit();
        audit.setUuid(UUID.randomUUID().toString());
        audit.setThread(uniqueRequestID);
        audit.setType("request");
        audit.setDate(new Date());
        audit.setParams(methodArgs);
        audit.setBody(requestBody);
        audit.setVerb(httpMethod);
        audit.setUrl(requestURI);   
        audit.setMethod(methodName);    

        // Enviar el mensaje a Kafka
        //kafkaProducerService.sendObjectMessage(audit);
    }

    @AfterReturning(pointcut = "controllerPointcut()", returning = "result")
    public void afterControllerMethod(JoinPoint joinPoint, Object result) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String uniqueRequestID = requestID.get();
        String methodName = joinPoint.getSignature().getName();
        String httpMethod = request.getMethod();

        Audit audit = new Audit();
        audit.setUuid(UUID.randomUUID().toString());
        audit.setThread(uniqueRequestID);
        audit.setType("response");
        audit.setDate(new Date());
        audit.setBody(result.toString());  
        audit.setMethod(methodName);
        audit.setUrl(request.getRequestURI()); 
        audit.setVerb(httpMethod);          
        
        // Enviar el mensaje a Kafka
        //kafkaProducerService.sendObjectMessage(audit);

        requestID.remove();
    }
}
