/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.database.impl.mariadb;

import com.zaxxer.hikari.HikariConfig;
import lombok.*;

/**
 * Authorization for a {@link MariaDB} database.
 *
 * @author Braydon
 */
@RequiredArgsConstructor @AllArgsConstructor @Getter @ToString
public final class MariaDBAuthorization {
    /**
     * The JDBC connection URL for the database.
     */
    @NonNull private final String jdbcUrl;
    
    /**
     * The username to use for authenticating with the database.
     */
    @NonNull private final String username;
    
    /**
     * The password to use for authenticating with the database.
     */
    @NonNull private final String password;
    
    /**
     * The optional {@link HikariConfig} to use.
     */
    private HikariConfig hikariConfig;
}