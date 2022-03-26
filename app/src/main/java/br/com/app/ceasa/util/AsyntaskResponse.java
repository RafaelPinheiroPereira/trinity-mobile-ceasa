package br.com.app.ceasa.util;

import java.io.Serializable;

public interface AsyntaskResponse<T extends Serializable> {
       void processSaveFinish(T object);
}
