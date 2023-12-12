package com.project.Patika.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // İzin vermek istediğiniz URL desenini belirtin
                .allowedOrigins("http://localhost:5173") // İzin verilen kaynakları belirtin (istemcinizin URL'si)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // İzin verilen HTTP metotlarını belirtin
                .allowCredentials(true); // İstemci tarafından kimlik bilgilerini paylaşmasına izin vermek için true olarak ayarlayın
    }
}
