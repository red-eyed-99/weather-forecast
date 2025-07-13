package config.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class SchedulingEnabledCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        var enabled = context.getEnvironment()
                .getProperty("scheduling.enabled", "true");
        return Boolean.parseBoolean(enabled);
    }
}
