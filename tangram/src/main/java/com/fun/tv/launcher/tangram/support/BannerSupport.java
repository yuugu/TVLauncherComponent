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

package com.fun.tv.launcher.tangram.support;

import com.fun.tv.launcher.tangram.ext.BannerListener;
import com.fun.tv.launcher.tangram.support.RxBannerScrolledListener.ScrollEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;

/**
 * You can get banner scroll info from this support
 * Created by mikeafc on 17/4/9.
 */
public class BannerSupport {

    private ConcurrentHashMap<String, List<BannerListener>> mSelectedListenerArrayMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, List<BannerListener>> mScrolledListenerArrayMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, List<BannerListener>> mScrollStateListenerArrayMap = new ConcurrentHashMap<>();

    @Deprecated
    private List<BannerListener> listeners = new ArrayList<BannerListener>();

    @Deprecated
    public void registerPageChangeListener(BannerListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Deprecated
    public void unregisterPageChangeListener(BannerListener listener) {
        listeners.remove(listener);
    }

    @Deprecated
    public List<BannerListener> getListeners() {
        return listeners;
    }

    public List<BannerListener> getSelectedListenerById(String id) {
        return mSelectedListenerArrayMap.get(id);
    }

    public List<BannerListener> getScrolledListenerById(String id) {
        return mScrolledListenerArrayMap.get(id);
    }

    public List<BannerListener> getScrollStateChangedListenerById(String id) {
        return mScrollStateListenerArrayMap.get(id);
    }

    public Observable<Integer> observeSelected(String id) {
        List<BannerListener> list = mSelectedListenerArrayMap.get(id);
        if (list == null) {
            list = new ArrayList<>();
            mSelectedListenerArrayMap.put(id, list);
        }
        RxBannerSelectedListener rxBannerSelectedListener = new RxBannerSelectedListener();
        list.add(rxBannerSelectedListener);
        BannerSelectedObservable bannerSupportObservable = new BannerSelectedObservable(rxBannerSelectedListener);
        return bannerSupportObservable;
    }

    public Observable<ScrollEvent> observeScrolled(String id) {
        List<BannerListener> list = mScrolledListenerArrayMap.get(id);
        if (list == null) {
            list = new ArrayList<>();
            mScrolledListenerArrayMap.put(id, list);
        }
        RxBannerScrolledListener rxBannerSelectedListener = new RxBannerScrolledListener();
        list.add(rxBannerSelectedListener);
        BannerScrolledObservable bannerSupportObservable = new BannerScrolledObservable(rxBannerSelectedListener);
        return bannerSupportObservable;
    }

    public Observable<Integer> observeScrollStateChanged(String id) {
        List<BannerListener> list = mScrollStateListenerArrayMap.get(id);
        if (list == null) {
            list = new ArrayList<>();
            mScrollStateListenerArrayMap.put(id, list);
        }
        RxBannerScrollStateChangedListener rxBannerSelectedListener = new RxBannerScrollStateChangedListener();
        list.add(rxBannerSelectedListener);
        BannerScrollStateChangedObservable bannerSupportObservable = new BannerScrollStateChangedObservable(rxBannerSelectedListener);
        return bannerSupportObservable;
    }

    public void destroy() {
        destroyBannerSelected();
        destroyBannerScrolled();
        destroyBannerScrollStateChanged();
    }

    public void destroyBannerSelected() {
        mSelectedListenerArrayMap.clear();
    }

    public void destroyBannerScrolled() {
        mScrolledListenerArrayMap.clear();
    }

    public void destroyBannerScrollStateChanged() {
        mScrollStateListenerArrayMap.clear();
    }

}