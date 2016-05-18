package com.bbsz.mapplication.plugin.ui.tools;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.View;

/**
 * Created by Administrator on 2016/5/18.
 */
public class MoreActionProvider extends ActionProvider {
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public MoreActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        return new ActionMoreWidget(getContext());
    }
}
