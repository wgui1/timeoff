package cn.timeoff.security.core;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;


public class DomainSecurityAuthenticationToken extends UsernamePasswordAuthenticationToken
                                               implements DomainAuthentication
{
   private static final long serialVersionUID = 1;
   private String domain = null;

   public DomainSecurityAuthenticationToken (Object principal, Object credentials, String domain) {
      super (principal, credentials);
      this.domain = domain;
   }

   public DomainSecurityAuthenticationToken(Object principal, Object credentials,
                                            Collection<? extends GrantedAuthority> authorities,
                                            String domain) {
      super (principal, credentials, authorities);
      this.domain = domain;
   }

   @Override
   public final String getDomain() {
      return domain;
   }

   public final void setDomain(String domain) {
      this.domain = domain;
   }
}