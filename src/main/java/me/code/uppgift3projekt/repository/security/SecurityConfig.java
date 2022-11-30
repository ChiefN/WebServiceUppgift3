package me.code.uppgift3projekt.repository.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/admin/**").authenticated()
                .anyRequest().permitAll()
                .and().oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)//This together with our decoder-bean will handle the reading of JWT
                //.httpBasic(Customizer.withDefaults()) //HTTPBasic is a basic filter. Replaces JWTVerifyFilter. Will set SecurityContextHolder and send the correct responses.
                .build();
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("PUT","GET","POST", "DELETE")
                ;
            }
        };
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        SecretKey originalKey = new SecretKeySpec("s/4KMb61LOrMYYAn4rfaQYSgr+le5SMrsMzKw8G6bXc=".getBytes(), "HMACSHA256");
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(originalKey).macAlgorithm(MacAlgorithm.HS256).build();
        return jwtDecoder; //Could build a custom JWTDecoder with auth0 here but Nimbus is included in OauthResourceServer-starter and its probably harder
    }

    @Bean //This part and tokenService is perfect for a layer. SpringBootAuthentication but in this project we work with a in memoryDB(hashmap) so its difficult to sync the DB. And even if i could do it, it isn't something you'll actually do in production.
    public JwtEncoder jwtEncoder(){
        SecretKey originalKey = new SecretKeySpec("s/4KMb61LOrMYYAn4rfaQYSgr+le5SMrsMzKw8G6bXc=".getBytes(), "HMACSHA256");
        JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<SecurityContext>(originalKey);

        return new NimbusJwtEncoder(immutableSecret);
    }

    @Bean //This will handle the authentication by username and password instead of using HTTPBasic
    public AuthenticationManager authManager(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder){
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider);
    }

}
