package testcomponent.heyongrui.com.componenta.ui.mono.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import testcomponent.heyongrui.com.componenta.net.dto.MonoCategoryDto;
import testcomponent.heyongrui.com.componenta.net.dto.MonoTabDto;
import testcomponent.heyongrui.com.componenta.net.dto.MonoTeaDto;

/**
 * Created by lambert on 2018/11/5.
 */

public class MonoMultipleItem implements MultiItemEntity {

    public static final int TYPE_ONE = 10001;
    public static final int TYPE_TWO = 10002;
    public static final int TYPE_CATEGORY = 10003;
    public static final int TYPE_TAB_MUSIC = 10004;

    public static final int SPAN_SIZE_ONE = 1;

    private int itemType;
    private int spanSize;
    private MonoTeaDto.EntityListBean entityListBean;
    private MonoCategoryDto.MeowsBean meowsBean;
    private MonoTabDto.EntityListBean tabEntityListBean;

    public MonoMultipleItem(int itemType, MonoTeaDto.EntityListBean entityListBean) {
        this.itemType = itemType;
        this.entityListBean = entityListBean;
    }

    public MonoMultipleItem(int itemType, MonoCategoryDto.MeowsBean meowsBean) {
        this.itemType = itemType;
        this.meowsBean = meowsBean;
    }

    public MonoMultipleItem(int itemType, MonoTabDto.EntityListBean tabEntityListBean) {
        this.itemType = itemType;
        this.tabEntityListBean = tabEntityListBean;
    }

    public MonoMultipleItem(int itemType, int spanSize, MonoTeaDto.EntityListBean entityListBean) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.entityListBean = entityListBean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public MonoTeaDto.EntityListBean getEntityListBean() {
        return entityListBean;
    }

    public void setEntityListBean(MonoTeaDto.EntityListBean entityListBean) {
        this.entityListBean = entityListBean;
    }

    public MonoCategoryDto.MeowsBean getMeowsBean() {
        return meowsBean;
    }

    public void setMeowsBean(MonoCategoryDto.MeowsBean meowsBean) {
        this.meowsBean = meowsBean;
    }

    public MonoTabDto.EntityListBean getTabEntityListBean() {
        return tabEntityListBean;
    }

    public void setTabEntityListBean(MonoTabDto.EntityListBean tabEntityListBean) {
        this.tabEntityListBean = tabEntityListBean;
    }
}
