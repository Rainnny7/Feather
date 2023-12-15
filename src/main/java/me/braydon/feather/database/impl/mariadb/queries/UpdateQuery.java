/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.database.impl.mariadb.queries;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import me.braydon.feather.database.impl.mariadb.MariaDB;

import java.util.Map;

/**
 * A builder for {@link MariaDB} update queries.
 *
 * @author Braydon
 */
@Builder @Getter
public class UpdateQuery {
    /**
     * The table to execute the update in.
     */
    @NonNull private String table;
    
    /**
     * The mapped values (by column) for this query.
     */
    @NonNull @Singular private Map<String, Object> values;
    
    /**
     * The where clause for this query.
     */
    @NonNull private WhereClause whereClause;
    
    /**
     * Build this query.
     *
     * @return the built query
     */
    @Override @NonNull
    public String toString() {
        // Build the values
        StringBuilder valuesBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            valuesBuilder.append("`").append(entry.getKey()).append("`")
                .append("='").append(entry.getValue()).append("', ");
        }
        String values = valuesBuilder.toString(); // The built values string
        values = values.substring(0, values.length() - 2); // Remove the trailing comma
        
        // Build the query
        return String.format("UPDATE `%s` SET %s WHERE %s;",
            table, values, whereClause
        );
    }
    
    /**
     * The where clause for this query.
     */
    @Builder @Getter
    public static class WhereClause {
        /**
         * The column to target.
         */
        @NonNull private String column;
        
        /**
         * The value of the column to target.
         */
        @NonNull private Object value;
        
        @Override @NonNull
        public String toString() {
            return column + "='" + value + "'";
        }
    }
}