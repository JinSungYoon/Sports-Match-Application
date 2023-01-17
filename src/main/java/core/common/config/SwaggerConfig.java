package core.common.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
					.select()
					.apis(RequestHandlerSelectors.basePackage("core.api"))
					.paths(PathSelectors.ant("/api/**"))
					.build()
					.apiInfo(apiInfo())
					.useDefaultResponseMessages(false)
					.securityContexts(Arrays.asList(securityContext()))
					.securitySchemes(Arrays.asList(apiKey()));
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("스포츠 클럽 매칭 어플리케이션 API 문서")
				.description("스포츠 클럽 매칭해 주는 기능을 제공하는 서비스의 API 입니다.")
				.version("1.0")
				.build();
	}
	
	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.build();
	}
	
	private List<SecurityReference> defaultAuth(){
		AuthorizationScope authorizationScope = new AuthorizationScope("global","accessEveryThing");	
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("Authorization",authorizationScopes));
	}
	
	private ApiKey apiKey() {
		return new ApiKey("Authorization","Authorization","header");
	}
}
