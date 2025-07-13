package config;

import config.conditions.SchedulingEnabledCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.EnableScheduling;

@Conditional(SchedulingEnabledCondition.class)
@EnableScheduling
public class SchedulingConfig {
}
