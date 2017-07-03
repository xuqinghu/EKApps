package com.fugao.formula.utils.swipe.interfaces;

import com.fugao.formula.utils.swipe.SwipeLayout;
import com.fugao.formula.utils.swipe.util.Attributes;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public interface SwipeItemMangerInterface {
    void openItem(int position);

    void closeItem(int position);

    void closeAllExcept(SwipeLayout layout);

    void closeAllItems();

    List<Integer> getOpenItems();

    List<SwipeLayout> getOpenLayouts();

    void removeShownLayouts(SwipeLayout layout);

    boolean isOpen(int position);

    Attributes.Mode getMode();

    void setMode(Attributes.Mode mode);
}
