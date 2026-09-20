package br.com.iqe.integration.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseTestSupport {

    protected Connection openConnection()
            throws SQLException {

        String url = System.getenv()
                .getOrDefault(
                        "DB_URL",
                        "jdbc:postgresql://localhost:5433/investment_qe"
                );

        String user = System.getenv()
                .getOrDefault(
                        "DB_USER",
                        "iqe_user"
                );

        String password = System.getenv()
                .getOrDefault(
                        "DB_PASSWORD",
                        "iqe_password"
                );

        return DriverManager.getConnection(
                url,
                user,
                password
        );
    }
}