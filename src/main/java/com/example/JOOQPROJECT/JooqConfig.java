// package com.example.JOOQPROJECT;

// import org.jooq.DSLContext;
// import org.jooq.SQLDialect;
// import org.jooq.impl.DSL;
// import org.jooq.conf.Settings;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import javax.sql.DataSource;

// @Configuration
// public class JooqConfig {

//     @Bean
//     public DSLContext dsl(DataSource dataSource) {
//         Settings settings = new Settings()
//                                 .withRenderSchema(true); // ensures schema is included
//         return DSL.using(dataSource, SQLDialect.POSTGRES, settings);
//     }
// }
