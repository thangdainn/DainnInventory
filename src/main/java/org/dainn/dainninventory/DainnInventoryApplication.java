package org.dainn.dainninventory;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DainnInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DainnInventoryApplication.class, args);
    }

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dciqj149d",
                "api_key", "211941132118145",
                "api_secret", "WpNssVnJ1ZuNPWF6lsT0juYCf1A",
                "secure", true
        ));
    }
}
