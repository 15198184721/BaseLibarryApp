package com.baselibarryapp.data.remote;

import com.baselibarryapp.api.HttpApi;
import com.baselibarryapp.bases.IPBean;
import com.baselibrary.base.basecomponent.basemvp.data.sourcess.IDataSource;
import com.baselibrary.http.HttpManager;
import com.baselibrary.http.intefaces.Action1;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-6-4
 * Description
 * </pre>
 */
public class IpBeanDataSources implements IDataSource<IPBean> {
    @Override
    public void getModuleList(Action1<List<IPBean>> soucessCall) {
        Observable<IPBean> obs = HttpManager.getRetrofit().create(HttpApi.class).getIp("json","218.4.255.255");
        HttpManager.getInstan().request(obs, su->{
            List<IPBean> list = new ArrayList<>();
            list.add(su);
            soucessCall.call(list);
        });
    }
}
