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

import android.support.annotation.NonNull;
import android.util.Log;

import com.fun.tv.launcher.tangram.support.HandlerTimer.TimerStatus;
import com.fun.tv.launcher.tangram.support.TimerSupport.OnTickListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by longerian on 2018/3/8.
 *
 * @author longerian
 * @date 2018/03/08
 */

public class RxTimer implements ITimer {

    private long mInterval;

    private TimerStatus mStatus;

    private boolean pause;

    private Observable<Long> mIntervalObservable;

    private ConcurrentHashMap<OnTickListener, Disposable> tickCache = new ConcurrentHashMap<>();

    public RxTimer(long interval) {
        this.mInterval = interval;
        this.mStatus = TimerStatus.Waiting;
        this.mIntervalObservable = Observable
                .interval(0, this.mInterval, TimeUnit.MILLISECONDS)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mStatus = TimerStatus.Running;
                        Log.d("RxTimerSupportTest", "accept " + disposable + " status " + mStatus);
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        mStatus = TimerStatus.Paused;
                        Log.d("RxTimerSupportTest", "on dispose " + " status " + mStatus);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        mStatus = TimerStatus.Stopped;
                        Log.d("RxTimerSupportTest", "on terminate " + " status " + mStatus);
                    }
                })
                .share();
    }

    public void register(final int interval, @NonNull final OnTickListener onTickListener, boolean intermediate) {
        Disposable disposable = this.mIntervalObservable.delaySubscription(intermediate ? 0 : interval * mInterval, TimeUnit.MILLISECONDS).skipWhile(

                new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return pause;
                    }
                }).observeOn(
                AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {

            @Override
            public void accept(Long count) throws Exception {
                Log.d("RxTimerSupportTest", "accept " + " value " + count);
                if (count % interval == 0) {
                    if (onTickListener != null) {
                        onTickListener.onTick();
                    }
                }
            }
        });
        tickCache.put(onTickListener, disposable);
    }

    public void unregister(OnTickListener onTickListener) {
        Disposable disposable = tickCache.get(onTickListener);
        if (disposable != null) {
            disposable.dispose();
            tickCache.remove(onTickListener);
        }
    }

    public boolean isRegistered(@NonNull OnTickListener onTickListener) {
        return tickCache.containsKey(onTickListener);
    }

    @Override
    public void start() {
        //no need
    }

    @Override
    public void start(boolean bySecond) {
        //no need
    }

    @Override
    public void pause() {
        pause = true;
        mStatus = TimerStatus.Paused;
    }

    @Override
    public void restart() {
        pause = false;
        if (mStatus == TimerStatus.Paused) {
            mStatus = TimerStatus.Running;
        }
    }

    @Override
    public void stop() {
        for (Map.Entry<OnTickListener, Disposable> entry : tickCache.entrySet()) {
            Disposable disposable = entry.getValue();
            disposable.dispose();
        }
        tickCache.clear();
    }

    @Override
    public void cancel() {
        //no need
    }

    @Override
    public void clear() {
        tickCache.clear();
    }

    @Override
    public TimerStatus getStatus() {
        return mStatus;
    }
}
