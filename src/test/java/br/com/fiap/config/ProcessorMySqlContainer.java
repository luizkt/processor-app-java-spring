package br.com.fiap.config;

import org.testcontainers.containers.MySQLContainer;

public class ProcessorMySqlContainer extends MySQLContainer {

    private static final String IMAGE_VERSION = "mysql:5.7";

    private static ProcessorMySqlContainer container;


    private ProcessorMySqlContainer() {
        super(IMAGE_VERSION);
    }

    public static ProcessorMySqlContainer getInstance() {
        if (container == null) {
            container = new ProcessorMySqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
