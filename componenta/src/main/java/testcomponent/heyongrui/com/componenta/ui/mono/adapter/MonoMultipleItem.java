package testcomponent.heyongrui.com.componenta.ui.mono.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import testcomponent.heyongrui.com.componenta.net.dto.MonoTeaDto;

/**
 * Created by lambert on 2018/11/5.
 */

public class MonoMultipleItem implements MultiItemEntity {

    public static final int TYPE_ONE = 10001;

    public static final int IMG_SPAN_SIZE = 1;

    private int itemType;
    private int spanSize;
    private MonoTeaDto.EntityListBean entityListBean;

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
}
