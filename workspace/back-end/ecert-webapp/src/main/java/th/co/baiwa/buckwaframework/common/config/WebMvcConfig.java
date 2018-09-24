//package th.co.baiwa.buckwaframework.common.config;
//
//import java.nio.charset.StandardCharsets;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//@Configuration
//@EnableWebMvc
//public class WebMvcConfig extends WebMvcConfigurer {
//	
//	private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);
//	
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		// Configure from WebMvcAutoConfiguration.addResourceHandlers()
//		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//		registry.addResourceHandler("/**").addResourceLocations(new String[] {
//			"classpath:/META-INF/resources/",
//			"classpath:/resources/",
//			"classpath:/static/",
//			"classpath:/public/",
//			"/"
//		});
//		// For Swagger2
//		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//	}
//	
//	@Bean(name = "multipartResolver")
//	public CommonsMultipartResolver multipartResolver() {
//		logger.info("Multipart Resolver");
//
//		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//		resolver.setDefaultEncoding(StandardCharsets.UTF_8.name());
//		
//		return resolver;
//	}
//	
////	@Override
////	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
////		Gson gson = new GsonBuilder()
////			.setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss")
////			.serializeNulls()
////			.registerTypeAdapter(Json.class, new JsonSerializer<Json>() {
////				@Override
////				public JsonElement serialize(Json src, Type typeOfSrc, JsonSerializationContext context) {
////					final JsonParser parser = new JsonParser();
////					return parser.parse(src.value());
////				}
////			})
////			.create();
////
////		GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
////		gsonConverter.setGson(gson);
////		
////		converters.add(gsonConverter);
////	}
//	
//}
