package com.baselibarryapp.data;

import com.baselibarryapp.api.HttpApi;
import com.baselibarryapp.bases.IPBean;
import com.baselibrary.base.basecomponent.basemvp.data.sourcess.IDataSource;
import com.baselibrary.http.HttpManager;
import com.baselibrary.http.intefaces.Action1;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-6-4
 * Description
 * </pre>
 */
public class IpBeanRepository implements IDataSource<IPBean> {
    @Override
    public void getModuleList(Action1<List<IPBean>> soucessCall) {
        HttpManager.getInstan().request(
                HttpManager.getRetrofit().create(HttpApi.class).getIp("json","218.4.255.255")
                ,su->{
                    List<IPBean> lis = new ArrayList<>();
                    lis.add(su);
                    soucessCall.call(lis);
                });
    }
}
