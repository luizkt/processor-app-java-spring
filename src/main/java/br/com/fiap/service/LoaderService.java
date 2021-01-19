package br.com.fiap.service;

import br.com.fiap.entity.ApplicationResponseBody;

import java.io.IOException;

public interface LoaderService {
    ApplicationResponseBody loadFromCsv() throws IOException;
}
