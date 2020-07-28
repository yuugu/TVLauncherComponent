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

import com.fun.tv.launcher.tangram.op.ClickExposureCellOp;
import com.fun.tv.launcher.tangram.util.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by longerian on 2018/3/7.
 *
 * @author longerian
 * @date 2018/03/07
 */

public class CellClickObservable extends Observable<ClickExposureCellOp> {

    private ClickExposureCellOp mRxClickExposureEvent;

    private RxClickListener mListener;

    public CellClickObservable(ClickExposureCellOp rxClickExposureEvent) {
        Preconditions.checkNotNull(rxClickExposureEvent);
        Preconditions.checkNotNull(rxClickExposureEvent.getArg1());
        this.mRxClickExposureEvent = rxClickExposureEvent;
    }

    public void setRxClickExposureEvent(ClickExposureCellOp rxClickExposureEvent) {
        mRxClickExposureEvent = rxClickExposureEvent;
    }

    @Override
    protected void subscribeActual(Observer<? super ClickExposureCellOp> observer) {
        if (!Preconditions.checkMainThread(observer)) {
            return;
        }
        if (mListener == null) {
            mListener = new RxClickListener(mRxClickExposureEvent, observer);
        } else {
            mListener.setRxClickExposureEvent(mRxClickExposureEvent);
            mListener.setObserver(observer);
        }
        observer.onSubscribe(mListener);
        mRxClickExposureEvent.getArg1().setOnClickListener(mListener);
    }

}
