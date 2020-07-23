package com.fun.tv.launcher.waterfall.module.support;

import com.fun.tv.launcher.tangram.support.InternalErrorSupport;

import java.util.Map;

public class SampleErrorSupport extends InternalErrorSupport {
    @Override
    public void onError(int code, String msg, Map<String, Object> relativeInfoMap) {


        super.onError(code, msg, relativeInfoMap);
    }
}
