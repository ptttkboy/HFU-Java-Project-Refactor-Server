package com.hfu.project_server.security.member_web_api.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 讀取application.properties中，下標為project.jwt的文件
 *
 * 注意：
 *  1. @ConfigurationProperties註解後，會在target/class/META_INF/spring-configuration-metadata.json中生成設定檔，即可以在application.properties中寫入對應檔案。
 *  2. 命名對應：  application -> header-prefix 對映 JavaClass -> headerPrefix
 *  2. @Configuration是將properties檔案bean化。
 *  3. 一定要有setter和getter，若無則無法寫入該類的屬性。
 *  4. 密鑰其實不該寫在這裡。
 */
@Configuration
@ConfigurationProperties(prefix = "project.jwt")
@Data
public class JwtProperties {

    private String secret;
    private String headerPrefix;
    private int accessTokenExpirationDays;
    private int refreshTokenExpirationDays;

}
