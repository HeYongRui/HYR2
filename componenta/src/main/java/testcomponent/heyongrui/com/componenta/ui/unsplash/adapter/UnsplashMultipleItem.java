package testcomponent.heyongrui.com.componenta.ui.unsplash.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import testcomponent.heyongrui.com.componenta.net.dto.UnsplashPicDto;

/**
 * Created by lambert on 2018/10/24.
 */

public class UnsplashMultipleItem implements MultiItemEntity {
    public static final int TYPE_ONE = 10001;

    public static final int IMG_SPAN_SIZE = 1;

    private int itemType;
    private int spanSize;
    private UnsplashPicDto unsplashPicDto;

    public UnsplashMultipleItem(int itemType, int spanSize, UnsplashPicDto unsplashPicDto) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.unsplashPicDto = unsplashPicDto;
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

    public UnsplashPicDto getUnsplashPicDto() {
        return unsplashPicDto;
    }

    public void setUnsplashPicDto(UnsplashPicDto unsplashPicDto) {
        this.unsplashPicDto = unsplashPicDto;
    }
}
