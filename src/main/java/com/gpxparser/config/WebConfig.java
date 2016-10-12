package com.gpxparser.config;

import com.gpxparser.jaxb.GpxType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.xml.bind.Marshaller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 10/12/2016
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ComponentScan({ "com.gpxparser.controller" })
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(marshallingMessageConverter());
    }

    @Bean
    public MarshallingHttpMessageConverter marshallingMessageConverter() {
        MarshallingHttpMessageConverter converter = new MarshallingHttpMessageConverter();
        converter.setMarshaller(jaxbMarshaller());
        converter.setUnmarshaller(jaxbMarshaller());
        return converter;
    }

    @Bean
    public Jaxb2Marshaller jaxbMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(GpxType.class);
        marshaller.setSchema(new ClassPathResource("gpx.xsd"));

        Map<String, Object> props = new HashMap<>();
        props.put("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        props.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        props.put(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        props.put(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.topografix.com/GPX/1/1 " +
                "http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 " +
                "http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd " +
                "http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd");
        marshaller.setMarshallerProperties(props);

        return marshaller;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver () {
        CommonsMultipartResolver  multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000);
//        multipartResolver.setDefaultEncoding("UTF-8");
        return multipartResolver;
    }
}
