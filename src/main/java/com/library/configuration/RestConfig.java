package com.library.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.library.entity.BookEntity;
import com.library.entity.MessageEntity;
import com.library.entity.ReviewEntity;



@Configuration
public class RestConfig implements RepositoryRestConfigurer  {

    private String ALLOWED_ORIGIN = "http://localhost:5173";

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,CorsRegistry cors){
        HttpMethod[] unsportedAction = {HttpMethod.POST,HttpMethod.PATCH,HttpMethod.DELETE,HttpMethod.PUT};

        config.exposeIdsFor(BookEntity.class);
        config.exposeIdsFor(ReviewEntity.class);
        config.exposeIdsFor(MessageEntity.class);

        disableHttpMethods(BookEntity.class,config,unsportedAction);
        disableHttpMethods(ReviewEntity.class,config,unsportedAction);
        disableHttpMethods(MessageEntity.class,config,unsportedAction);

        cors.addMapping(config.getBasePath()+"/**")
            .allowedOrigins(ALLOWED_ORIGIN);
    }
    
    public void disableHttpMethods(Class theClass,RepositoryRestConfiguration config,HttpMethod[] unSupportActions){
            config.getExposureConfiguration()
                    .forDomainType(theClass)
                    .withItemExposure((metdata,httpmethods)->
                        httpmethods.disable(unSupportActions)
                    )
                    .withCollectionExposure((metdata,httpmethods)->
                        httpmethods.disable(unSupportActions)
                    );
    }
}
