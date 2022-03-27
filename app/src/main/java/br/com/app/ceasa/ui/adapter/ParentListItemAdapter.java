package br.com.app.ceasa.ui.adapter;

import java.util.List;

public interface ParentListItemAdapter {

    /**
     * Getter for the list of this parent list item's child list items.
     * <p>
     * If list is empty, the parent list item has no children.
     *
     * @return A {@link List} of the children of this {@link ParentListItemAdapter}
     */
    List<?> getChildItemList();

    /**
     * Getter used to determine if this {@link ParentListItemAdapter}'s
     * {@link android.view.View} should show up initially as expanded.
     *
     * @return true if expanded, false if not
     */
    boolean isInitiallyExpanded();

}
