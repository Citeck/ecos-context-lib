package ru.citeck.ecos.context.lib.spring.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Autoconfiguration to initialize context-lib beans.
 */
@Configuration
@ComponentScan(basePackages = ["ru.citeck.ecos.context.lib.spring"])
open class ContextLibAutoConfiguration
