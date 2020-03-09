package cn.ftf.myblog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Pointcut("execution(* cn.ftf.myblog.web.*.*(..))")  //拦截所有的controller
    public void log(){

    }
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        String url=request.getRequestURI();
        String ip=request.getRemoteAddr();
        String contextPath=request.getContextPath();
        //获取请求的方法名，需要joinPoint对象
        String classPath=joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        Object[] args=joinPoint.getArgs();
        RequestInfo requestInfo=new RequestInfo(url,ip,classPath,args);
        logger.info(requestInfo.toString());
    }
    @After("log()")
    public void doAfter(){

    }
    //在请求返回的时候拦截处理
    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result){
        logger.info("result:",result);
    }

    //内部类
    private class RequestInfo{
        private String url;
        private String ip;
        private String classPath;
        private Object[] args;

       public RequestInfo(String url, String ip, String classPath, Object[] args) {
           this.url = url;
           this.ip = ip;
           this.classPath = classPath;
           this.args = args;
       }

       public String getUrl() {
           return url;
       }

       public void setUrl(String url) {
           this.url = url;
       }

       public String getIp() {
           return ip;
       }

       public void setIp(String ip) {
           this.ip = ip;
       }

       public String getClassPath() {
           return classPath;
       }

       public void setClassPath(String classPath) {
           this.classPath = classPath;
       }

       public Object[] getArgs() {
           return args;
       }

       public void setArgs(Object[] args) {
           this.args = args;
       }

       @Override
       public String toString() {
           return "RequestInfo{" +
                   "url='" + url + '\'' +
                   ", ip='" + ip + '\'' +
                   ", classPath='" + classPath + '\'' +
                   ", args=" + Arrays.toString(args) +
                   '}';
       }
   }
}
