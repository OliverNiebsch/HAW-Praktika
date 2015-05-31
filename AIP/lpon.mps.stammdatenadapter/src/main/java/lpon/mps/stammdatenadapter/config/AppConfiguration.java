package lpon.mps.stammdatenadapter.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Base configuration of Application referenced in web.xml or via spring setup
 *
 */
@Configuration
@ComponentScan(basePackages = "lpon.mps.stammdatenadapter")
public class AppConfiguration {
}