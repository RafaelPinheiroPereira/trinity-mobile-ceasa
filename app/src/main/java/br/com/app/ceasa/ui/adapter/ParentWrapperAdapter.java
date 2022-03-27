package br.com.app.ceasa.ui.adapter;

import java.util.List;

public class ParentWrapperAdapter {

    private boolean mExpanded;

    private ParentListItemAdapter mParentListItemAdapter;

    /**
     * Default constructor.
     *
     * @param parentListItemAdapter The {@link ParentListItemAdapter} to wrap
     */
    public ParentWrapperAdapter(ParentListItemAdapter parentListItemAdapter) {
        mParentListItemAdapter = parentListItemAdapter;
        mExpanded = false;
    }

    /**
     * Gets the {@link ParentListItemAdapter} being wrapped.
     *
     * @return The {@link ParentListItemAdapter} being wrapped
     */
    public ParentListItemAdapter getParentListItem() {
        return mParentListItemAdapter;
    }

    /**
     * Sets the {@link ParentListItemAdapter} to wrap.
     *
     * @param parentListItemAdapter The {@link ParentListItemAdapter} to wrap
     */
    public void setParentListItem(ParentListItemAdapter parentListItemAdapter) {
        mParentListItemAdapter = parentListItemAdapter;
    }

    /**
     * Gets the expanded state associated with the {@link ParentListItemAdapter}.
     *
     * @return true if expanded, false if not
     */
    public boolean isExpanded() {
        return mExpanded;
    }

    /**
     * Sets the expanded state associated with the {@link ParentListItemAdapter}.
     *
     * @param expanded true if expanded, false if not
     */
    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    public boolean isInitiallyExpanded() {
        return mParentListItemAdapter.isInitiallyExpanded();
    }

    public List<?> getChildItemList() {
        return mParentListItemAdapter.getChildItemList();
    }
}
