package personal.dvinov.calendar.service.api.common.util;

import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class ContextLogger {
    public static void logContext(Context context) {
        final LambdaLogger logger = context.getLogger();
        
        logger.log("Context: " + context);
        
        if (context.getIdentity() != null) {
            final CognitoIdentity identity = context.getIdentity();
            
            logger.log("Cognito: " + identity);
            if (identity.getIdentityPoolId() != null) {
                logger.log("Cognito pool id: " + identity.getIdentityPoolId());
            }
            if (identity.getIdentityId() != null) {
                logger.log("Cognito identity id: " + identity.getIdentityId());
            }
        }
    }
}
