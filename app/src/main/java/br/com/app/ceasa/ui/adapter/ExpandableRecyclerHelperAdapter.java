package br.com.app.ceasa.ui.adapter;



import java.util.ArrayList;
import java.util.List;

public class ExpandableRecyclerHelperAdapter {


    public static List<Object> generateParentChildItemList(List<? extends ParentListItemAdapter> parentItemList) {
        List<Object> parentWrapperList = new ArrayList<>();
        ParentListItemAdapter parentListItemAdapter;
        ParentWrapperAdapter parentWrapperAdapter;

        int parentListItemCount = parentItemList.size();
        for (int i = 0; i < parentListItemCount; i++) {
            parentListItemAdapter = parentItemList.get(i);
            parentWrapperAdapter = new ParentWrapperAdapter(parentListItemAdapter);
            parentWrapperList.add(parentWrapperAdapter);

            if (parentWrapperAdapter.isInitiallyExpanded()) {
                parentWrapperAdapter.setExpanded(true);

                int childListItemCount = parentWrapperAdapter.getChildItemList().size();
                for (int j = 0; j < childListItemCount; j++) {
                    parentWrapperList.add(parentWrapperAdapter.getChildItemList().get(j));
                }
            }
        }

        return parentWrapperList;
    }
}