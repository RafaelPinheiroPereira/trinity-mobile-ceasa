package br.com.app.ceasa.model.entity;

import java.util.List;

import br.com.app.ceasa.ui.adapter.ParentListItemAdapter;

public class Home implements ParentListItemAdapter {
    Client client;
    @Override
    public List<?> getChildItemList() {
        return client.getPayments();
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
