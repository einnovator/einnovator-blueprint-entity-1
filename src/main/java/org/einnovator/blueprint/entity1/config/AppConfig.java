package org.einnovator.blueprint.entity1.config;

import org.einnovator.clipboard.ClipboardConfig;
import org.einnovator.common.config.AppConfiguration;
import org.einnovator.common.config.UIConfiguration;
import org.einnovator.common.web.InfrastructureConfig;
import org.einnovator.documents.client.config.DocumentsClientConfig;
import org.einnovator.notifications.client.config.NotificationsClientConfig;
import org.einnovator.social.client.config.SocialClientConfig;
import org.einnovator.sso.client.config.SsoClientSecurityConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties({AppConfiguration.class, UIConfiguration.class, AppConfiguration.class})
@Import({ SsoClientSecurityConfigurer.class, NotificationsClientConfig.class, DocumentsClientConfig.class, SocialClientConfig.class,
	ClipboardConfig.class, InfrastructureConfig.class })
@Configuration
public class AppConfig {

}
