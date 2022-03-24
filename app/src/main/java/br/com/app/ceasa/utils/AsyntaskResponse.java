package br.com.app.ceasa.utils;

import java.io.Serializable;

public interface AsyntaskResponse<T extends Serializable> {
       void processSaveFinish(T object);
}
