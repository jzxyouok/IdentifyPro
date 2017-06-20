package com.bobao.identifypro.ui.widget.indicator;

public interface IconPagerAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     *
     * @return the icon res
     */
    int getIconTopResId(int index);

    /**
     * Get icon representing the page at {@code index} in the adapter.
     *
     * @return the color of bottom icon
     */
    int getIconBottomColor(int index);

    /**
     * Get icon representing the page at {@code index} in the adapter.
     *
     * @return the color of text
     */
    int getTextColor(int index);

    /**
     * From PagerAdapter
     */
    int getCount();
}
