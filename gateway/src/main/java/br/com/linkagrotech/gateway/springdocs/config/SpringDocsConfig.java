package br.com.linkagrotech.gateway.springdocs.config;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SpringDocsConfig {

    @Bean
    @Lazy(false)
    Set<SwaggerUrl> apis(RouteDefinitionLocator locator, SwaggerUiConfigParameters swaggerUiConfigParameters) {
	Set<SwaggerUrl> urls = new HashSet<>();
	List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
	definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service"))
		.forEach(routeDefinition -> {
		    String name = routeDefinition.getId().replace("-service", "");
		    SwaggerUrl swaggerUrl = new SwaggerUrl(name, DEFAULT_API_DOCS_URL + "/" + name, null);
		    urls.add(swaggerUrl);
		});
	swaggerUiConfigParameters.setUrls(urls);
	return urls;
    }
}
