package com.fun.tv.launcher.tangram.op;

import java.util.List;

import com.fun.tv.launcher.tangram.dataparser.concrete.Card;
import com.fun.tv.launcher.tangram.structure.BaseCell;

/**
 * Created by longerian on 2018/3/26.
 *
 * @author longerian
 * @date 2018/03/26
 */

public class LoadMoreOp extends TangramOp3<Card, List<BaseCell>, Boolean> {

    public LoadMoreOp(Card arg1, List<BaseCell> arg2, Boolean arg3) {
        super(arg1, arg2, arg3);
    }

}
