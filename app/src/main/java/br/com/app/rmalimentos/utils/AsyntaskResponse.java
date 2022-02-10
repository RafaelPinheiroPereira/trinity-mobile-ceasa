package br.com.app.rmalimentos.utils;

import java.io.Serializable;

public interface AsyntaskResponse<T extends Serializable> {
       void processSaveFinish(T object);
}
