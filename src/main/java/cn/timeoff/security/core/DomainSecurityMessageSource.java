package cn.timeoff.security.core;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;


public class DomainSecurityMessageSource extends ResourceBundleMessageSource {
    //~ Constructors ===================================================================================================

    public DomainSecurityMessageSource() {
        setBasename("cn.timeoff.security.core.messages");
    }

    //~ Methods ========================================================================================================

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(new DomainSecurityMessageSource());
    }
}