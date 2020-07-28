/*
 * MIT License
 *
 * Copyright (c) 2018 Alibaba Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.fun.tv.launcher.tangram.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.fun.tv.launcher.tangram.R;
import com.fun.tv.launcher.tangram.core.adapter.BinderViewHolder;
import com.fun.tv.launcher.tangram.core.adapter.GroupBasicAdapter;
import com.fun.tv.launcher.tangram.dataparser.concrete.Style;
import com.fun.tv.launcher.tangram.structure.BaseCell;
import com.fun.tv.launcher.tangram.structure.cell.LinearScrollCell;
import com.fun.tv.launcher.tangram.structure.view.ITangramViewLifeCycle;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kunlun on 9/17/16.
 */
public class LinearScrollView extends LinearLayout implements ITangramViewLifeCycle {
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private View indicator, indicatorContainer;

    private LinearScrollCell lSCell;

    // total distance that indicator can move.
    private float totalDistanceOfIndicator = 0;
    // total distance that recycler view can scroll.
    private float totalDistance = 0;

    private List<BinderViewHolder> mViewHolders = new ArrayList<BinderViewHolder>();

    private RecyclerView.ItemDecoration itemDecoration;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (lSCell == null) {
                return;
            }

            lSCell.currentDistance += dx;

            if (lSCell.hasIndicator && totalDistance > 0) {
                float distance = Math.max(0, Math.min((int) (lSCell.currentDistance * totalDistanceOfIndicator
                        / totalDistance + 0.5), totalDistanceOfIndicator));
                indicator.setTranslationX(distance);
            }
        }
    };

    public LinearScrollView(Context context) {
        this(context, null);
    }

    public LinearScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER_HORIZONTAL);
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.tangram_linearscrollview, this);
        setClickable(true);
        recyclerView = (RecyclerView) findViewById(R.id.tangram_linearscrollview_container);

        layoutManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        indicator = findViewById(R.id.tangram_linearscrollview_indicator);
        indicatorContainer = findViewById(R.id.tangram_linearscrollview_indicator_container);

        totalDistanceOfIndicator = Style.dp2px(34);
    }

    @Override
    public void cellInited(BaseCell cell) {
        if (cell instanceof LinearScrollCell) {
            this.lSCell = (LinearScrollCell) cell;
            if (lSCell.maxRows > 1) {
                layoutManager.setSpanCount(lSCell.maxRows);
            } else {
                layoutManager.setSpanCount(1);
            }
            totalDistanceOfIndicator = (float) (this.lSCell.defaultIndicatorWidth - this.lSCell.indicatorWidth);
        }
    }

    @Override
    public void postBindView(BaseCell cell) {
        if (lSCell == null) {
            return;
        }
        recyclerView.setRecycledViewPool(lSCell.getRecycledViewPool());

        recyclerView.removeItemDecoration(itemDecoration);
        if (lSCell.hGap > 0) {
            itemDecoration = new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.set(0, 0, 0, 0);

                    int cellCount = lSCell.cells.size();
                    int viewIndex = (int) view.getTag(R.id.TANGRAM_LINEAR_SCROLL_POS);
                    int rowCount = (int) (lSCell.cells.size() * 1.0f / lSCell.maxRows + 0.5f);

                    boolean isLastCellInRow = false;
                    if ((viewIndex + 1) % rowCount == 0) {
                        // the last cell in every row
                        isLastCellInRow = true;
                    }

                    if (viewIndex == (cellCount - 1)) {
                        // the last cell in cells
                        isLastCellInRow = true;
                    }

                    if (viewIndex % rowCount == 0) {
                        // first view only set right
                        outRect.right = (int) (lSCell.hGap / 2);
                    } else if (isLastCellInRow) {
                        // last view only set left
                        outRect.left = (int) (lSCell.hGap / 2);

                        if (lSCell.maxRows > 1 && (cellCount % lSCell.maxRows == 1) && (viewIndex == cellCount - 1)) {
                            // the last cell in penultimate row
                            outRect.right = (int) (lSCell.hGap / 2);
                        }
                    } else {
                        outRect.left = (int) (lSCell.hGap / 2);
                        outRect.right = (int) (lSCell.hGap / 2);
                    }
                }
            };
            recyclerView.addItemDecoration(itemDecoration);
        }

        float[] starts = null;
        if (lSCell.cells != null && lSCell.cells.size() > 0) {
            int maxRowCount = lSCell.cells.size();
            if (lSCell.maxRows > 1) {
                maxRowCount = (int) (maxRowCount * 1.0f / lSCell.maxRows + 0.5f);
            }

            starts = new float[maxRowCount];
            for (int i = 0; i < maxRowCount; i++) {
                int rowIndex = lSCell.maxRows * i;
                int mapperIndex = lSCell.getMapperPosition(rowIndex);

                starts[i] = totalDistance;

                BaseCell bc = lSCell.cells.get(mapperIndex);
                if (bc.style != null && bc.style.margin.length > 0) {
                    totalDistance = totalDistance + bc.style.margin[1] + bc.style.margin[3];
                }
                if (!Double.isNaN(lSCell.pageWidth)) {
                    if (bc.extras.has("pageWidth")) {
                        totalDistance += Style.parseSize(bc.extras.optString("pageWidth"), 0);
                    } else {
                        totalDistance += lSCell.pageWidth;
                    }
                }
                if (i > 0 && lSCell.hGap > 0) {
                    totalDistance += lSCell.hGap;
                }
            }
        }
        totalDistance -= getScreenWidth();

        // calculate height of recycler view.
        ViewGroup.LayoutParams lp = recyclerView.getLayoutParams();
        if (!Double.isNaN(lSCell.pageHeight)) {
            if (lSCell.maxRows == 0) {
                lp.height = (int) (lSCell.pageHeight + 0.5f);
            } else {
                lp.height = (int) (lSCell.pageHeight * lSCell.maxRows + 0.5f);
            }
            if (lSCell.maxRows > 1 && lSCell.vGap > 0) {
                lp.height += (int) ((lSCell.maxRows - 1) * lSCell.vGap + 0.5f);
            }
        }
        recyclerView.setLayoutParams(lp);

        recyclerView.setAdapter(lSCell.adapter);

        if (lSCell.hasIndicator && totalDistance > 0) {
            setViewColor(indicator, lSCell.indicatorColor);
            setViewColor(indicatorContainer, lSCell.defaultIndicatorColor);
            setIndicatorMeasure(indicator, (int) Math.round(lSCell.indicatorWidth),
                    (int) Math.round(lSCell.indicatorHeight), 0);
            setIndicatorMeasure(indicatorContainer, (int) Math.round(lSCell.defaultIndicatorWidth),
                    (int) Math.round(lSCell.indicatorHeight), (int) Math.round(lSCell.indicatorMargin));
            indicatorContainer.setVisibility(VISIBLE);
        } else {
            indicatorContainer.setVisibility(GONE);
        }

        recyclerView.addOnScrollListener(onScrollListener);

        setBackgroundColor(lSCell.bgColor);

        if (lSCell.retainScrollState && starts != null) {
            GridLayoutManager lm = (GridLayoutManager) recyclerView.getLayoutManager();
            int position = computeFirstCompletelyVisibleItemPositionForScrolledX(starts);
            lm.scrollToPositionWithOffset(position * lSCell.maxRows, (int) (starts[position] - lSCell.currentDistance));
        }
        if (lSCell.scrollMarginLeft > 0 || lSCell.scrollMarginRight > 0) {
            setPadding(lSCell.scrollMarginLeft, 0, lSCell.scrollMarginRight, 0);
            setClipToPadding(false);
            setClipChildren(false);
        } else {
            setPadding(0, 0, 0, 0);
            setClipToPadding(true);
            setClipChildren(true);
        }
        recycleView(lSCell);
        bindHeaderView(lSCell.mHeader);
        bindFooterView(lSCell.mFooter);
    }

    /**
     * Set indicator measure
     *
     * @param indicator indicator view
     * @param width     indicator width
     * @param height    indicator height
     * @param margin    indicator top margin
     */
    private void setIndicatorMeasure(View indicator, int width, int height, int margin) {
        if (indicator != null) {
            ViewGroup.LayoutParams layoutParams = indicator.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;

            if (margin > 0) {
                if (layoutParams instanceof FrameLayout.LayoutParams) {
                    FrameLayout.LayoutParams frameLayoutParams = (FrameLayout.LayoutParams) layoutParams;
                    frameLayoutParams.topMargin = margin;
                } else if (layoutParams instanceof LayoutParams) {
                    LayoutParams linearLayoutParams = (LayoutParams) layoutParams;
                    linearLayoutParams.topMargin = margin;
                }
            }
            indicator.setLayoutParams(layoutParams);
        }
    }

    /**
     * Find the first completely visible position.
     *
     * @param starts A recorder array to save each item's left position, including its margin.
     * @return Position of first completely visible item.
     */
    private int computeFirstCompletelyVisibleItemPositionForScrolledX(float[] starts) {
        if (lSCell == null || starts == null || starts.length <= 0) {
            return 0;
        }
        for (int i = 0; i < starts.length; i++) {
            if (starts[i] >= lSCell.currentDistance) {
                return i;
            }
        }
        return starts.length - 1;
    }

    @Override
    public void postUnBindView(BaseCell cell) {
        if (lSCell == null) {
            return;
        }

        totalDistance = 0;

        if (lSCell.hasIndicator) {
            indicator.setTranslationX(0);
        }

        recyclerView.removeOnScrollListener(onScrollListener);
        recyclerView.setAdapter(null);

        lSCell = null;
        recycleView(cell);
    }

    private void setViewColor(View view, int color) {
        if (view.getBackground() instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) view.getBackground().mutate();
            drawable.setColor(color);
        }
    }

    private int getScreenWidth() {
        Resources resources = getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        return configuration.orientation == Configuration.ORIENTATION_PORTRAIT ?
                dm.widthPixels : dm.heightPixels;
    }

    private void bindHeaderView(BaseCell cell) {
        if (cell.isValid()) {
            View header = getViewFromRecycler(cell);
            if (header != null) {
                header.setId(R.id.TANGRAM_BANNER_HEADER_ID);
                //为了解决在 item 复用过程中，itemView 的 layoutParams 复用造成 layout 错误,这里要提供一个新的 layoutParams。
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.topMargin = cell.style.margin[Style.MARGIN_TOP_INDEX];
                lp.leftMargin = cell.style.margin[Style.MARGIN_LEFT_INDEX];
                lp.bottomMargin = cell.style.margin[Style.MARGIN_BOTTOM_INDEX];
                lp.rightMargin = cell.style.margin[Style.MARGIN_RIGHT_INDEX];
                addView(header, 0, lp);
            }
        }
    }

    private void bindFooterView(BaseCell cell) {
        if (cell.isValid()) {
            View footer = getViewFromRecycler(cell);
            if (footer != null) {
                footer.setId(R.id.TANGRAM_BANNER_FOOTER_ID);
                //为了解决在 item 复用过程中，itemView 的 layoutParams 复用造成 layout 错误,这里要提供一个新的 layoutParams。
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.topMargin = cell.style.margin[Style.MARGIN_TOP_INDEX];
                lp.leftMargin = cell.style.margin[Style.MARGIN_LEFT_INDEX];
                lp.bottomMargin = cell.style.margin[Style.MARGIN_BOTTOM_INDEX];
                lp.rightMargin = cell.style.margin[Style.MARGIN_RIGHT_INDEX];
                addView(footer, lp);
            }
        }
    }

    private View getViewFromRecycler(@NonNull BaseCell cell) {
        GroupBasicAdapter adapter = cell.serviceManager.getService(GroupBasicAdapter.class);
        RecyclerView.RecycledViewPool pool = cell.serviceManager.getService(RecyclerView.RecycledViewPool.class);
        int itemViewType = adapter.getItemType(cell);
        BinderViewHolder holder = (BinderViewHolder) pool.getRecycledView(itemViewType);
        if (holder == null) {
            holder = (BinderViewHolder) adapter.createViewHolder(this, itemViewType);
        }
        holder.bind(cell);
        mViewHolders.add(holder);
        return holder.itemView;
    }

    private void recycleView(@NonNull BaseCell cell) {
        if (!mViewHolders.isEmpty()) {
            RecyclerView.RecycledViewPool pool = cell.serviceManager.getService(RecyclerView.RecycledViewPool.class);
            for (int i = 0, size = mViewHolders.size(); i < size; i++) {
                BinderViewHolder viewHolder = mViewHolders.get(i);
                viewHolder.unbind();
                removeView(viewHolder.itemView);
                pool.putRecycledView(viewHolder);
            }
            mViewHolders.clear();
        }
    }
}
