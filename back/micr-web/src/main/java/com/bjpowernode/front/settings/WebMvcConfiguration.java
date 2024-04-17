package com.bjpowernode.front.settings;

import com.bjpowernode.front.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtSecret);
        String[] addPath = {"/v1/user/realname"};
        registry.addInterceptor(tokenInterceptor).addPathPatterns(addPath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("===========addCorsMappings===========");
        /**处理的请求地址，拦截这些地址 ，使用跨域处理逻辑*/
        registry.addMapping("/**")
                /**可跨域的域名，可以为* */
                .allowedOriginPatterns("http://localhost:8080")
                /**允许跨域的方法 */
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                /**是否允许 浏览器发送Cookie */
                .allowCredentials(true)
                /**预检请求有效期 */
                .maxAge(3600)
                /**支持跨域的请求头，请求头包含哪些数据时可支持跨域功能 */
                .allowedHeaders("*");
    }
}
