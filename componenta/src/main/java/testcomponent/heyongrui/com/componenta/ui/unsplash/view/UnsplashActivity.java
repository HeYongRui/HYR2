package testcomponent.heyongrui.com.componenta.ui.unsplash.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import testcomponent.heyongrui.com.base.base.BaseActivity;
import testcomponent.heyongrui.com.base.network.configure.ResponseDisposable;
import testcomponent.heyongrui.com.base.widget.itemdecoration.Divider;
import testcomponent.heyongrui.com.base.widget.itemdecoration.DividerBuilder;
import testcomponent.heyongrui.com.base.widget.itemdecoration.RecycleViewItemDecoration;
import testcomponent.heyongrui.com.componenta.R;
import testcomponent.heyongrui.com.componenta.net.dto.UnsplashPicDto;
import testcomponent.heyongrui.com.componenta.net.service.QingMangService;
import testcomponent.heyongrui.com.componenta.net.service.UnsplashService;
import testcomponent.heyongrui.com.componenta.ui.unsplash.adapter.UnsplashAdapter;
import testcomponent.heyongrui.com.componenta.ui.unsplash.adapter.UnsplashMultipleItem;

/**
 * Created by lambert on 2018/10/24.
 */

public class UnsplashActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private TextView tv;

    @Inject
    UnsplashService unsplashService;

    @Inject
    QingMangService qingMangService;

    private int page = 1;
    private int per_page = 20;
    private UnsplashAdapter unsplashAdapter;

    public static void launchActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UnsplashActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_unsplash;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        recyclerView = findViewById(R.id.recyclerView);
        tv = findViewById(R.id.tv);
        initRecyclerView();
        getQingMang();
        getUnsplashPics();
    }

    private void initRecyclerView() {
        List<UnsplashMultipleItem> data = new ArrayList<>();
        unsplashAdapter = new UnsplashAdapter(data);
        unsplashAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        unsplashAdapter.bindToRecyclerView(recyclerView);
        int color = ContextCompat.getColor(this, R.color.white);
        int gap = 10;
        recyclerView.addItemDecoration(new RecycleViewItemDecoration(this) {
            @Override
            public Divider getDivider(int itemPosition) {
                DividerBuilder dividerBuilder = new DividerBuilder();
                if (itemPosition % 2 == 0) {
                    dividerBuilder.setLeftSideLine(true, color, gap, 0, 0);
                    dividerBuilder.setRightSideLine(true, color, gap / 2, 0, 0);
                } else {
                    dividerBuilder.setLeftSideLine(true, color, gap / 2, 0, 0);
                    dividerBuilder.setRightSideLine(true, color, gap, 0, 0);
                }
                dividerBuilder.setTopSideLine(true, color, gap / 2, 0, 0);
                dividerBuilder.setBottomSideLine(true, color, gap / 2, 0, 0);
                Divider divider = dividerBuilder.create();
                return divider;
            }
        });
        unsplashAdapter.setOnItemClickListener((adapter, view, position) -> {
            UnsplashMultipleItem multipleItem = (UnsplashMultipleItem) adapter.getData().get(position);
            UnsplashPicDto unsplashPicDto = multipleItem.getUnsplashPicDto();
            ImageView iv = view.findViewById(R.id.iv);
            int[] screenLocationS = new int[2];
            iv.getLocationOnScreen(screenLocationS);
            UnsplashDetailActivity.launchActivity(UnsplashActivity.this, unsplashPicDto,
                    screenLocationS[1], screenLocationS[0], iv.getWidth(), iv.getHeight());
        });
    }

    private void getQingMang() {
        mRxManager.add(new QingMangService().getQingMang("2.0.3", "0214084828574de8b60c1197f30b2e34d38295fb", 94)
                .subscribeWith(new ResponseDisposable<ResponseBody>(this) {
                    @Override
                    protected void onSuccess(ResponseBody responseBody) {
                        try {
                            String xmlContent = responseBody.string();
                            Log.d("getQingMang", xmlContent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String errorMsg) {

                    }
                }));
    }

    private void getUnsplashPics() {
//        mRxManager.add(new UnsplashService().getRandomPicMap(page, per_page)
//                .subscribeWith(new ResponseDisposable<List<UnsplashPicDto>>(this, true) {
//                    @Override
//                    protected void onSuccess(List<UnsplashPicDto> unsplashPicListDto) {
//                        setData(unsplashPicListDto);
//                    }
//
//                    @Override
//                    protected void onFailure(int errorCode, String errorMsg) {
//                        ToastUtils.showShort(errorMsg);
//                    }
//                }));
        String testJson = "[\n" +
                "    {\n" +
                "        \"id\":\"ftffrOijzJw\",\n" +
                "        \"created_at\":\"2018-10-22T16:33:07-04:00\",\n" +
                "        \"updated_at\":\"2018-10-23T11:51:46-04:00\",\n" +
                "        \"width\":5304,\n" +
                "        \"height\":7952,\n" +
                "        \"color\":\"#ECEFEE\",\n" +
                "        \"description\":null,\n" +
                "        \"urls\":{\n" +
                "            \"raw\":\"https://images.unsplash.com/photo-1540239937192-33211ff3eb67?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=c30fab5324091149c08cea9948c6049c\",\n" +
                "            \"full\":\"https://images.unsplash.com/photo-1540239937192-33211ff3eb67?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=a32b06758c4a6ed28974cc7a01c6fa3b\",\n" +
                "            \"regular\":\"https://images.unsplash.com/photo-1540239937192-33211ff3eb67?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=c6e326f5a886c6664348e65eee58f153\",\n" +
                "            \"small\":\"https://images.unsplash.com/photo-1540239937192-33211ff3eb67?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=2471f15d86fc587d5a206451d9350b25\",\n" +
                "            \"thumb\":\"https://images.unsplash.com/photo-1540239937192-33211ff3eb67?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=e9012ba997de993daa80cacf2604f009\"\n" +
                "        },\n" +
                "        \"links\":{\n" +
                "            \"self\":\"https://api.unsplash.com/photos/ftffrOijzJw\",\n" +
                "            \"html\":\"https://unsplash.com/photos/ftffrOijzJw\",\n" +
                "            \"download\":\"https://unsplash.com/photos/ftffrOijzJw/download\",\n" +
                "            \"download_location\":\"https://api.unsplash.com/photos/ftffrOijzJw/download\"\n" +
                "        },\n" +
                "        \"categories\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"sponsored\":false,\n" +
                "        \"sponsored_by\":null,\n" +
                "        \"sponsored_impressions_id\":null,\n" +
                "        \"likes\":206,\n" +
                "        \"liked_by_user\":false,\n" +
                "        \"current_user_collections\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"slug\":null,\n" +
                "        \"user\":{\n" +
                "            \"id\":\"fbPZwdKgWWs\",\n" +
                "            \"updated_at\":\"2018-10-23T22:04:04-04:00\",\n" +
                "            \"username\":\"jeremybishop\",\n" +
                "            \"name\":\"Jeremy Bishop\",\n" +
                "            \"first_name\":\"Jeremy\",\n" +
                "            \"last_name\":\"Bishop\",\n" +
                "            \"twitter_username\":null,\n" +
                "            \"portfolio_url\":\"http://jeremybishopphotography.com\",\n" +
                "            \"bio\":\"Lifestyle, Landscape, Waterman |\n" +
                "Creating to Inspire through each frame \n" +
                "\",\n" +
                "            \"location\":\"California\",\n" +
                "            \"links\":{\n" +
                "                \"self\":\"https://api.unsplash.com/users/jeremybishop\",\n" +
                "                \"html\":\"https://unsplash.com/@jeremybishop\",\n" +
                "                \"photos\":\"https://api.unsplash.com/users/jeremybishop/photos\",\n" +
                "                \"likes\":\"https://api.unsplash.com/users/jeremybishop/likes\",\n" +
                "                \"portfolio\":\"https://api.unsplash.com/users/jeremybishop/portfolio\",\n" +
                "                \"following\":\"https://api.unsplash.com/users/jeremybishop/following\",\n" +
                "                \"followers\":\"https://api.unsplash.com/users/jeremybishop/followers\"\n" +
                "            },\n" +
                "            \"profile_image\":{\n" +
                "                \"small\":\"https://images.unsplash.com/profile-1475405901109-04b2f633a548?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=8bb5a4dcbbe82648e3c78eb143435886\",\n" +
                "                \"medium\":\"https://images.unsplash.com/profile-1475405901109-04b2f633a548?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=164eaa85bd58a3e71871d0387246310c\",\n" +
                "                \"large\":\"https://images.unsplash.com/profile-1475405901109-04b2f633a548?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=07596ffc8a1763674708a2d8500f2427\"\n" +
                "            },\n" +
                "            \"instagram_username\":\"bluumind\",\n" +
                "            \"total_collections\":15,\n" +
                "            \"total_likes\":3788,\n" +
                "            \"total_photos\":751,\n" +
                "            \"accepted_tos\":false\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"r5MzeN6fYbc\",\n" +
                "        \"created_at\":\"2018-10-22T15:26:25-04:00\",\n" +
                "        \"updated_at\":\"2018-10-23T11:51:44-04:00\",\n" +
                "        \"width\":3008,\n" +
                "        \"height\":4512,\n" +
                "        \"color\":\"#F4F3F1\",\n" +
                "        \"description\":null,\n" +
                "        \"urls\":{\n" +
                "            \"raw\":\"https://images.unsplash.com/photo-1540236261390-89c218511f2b?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=a02ac791df9228ed8b5e5e61aba4f9d1\",\n" +
                "            \"full\":\"https://images.unsplash.com/photo-1540236261390-89c218511f2b?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=835616a9a6e6945837824c5fd498de3c\",\n" +
                "            \"regular\":\"https://images.unsplash.com/photo-1540236261390-89c218511f2b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=55404f2cc9fc55f2bc5fd7a32f433303\",\n" +
                "            \"small\":\"https://images.unsplash.com/photo-1540236261390-89c218511f2b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=a8a4a2be7057aa601d253443d3c2ce50\",\n" +
                "            \"thumb\":\"https://images.unsplash.com/photo-1540236261390-89c218511f2b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=26158154d773147dbdd158f62edd4e10\"\n" +
                "        },\n" +
                "        \"links\":{\n" +
                "            \"self\":\"https://api.unsplash.com/photos/r5MzeN6fYbc\",\n" +
                "            \"html\":\"https://unsplash.com/photos/r5MzeN6fYbc\",\n" +
                "            \"download\":\"https://unsplash.com/photos/r5MzeN6fYbc/download\",\n" +
                "            \"download_location\":\"https://api.unsplash.com/photos/r5MzeN6fYbc/download\"\n" +
                "        },\n" +
                "        \"categories\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"sponsored\":false,\n" +
                "        \"sponsored_by\":null,\n" +
                "        \"sponsored_impressions_id\":null,\n" +
                "        \"likes\":156,\n" +
                "        \"liked_by_user\":false,\n" +
                "        \"current_user_collections\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"slug\":null,\n" +
                "        \"user\":{\n" +
                "            \"id\":\"R-yMK9J038A\",\n" +
                "            \"updated_at\":\"2018-10-23T22:04:06-04:00\",\n" +
                "            \"username\":\"bogdipasca\",\n" +
                "            \"name\":\"Bogdan Pasca\",\n" +
                "            \"first_name\":\"Bogdan\",\n" +
                "            \"last_name\":\"Pasca\",\n" +
                "            \"twitter_username\":null,\n" +
                "            \"portfolio_url\":null,\n" +
                "            \"bio\":null,\n" +
                "            \"location\":null,\n" +
                "            \"links\":{\n" +
                "                \"self\":\"https://api.unsplash.com/users/bogdipasca\",\n" +
                "                \"html\":\"https://unsplash.com/@bogdipasca\",\n" +
                "                \"photos\":\"https://api.unsplash.com/users/bogdipasca/photos\",\n" +
                "                \"likes\":\"https://api.unsplash.com/users/bogdipasca/likes\",\n" +
                "                \"portfolio\":\"https://api.unsplash.com/users/bogdipasca/portfolio\",\n" +
                "                \"following\":\"https://api.unsplash.com/users/bogdipasca/following\",\n" +
                "                \"followers\":\"https://api.unsplash.com/users/bogdipasca/followers\"\n" +
                "            },\n" +
                "            \"profile_image\":{\n" +
                "                \"small\":\"https://images.unsplash.com/profile-fb-1539582911-1a3136009ab0.jpg?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=42ac0f5ad146d68c4d055557b21ea875\",\n" +
                "                \"medium\":\"https://images.unsplash.com/profile-fb-1539582911-1a3136009ab0.jpg?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=1e6a527d38ddac1e6786e28ec1733f7d\",\n" +
                "                \"large\":\"https://images.unsplash.com/profile-fb-1539582911-1a3136009ab0.jpg?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=29a9ec4fcc07d7950168b39643c30a9e\"\n" +
                "            },\n" +
                "            \"instagram_username\":null,\n" +
                "            \"total_collections\":0,\n" +
                "            \"total_likes\":15,\n" +
                "            \"total_photos\":16,\n" +
                "            \"accepted_tos\":false\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"1E13HdP73dg\",\n" +
                "        \"created_at\":\"2018-10-22T14:46:45-04:00\",\n" +
                "        \"updated_at\":\"2018-10-23T11:51:43-04:00\",\n" +
                "        \"width\":3967,\n" +
                "        \"height\":5950,\n" +
                "        \"color\":\"#F4ECDE\",\n" +
                "        \"description\":null,\n" +
                "        \"urls\":{\n" +
                "            \"raw\":\"https://images.unsplash.com/photo-1540233797181-c10cbfa4ebda?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=4d84774bcd5471e05eb3be4396e3d013\",\n" +
                "            \"full\":\"https://images.unsplash.com/photo-1540233797181-c10cbfa4ebda?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=35a8cfcc74e7091854d03ada6c05850d\",\n" +
                "            \"regular\":\"https://images.unsplash.com/photo-1540233797181-c10cbfa4ebda?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=0764caccdfbc926fbdf472b25e31a4c6\",\n" +
                "            \"small\":\"https://images.unsplash.com/photo-1540233797181-c10cbfa4ebda?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=f6e4eee2f4901fc7c46bed46cd0f1391\",\n" +
                "            \"thumb\":\"https://images.unsplash.com/photo-1540233797181-c10cbfa4ebda?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=133762a88adbdb1945b0161f7e55ae13\"\n" +
                "        },\n" +
                "        \"links\":{\n" +
                "            \"self\":\"https://api.unsplash.com/photos/1E13HdP73dg\",\n" +
                "            \"html\":\"https://unsplash.com/photos/1E13HdP73dg\",\n" +
                "            \"download\":\"https://unsplash.com/photos/1E13HdP73dg/download\",\n" +
                "            \"download_location\":\"https://api.unsplash.com/photos/1E13HdP73dg/download\"\n" +
                "        },\n" +
                "        \"categories\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"sponsored\":false,\n" +
                "        \"sponsored_by\":null,\n" +
                "        \"sponsored_impressions_id\":null,\n" +
                "        \"likes\":129,\n" +
                "        \"liked_by_user\":false,\n" +
                "        \"current_user_collections\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"slug\":null,\n" +
                "        \"user\":{\n" +
                "            \"id\":\"tekQWDHxcog\",\n" +
                "            \"updated_at\":\"2018-10-24T00:10:33-04:00\",\n" +
                "            \"username\":\"ryejessen\",\n" +
                "            \"name\":\"Rye Jessen\",\n" +
                "            \"first_name\":\"Rye\",\n" +
                "            \"last_name\":\"Jessen\",\n" +
                "            \"twitter_username\":\"ryejessen\",\n" +
                "            \"portfolio_url\":\"http://ryejessen.com\",\n" +
                "            \"bio\":\"Photographer based in Vancouver, BC\",\n" +
                "            \"location\":\"Vancouver, BC\",\n" +
                "            \"links\":{\n" +
                "                \"self\":\"https://api.unsplash.com/users/ryejessen\",\n" +
                "                \"html\":\"https://unsplash.com/@ryejessen\",\n" +
                "                \"photos\":\"https://api.unsplash.com/users/ryejessen/photos\",\n" +
                "                \"likes\":\"https://api.unsplash.com/users/ryejessen/likes\",\n" +
                "                \"portfolio\":\"https://api.unsplash.com/users/ryejessen/portfolio\",\n" +
                "                \"following\":\"https://api.unsplash.com/users/ryejessen/following\",\n" +
                "                \"followers\":\"https://api.unsplash.com/users/ryejessen/followers\"\n" +
                "            },\n" +
                "            \"profile_image\":{\n" +
                "                \"small\":\"https://images.unsplash.com/profile-1527892209335-f555b238462e?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=2b918049f9e5efba41c88c36cfb8f41c\",\n" +
                "                \"medium\":\"https://images.unsplash.com/profile-1527892209335-f555b238462e?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=e71d1974d4a4839b62972d8e0192a05a\",\n" +
                "                \"large\":\"https://images.unsplash.com/profile-1527892209335-f555b238462e?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=637949353c4b9276834a634f7bbcb1bc\"\n" +
                "            },\n" +
                "            \"instagram_username\":\"ryejessen\",\n" +
                "            \"total_collections\":3,\n" +
                "            \"total_likes\":216,\n" +
                "            \"total_photos\":54,\n" +
                "            \"accepted_tos\":false\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"ePK_wgkE968\",\n" +
                "        \"created_at\":\"2018-10-22T12:10:44-04:00\",\n" +
                "        \"updated_at\":\"2018-10-23T11:51:26-04:00\",\n" +
                "        \"width\":3384,\n" +
                "        \"height\":6016,\n" +
                "        \"color\":\"#DEC3A2\",\n" +
                "        \"description\":null,\n" +
                "        \"urls\":{\n" +
                "            \"raw\":\"https://images.unsplash.com/photo-1540224542762-172d4ce88192?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=05c765c3e511e5780223e7d5d893f84c\",\n" +
                "            \"full\":\"https://images.unsplash.com/photo-1540224542762-172d4ce88192?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=80262246c81b788a6264cf6b2afd188f\",\n" +
                "            \"regular\":\"https://images.unsplash.com/photo-1540224542762-172d4ce88192?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=c01d2956fc7c24998c0f7f858e04437a\",\n" +
                "            \"small\":\"https://images.unsplash.com/photo-1540224542762-172d4ce88192?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=b7a5f945fd67fe828ab4fe89ebefbab2\",\n" +
                "            \"thumb\":\"https://images.unsplash.com/photo-1540224542762-172d4ce88192?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=da14705f6129929b8e468f80a3053139\"\n" +
                "        },\n" +
                "        \"links\":{\n" +
                "            \"self\":\"https://api.unsplash.com/photos/ePK_wgkE968\",\n" +
                "            \"html\":\"https://unsplash.com/photos/ePK_wgkE968\",\n" +
                "            \"download\":\"https://unsplash.com/photos/ePK_wgkE968/download\",\n" +
                "            \"download_location\":\"https://api.unsplash.com/photos/ePK_wgkE968/download\"\n" +
                "        },\n" +
                "        \"categories\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"sponsored\":false,\n" +
                "        \"sponsored_by\":null,\n" +
                "        \"sponsored_impressions_id\":null,\n" +
                "        \"likes\":60,\n" +
                "        \"liked_by_user\":false,\n" +
                "        \"current_user_collections\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"slug\":null,\n" +
                "        \"user\":{\n" +
                "            \"id\":\"z5O37uH-zgI\",\n" +
                "            \"updated_at\":\"2018-10-23T23:17:10-04:00\",\n" +
                "            \"username\":\"petibalt\",\n" +
                "            \"name\":\"Thibault Penin\",\n" +
                "            \"first_name\":\"Thibault\",\n" +
                "            \"last_name\":\"Penin\",\n" +
                "            \"twitter_username\":\"ThibaultPenin\",\n" +
                "            \"portfolio_url\":\"https://www.thibaultpenin.com/\",\n" +
                "            \"bio\":\"21 year-old self taught french photographer and filmmaker\",\n" +
                "            \"location\":\"Biarritz, France\",\n" +
                "            \"links\":{\n" +
                "                \"self\":\"https://api.unsplash.com/users/petibalt\",\n" +
                "                \"html\":\"https://unsplash.com/@petibalt\",\n" +
                "                \"photos\":\"https://api.unsplash.com/users/petibalt/photos\",\n" +
                "                \"likes\":\"https://api.unsplash.com/users/petibalt/likes\",\n" +
                "                \"portfolio\":\"https://api.unsplash.com/users/petibalt/portfolio\",\n" +
                "                \"following\":\"https://api.unsplash.com/users/petibalt/following\",\n" +
                "                \"followers\":\"https://api.unsplash.com/users/petibalt/followers\"\n" +
                "            },\n" +
                "            \"profile_image\":{\n" +
                "                \"small\":\"https://images.unsplash.com/profile-1537527170077-254baf23ce49?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=d3337d40ec3c81b87fda851394c3cdbd\",\n" +
                "                \"medium\":\"https://images.unsplash.com/profile-1537527170077-254baf23ce49?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=48dbe313378d927cf82a7c8a65377501\",\n" +
                "                \"large\":\"https://images.unsplash.com/profile-1537527170077-254baf23ce49?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=f279c5c7d074a2809705524bc1f2a470\"\n" +
                "            },\n" +
                "            \"instagram_username\":\"petibalt\",\n" +
                "            \"total_collections\":0,\n" +
                "            \"total_likes\":4,\n" +
                "            \"total_photos\":18,\n" +
                "            \"accepted_tos\":false\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"c7x5waQk498\",\n" +
                "        \"created_at\":\"2018-10-23T10:09:52-04:00\",\n" +
                "        \"updated_at\":\"2018-10-23T11:50:32-04:00\",\n" +
                "        \"width\":4480,\n" +
                "        \"height\":6720,\n" +
                "        \"color\":\"#070201\",\n" +
                "        \"description\":null,\n" +
                "        \"urls\":{\n" +
                "            \"raw\":\"https://images.unsplash.com/photo-1540303518795-54782434b247?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=6e5049ce32d491eb399f090e2154d097\",\n" +
                "            \"full\":\"https://images.unsplash.com/photo-1540303518795-54782434b247?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=d81db2ccdba847606aa33f7eb42cdfcb\",\n" +
                "            \"regular\":\"https://images.unsplash.com/photo-1540303518795-54782434b247?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=3476d17fd9a6a59534f6a069690611d2\",\n" +
                "            \"small\":\"https://images.unsplash.com/photo-1540303518795-54782434b247?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=7229853df0ee5232132ddfa92979f4a0\",\n" +
                "            \"thumb\":\"https://images.unsplash.com/photo-1540303518795-54782434b247?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=7010b50bdb722f65a734ec6d08c18b10\"\n" +
                "        },\n" +
                "        \"links\":{\n" +
                "            \"self\":\"https://api.unsplash.com/photos/c7x5waQk498\",\n" +
                "            \"html\":\"https://unsplash.com/photos/c7x5waQk498\",\n" +
                "            \"download\":\"https://unsplash.com/photos/c7x5waQk498/download\",\n" +
                "            \"download_location\":\"https://api.unsplash.com/photos/c7x5waQk498/download\"\n" +
                "        },\n" +
                "        \"categories\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"sponsored\":false,\n" +
                "        \"sponsored_by\":null,\n" +
                "        \"sponsored_impressions_id\":null,\n" +
                "        \"likes\":59,\n" +
                "        \"liked_by_user\":false,\n" +
                "        \"current_user_collections\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"slug\":null,\n" +
                "        \"user\":{\n" +
                "            \"id\":\"dG6lZyj-wvM\",\n" +
                "            \"updated_at\":\"2018-10-24T02:51:07-04:00\",\n" +
                "            \"username\":\"nate_dumlao\",\n" +
                "            \"name\":\"Nathan Dumlao\",\n" +
                "            \"first_name\":\"Nathan\",\n" +
                "            \"last_name\":\"Dumlao\",\n" +
                "            \"twitter_username\":\"Nate_Dumlao\",\n" +
                "            \"portfolio_url\":\"http://www.nathandumlaophotos.com\",\n" +
                "            \"bio\":\"Brand Consultant and Content Creator living in Los Angeles creating up the coast.\",\n" +
                "            \"location\":\"Los Angeles\",\n" +
                "            \"links\":{\n" +
                "                \"self\":\"https://api.unsplash.com/users/nate_dumlao\",\n" +
                "                \"html\":\"https://unsplash.com/@nate_dumlao\",\n" +
                "                \"photos\":\"https://api.unsplash.com/users/nate_dumlao/photos\",\n" +
                "                \"likes\":\"https://api.unsplash.com/users/nate_dumlao/likes\",\n" +
                "                \"portfolio\":\"https://api.unsplash.com/users/nate_dumlao/portfolio\",\n" +
                "                \"following\":\"https://api.unsplash.com/users/nate_dumlao/following\",\n" +
                "                \"followers\":\"https://api.unsplash.com/users/nate_dumlao/followers\"\n" +
                "            },\n" +
                "            \"profile_image\":{\n" +
                "                \"small\":\"https://images.unsplash.com/profile-1495427732560-fe5248ad6638?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=8e6405920894a45ce9204dd11d1465f3\",\n" +
                "                \"medium\":\"https://images.unsplash.com/profile-1495427732560-fe5248ad6638?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=1978e1f9440ee4cc1da03c318068593f\",\n" +
                "                \"large\":\"https://images.unsplash.com/profile-1495427732560-fe5248ad6638?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=8f75defe3b90fd37243d80d207c8c6d6\"\n" +
                "            },\n" +
                "            \"instagram_username\":\"nate_dumlao\",\n" +
                "            \"total_collections\":2,\n" +
                "            \"total_likes\":1304,\n" +
                "            \"total_photos\":777,\n" +
                "            \"accepted_tos\":false\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"6-IWz3nSlrY\",\n" +
                "        \"created_at\":\"2018-10-23T08:26:56-04:00\",\n" +
                "        \"updated_at\":\"2018-10-23T11:50:18-04:00\",\n" +
                "        \"width\":4480,\n" +
                "        \"height\":6720,\n" +
                "        \"color\":\"#121311\",\n" +
                "        \"description\":null,\n" +
                "        \"urls\":{\n" +
                "            \"raw\":\"https://images.unsplash.com/photo-1540297388228-8b2c64460a2b?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=88ffa7f9213554d3a9fbd0f63dcc151f\",\n" +
                "            \"full\":\"https://images.unsplash.com/photo-1540297388228-8b2c64460a2b?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=304d204069bec3515c1936ed7e49a18c\",\n" +
                "            \"regular\":\"https://images.unsplash.com/photo-1540297388228-8b2c64460a2b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=a799f19bd9fa1eddea51e3e81030bd31\",\n" +
                "            \"small\":\"https://images.unsplash.com/photo-1540297388228-8b2c64460a2b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=b1b9baebf57acb8de91cc13f6eea5938\",\n" +
                "            \"thumb\":\"https://images.unsplash.com/photo-1540297388228-8b2c64460a2b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=9839b06aea1c712cc384c03283f02d40\"\n" +
                "        },\n" +
                "        \"links\":{\n" +
                "            \"self\":\"https://api.unsplash.com/photos/6-IWz3nSlrY\",\n" +
                "            \"html\":\"https://unsplash.com/photos/6-IWz3nSlrY\",\n" +
                "            \"download\":\"https://unsplash.com/photos/6-IWz3nSlrY/download\",\n" +
                "            \"download_location\":\"https://api.unsplash.com/photos/6-IWz3nSlrY/download\"\n" +
                "        },\n" +
                "        \"categories\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"sponsored\":false,\n" +
                "        \"sponsored_by\":null,\n" +
                "        \"sponsored_impressions_id\":null,\n" +
                "        \"likes\":38,\n" +
                "        \"liked_by_user\":false,\n" +
                "        \"current_user_collections\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"slug\":null,\n" +
                "        \"user\":{\n" +
                "            \"id\":\"dG6lZyj-wvM\",\n" +
                "            \"updated_at\":\"2018-10-24T02:51:07-04:00\",\n" +
                "            \"username\":\"nate_dumlao\",\n" +
                "            \"name\":\"Nathan Dumlao\",\n" +
                "            \"first_name\":\"Nathan\",\n" +
                "            \"last_name\":\"Dumlao\",\n" +
                "            \"twitter_username\":\"Nate_Dumlao\",\n" +
                "            \"portfolio_url\":\"http://www.nathandumlaophotos.com\",\n" +
                "            \"bio\":\"Brand Consultant and Content Creator living in Los Angeles creating up the coast.\",\n" +
                "            \"location\":\"Los Angeles\",\n" +
                "            \"links\":{\n" +
                "                \"self\":\"https://api.unsplash.com/users/nate_dumlao\",\n" +
                "                \"html\":\"https://unsplash.com/@nate_dumlao\",\n" +
                "                \"photos\":\"https://api.unsplash.com/users/nate_dumlao/photos\",\n" +
                "                \"likes\":\"https://api.unsplash.com/users/nate_dumlao/likes\",\n" +
                "                \"portfolio\":\"https://api.unsplash.com/users/nate_dumlao/portfolio\",\n" +
                "                \"following\":\"https://api.unsplash.com/users/nate_dumlao/following\",\n" +
                "                \"followers\":\"https://api.unsplash.com/users/nate_dumlao/followers\"\n" +
                "            },\n" +
                "            \"profile_image\":{\n" +
                "                \"small\":\"https://images.unsplash.com/profile-1495427732560-fe5248ad6638?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=8e6405920894a45ce9204dd11d1465f3\",\n" +
                "                \"medium\":\"https://images.unsplash.com/profile-1495427732560-fe5248ad6638?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=1978e1f9440ee4cc1da03c318068593f\",\n" +
                "                \"large\":\"https://images.unsplash.com/profile-1495427732560-fe5248ad6638?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=8f75defe3b90fd37243d80d207c8c6d6\"\n" +
                "            },\n" +
                "            \"instagram_username\":\"nate_dumlao\",\n" +
                "            \"total_collections\":2,\n" +
                "            \"total_likes\":1304,\n" +
                "            \"total_photos\":777,\n" +
                "            \"accepted_tos\":false\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"I1RvOBuiOyQ\",\n" +
                "        \"created_at\":\"2018-10-23T07:23:25-04:00\",\n" +
                "        \"updated_at\":\"2018-10-23T11:50:17-04:00\",\n" +
                "        \"width\":3744,\n" +
                "        \"height\":5616,\n" +
                "        \"color\":\"#0A1111\",\n" +
                "        \"description\":null,\n" +
                "        \"urls\":{\n" +
                "            \"raw\":\"https://images.unsplash.com/photo-1540293547891-82874a25a87a?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=da0e27308a7420597813e644f1739330\",\n" +
                "            \"full\":\"https://images.unsplash.com/photo-1540293547891-82874a25a87a?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=26fab8fa87cf909b17f43312236ea4b5\",\n" +
                "            \"regular\":\"https://images.unsplash.com/photo-1540293547891-82874a25a87a?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=bfac63e2d0f4c4a5ac1716629d4596d1\",\n" +
                "            \"small\":\"https://images.unsplash.com/photo-1540293547891-82874a25a87a?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=f49d6e020446a6627ada59ddb7809fd3\",\n" +
                "            \"thumb\":\"https://images.unsplash.com/photo-1540293547891-82874a25a87a?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=19d0206a5c2faaa8aeffd3058e2e2eee\"\n" +
                "        },\n" +
                "        \"links\":{\n" +
                "            \"self\":\"https://api.unsplash.com/photos/I1RvOBuiOyQ\",\n" +
                "            \"html\":\"https://unsplash.com/photos/I1RvOBuiOyQ\",\n" +
                "            \"download\":\"https://unsplash.com/photos/I1RvOBuiOyQ/download\",\n" +
                "            \"download_location\":\"https://api.unsplash.com/photos/I1RvOBuiOyQ/download\"\n" +
                "        },\n" +
                "        \"categories\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"sponsored\":false,\n" +
                "        \"sponsored_by\":null,\n" +
                "        \"sponsored_impressions_id\":null,\n" +
                "        \"likes\":46,\n" +
                "        \"liked_by_user\":false,\n" +
                "        \"current_user_collections\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"slug\":null,\n" +
                "        \"user\":{\n" +
                "            \"id\":\"fgd55XJhy14\",\n" +
                "            \"updated_at\":\"2018-10-23T16:36:33-04:00\",\n" +
                "            \"username\":\"victoriakosmo\",\n" +
                "            \"name\":\"Victoria Shes\",\n" +
                "            \"first_name\":\"Victoria\",\n" +
                "            \"last_name\":\"Shes\",\n" +
                "            \"twitter_username\":null,\n" +
                "            \"portfolio_url\":null,\n" +
                "            \"bio\":\"24 y. o. graphic designer and photographer from Russia\",\n" +
                "            \"location\":\"Armavir, Russian Federation\",\n" +
                "            \"links\":{\n" +
                "                \"self\":\"https://api.unsplash.com/users/victoriakosmo\",\n" +
                "                \"html\":\"https://unsplash.com/@victoriakosmo\",\n" +
                "                \"photos\":\"https://api.unsplash.com/users/victoriakosmo/photos\",\n" +
                "                \"likes\":\"https://api.unsplash.com/users/victoriakosmo/likes\",\n" +
                "                \"portfolio\":\"https://api.unsplash.com/users/victoriakosmo/portfolio\",\n" +
                "                \"following\":\"https://api.unsplash.com/users/victoriakosmo/following\",\n" +
                "                \"followers\":\"https://api.unsplash.com/users/victoriakosmo/followers\"\n" +
                "            },\n" +
                "            \"profile_image\":{\n" +
                "                \"small\":\"https://images.unsplash.com/profile-1536748652262-d97761f5d0c2?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=3a71dfa9894127726a43e620eaf634e6\",\n" +
                "                \"medium\":\"https://images.unsplash.com/profile-1536748652262-d97761f5d0c2?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=1df9faed4ebff7bcf8dd5e3a779cd650\",\n" +
                "                \"large\":\"https://images.unsplash.com/profile-1536748652262-d97761f5d0c2?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=ca91f283131ed48ce2c7aefde34731b0\"\n" +
                "            },\n" +
                "            \"instagram_username\":\"victoriakosmo\",\n" +
                "            \"total_collections\":0,\n" +
                "            \"total_likes\":151,\n" +
                "            \"total_photos\":15,\n" +
                "            \"accepted_tos\":false\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"CiebRcG9Ee8\",\n" +
                "        \"created_at\":\"2018-10-23T09:33:12-04:00\",\n" +
                "        \"updated_at\":\"2018-10-23T11:50:13-04:00\",\n" +
                "        \"width\":2666,\n" +
                "        \"height\":4000,\n" +
                "        \"color\":\"#000E14\",\n" +
                "        \"description\":null,\n" +
                "        \"urls\":{\n" +
                "            \"raw\":\"https://images.unsplash.com/photo-1540301499174-5062d7e66ce8?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=5aecd1c2c43c09c2d1cb9c1ff41dec0e\",\n" +
                "            \"full\":\"https://images.unsplash.com/photo-1540301499174-5062d7e66ce8?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=9e18d80e5eb6a19dddc25d8f2659d2d3\",\n" +
                "            \"regular\":\"https://images.unsplash.com/photo-1540301499174-5062d7e66ce8?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=8ccfca61b27be8ea4b1aed56fe61fdbb\",\n" +
                "            \"small\":\"https://images.unsplash.com/photo-1540301499174-5062d7e66ce8?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=c6b4c5c114fa89af79fc76e7aee2d7c3\",\n" +
                "            \"thumb\":\"https://images.unsplash.com/photo-1540301499174-5062d7e66ce8?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=f69fbf7bcdf096eb464dc55deb7597c1\"\n" +
                "        },\n" +
                "        \"links\":{\n" +
                "            \"self\":\"https://api.unsplash.com/photos/CiebRcG9Ee8\",\n" +
                "            \"html\":\"https://unsplash.com/photos/CiebRcG9Ee8\",\n" +
                "            \"download\":\"https://unsplash.com/photos/CiebRcG9Ee8/download\",\n" +
                "            \"download_location\":\"https://api.unsplash.com/photos/CiebRcG9Ee8/download\"\n" +
                "        },\n" +
                "        \"categories\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"sponsored\":false,\n" +
                "        \"sponsored_by\":null,\n" +
                "        \"sponsored_impressions_id\":null,\n" +
                "        \"likes\":77,\n" +
                "        \"liked_by_user\":false,\n" +
                "        \"current_user_collections\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"slug\":null,\n" +
                "        \"user\":{\n" +
                "            \"id\":\"XZDJrfKzdWY\",\n" +
                "            \"updated_at\":\"2018-10-24T00:03:50-04:00\",\n" +
                "            \"username\":\"eberhardgross\",\n" +
                "            \"name\":\"eberhard grossgasteiger\",\n" +
                "            \"first_name\":\"eberhard\",\n" +
                "            \"last_name\":\"grossgasteiger\",\n" +
                "            \"twitter_username\":\"eberhardgross\",\n" +
                "            \"portfolio_url\":\"http://instagram.com/eberhard_grossgasteiger\",\n" +
                "            \"bio\":\"++++ Simplicity is the ultimate sophistication, nothing is more challenging! ++++ \n" +
                "\n" +
                "https://instagram.com/eberhard_grossgasteiger\n" +
                "\n" +
                "\n" +
                "\n" +
                "\",\n" +
                "            \"location\":\"Ahrntal, South Tyrol, Italy\",\n" +
                "            \"links\":{\n" +
                "                \"self\":\"https://api.unsplash.com/users/eberhardgross\",\n" +
                "                \"html\":\"https://unsplash.com/@eberhardgross\",\n" +
                "                \"photos\":\"https://api.unsplash.com/users/eberhardgross/photos\",\n" +
                "                \"likes\":\"https://api.unsplash.com/users/eberhardgross/likes\",\n" +
                "                \"portfolio\":\"https://api.unsplash.com/users/eberhardgross/portfolio\",\n" +
                "                \"following\":\"https://api.unsplash.com/users/eberhardgross/following\",\n" +
                "                \"followers\":\"https://api.unsplash.com/users/eberhardgross/followers\"\n" +
                "            },\n" +
                "            \"profile_image\":{\n" +
                "                \"small\":\"https://images.unsplash.com/profile-1536052438125-133137ad2359?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=d2d394899e2c0a2cf97007bb6409636f\",\n" +
                "                \"medium\":\"https://images.unsplash.com/profile-1536052438125-133137ad2359?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=921eef99872442a764649865a98a9774\",\n" +
                "                \"large\":\"https://images.unsplash.com/profile-1536052438125-133137ad2359?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=bc192aa550aa2f4f9987b4f167c2a822\"\n" +
                "            },\n" +
                "            \"instagram_username\":\"eberhard_grossgasteiger\",\n" +
                "            \"total_collections\":3,\n" +
                "            \"total_likes\":2291,\n" +
                "            \"total_photos\":618,\n" +
                "            \"accepted_tos\":false\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"kZml8EXoNr0\",\n" +
                "        \"created_at\":\"2018-10-22T20:08:50-04:00\",\n" +
                "        \"updated_at\":\"2018-10-23T11:49:07-04:00\",\n" +
                "        \"width\":2394,\n" +
                "        \"height\":2993,\n" +
                "        \"color\":\"#FFC15A\",\n" +
                "        \"description\":null,\n" +
                "        \"urls\":{\n" +
                "            \"raw\":\"https://images.unsplash.com/photo-1540253208288-6a6c32573092?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=499d350b1655d0a30373742a651683a4\",\n" +
                "            \"full\":\"https://images.unsplash.com/photo-1540253208288-6a6c32573092?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=d9f2357bda7bc184470f7e32a1063270\",\n" +
                "            \"regular\":\"https://images.unsplash.com/photo-1540253208288-6a6c32573092?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=e76f7a5f536ca55159d19dc1aa176cf5\",\n" +
                "            \"small\":\"https://images.unsplash.com/photo-1540253208288-6a6c32573092?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=496e3dd350d462c5070d697d336a4e30\",\n" +
                "            \"thumb\":\"https://images.unsplash.com/photo-1540253208288-6a6c32573092?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=d38172503aa5f218479a71fbc35602b8\"\n" +
                "        },\n" +
                "        \"links\":{\n" +
                "            \"self\":\"https://api.unsplash.com/photos/kZml8EXoNr0\",\n" +
                "            \"html\":\"https://unsplash.com/photos/kZml8EXoNr0\",\n" +
                "            \"download\":\"https://unsplash.com/photos/kZml8EXoNr0/download\",\n" +
                "            \"download_location\":\"https://api.unsplash.com/photos/kZml8EXoNr0/download\"\n" +
                "        },\n" +
                "        \"categories\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"sponsored\":false,\n" +
                "        \"sponsored_by\":null,\n" +
                "        \"sponsored_impressions_id\":null,\n" +
                "        \"likes\":120,\n" +
                "        \"liked_by_user\":false,\n" +
                "        \"current_user_collections\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"slug\":null,\n" +
                "        \"user\":{\n" +
                "            \"id\":\"M1ICDrUoPKg\",\n" +
                "            \"updated_at\":\"2018-10-23T22:43:45-04:00\",\n" +
                "            \"username\":\"benkleaphoto\",\n" +
                "            \"name\":\"Ben Klea\",\n" +
                "            \"first_name\":\"Ben\",\n" +
                "            \"last_name\":\"Klea\",\n" +
                "            \"twitter_username\":null,\n" +
                "            \"portfolio_url\":null,\n" +
                "            \"bio\":\"Husband, father, traveler. Love to see the \uD83C\uDF0E with my fam. \n" +
                "Photography for fun, day job in aviation.\n" +
                "All \uD83D\uDCF7 by me, visuals by the Creator.\n" +
                "\uD83C\uDFE0 is CLE.\n" +
                "Follow me on Instagram @benkleaphoto\",\n" +
                "            \"location\":\"Cleveland, Ohio, USA\",\n" +
                "            \"links\":{\n" +
                "                \"self\":\"https://api.unsplash.com/users/benkleaphoto\",\n" +
                "                \"html\":\"https://unsplash.com/@benkleaphoto\",\n" +
                "                \"photos\":\"https://api.unsplash.com/users/benkleaphoto/photos\",\n" +
                "                \"likes\":\"https://api.unsplash.com/users/benkleaphoto/likes\",\n" +
                "                \"portfolio\":\"https://api.unsplash.com/users/benkleaphoto/portfolio\",\n" +
                "                \"following\":\"https://api.unsplash.com/users/benkleaphoto/following\",\n" +
                "                \"followers\":\"https://api.unsplash.com/users/benkleaphoto/followers\"\n" +
                "            },\n" +
                "            \"profile_image\":{\n" +
                "                \"small\":\"https://images.unsplash.com/profile-1540252301988-030fd9b8036f?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=79c67731a6a44ac0fe3b2aebba4ef81a\",\n" +
                "                \"medium\":\"https://images.unsplash.com/profile-1540252301988-030fd9b8036f?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=292c344b0bca3f2394cac490bfc9578c\",\n" +
                "                \"large\":\"https://images.unsplash.com/profile-1540252301988-030fd9b8036f?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=a130da5571564fa96a28765bd317dd18\"\n" +
                "            },\n" +
                "            \"instagram_username\":\"benkleaphoto\",\n" +
                "            \"total_collections\":0,\n" +
                "            \"total_likes\":0,\n" +
                "            \"total_photos\":5,\n" +
                "            \"accepted_tos\":false\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"BTSCGjJvrLI\",\n" +
                "        \"created_at\":\"2018-10-22T20:12:30-04:00\",\n" +
                "        \"updated_at\":\"2018-10-23T11:49:05-04:00\",\n" +
                "        \"width\":3002,\n" +
                "        \"height\":3752,\n" +
                "        \"color\":\"#FEC677\",\n" +
                "        \"description\":null,\n" +
                "        \"urls\":{\n" +
                "            \"raw\":\"https://images.unsplash.com/photo-1540253333570-41c2227e4452?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=22a26c6099edb03b4da4b0bd8dbf5978\",\n" +
                "            \"full\":\"https://images.unsplash.com/photo-1540253333570-41c2227e4452?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=87d2aa738f445f27b1f914811c915027\",\n" +
                "            \"regular\":\"https://images.unsplash.com/photo-1540253333570-41c2227e4452?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=63fdb2aa03da731690e9154878d80f8e\",\n" +
                "            \"small\":\"https://images.unsplash.com/photo-1540253333570-41c2227e4452?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=de65dd3fca0ccceafdcd106d5d4e99cf\",\n" +
                "            \"thumb\":\"https://images.unsplash.com/photo-1540253333570-41c2227e4452?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjM5OTA2fQ&s=608528d8346fc7f9ffda50577d6f69e3\"\n" +
                "        },\n" +
                "        \"links\":{\n" +
                "            \"self\":\"https://api.unsplash.com/photos/BTSCGjJvrLI\",\n" +
                "            \"html\":\"https://unsplash.com/photos/BTSCGjJvrLI\",\n" +
                "            \"download\":\"https://unsplash.com/photos/BTSCGjJvrLI/download\",\n" +
                "            \"download_location\":\"https://api.unsplash.com/photos/BTSCGjJvrLI/download\"\n" +
                "        },\n" +
                "        \"categories\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"sponsored\":false,\n" +
                "        \"sponsored_by\":null,\n" +
                "        \"sponsored_impressions_id\":null,\n" +
                "        \"likes\":44,\n" +
                "        \"liked_by_user\":false,\n" +
                "        \"current_user_collections\":[\n" +
                "\n" +
                "        ],\n" +
                "        \"slug\":null,\n" +
                "        \"user\":{\n" +
                "            \"id\":\"M1ICDrUoPKg\",\n" +
                "            \"updated_at\":\"2018-10-23T22:43:45-04:00\",\n" +
                "            \"username\":\"benkleaphoto\",\n" +
                "            \"name\":\"Ben Klea\",\n" +
                "            \"first_name\":\"Ben\",\n" +
                "            \"last_name\":\"Klea\",\n" +
                "            \"twitter_username\":null,\n" +
                "            \"portfolio_url\":null,\n" +
                "            \"bio\":\"Husband, father, traveler. Love to see the \uD83C\uDF0E with my fam. \n" +
                "Photography for fun, day job in aviation.\n" +
                "All \uD83D\uDCF7 by me, visuals by the Creator.\n" +
                "\uD83C\uDFE0 is CLE.\n" +
                "Follow me on Instagram @benkleaphoto\",\n" +
                "            \"location\":\"Cleveland, Ohio, USA\",\n" +
                "            \"links\":{\n" +
                "                \"self\":\"https://api.unsplash.com/users/benkleaphoto\",\n" +
                "                \"html\":\"https://unsplash.com/@benkleaphoto\",\n" +
                "                \"photos\":\"https://api.unsplash.com/users/benkleaphoto/photos\",\n" +
                "                \"likes\":\"https://api.unsplash.com/users/benkleaphoto/likes\",\n" +
                "                \"portfolio\":\"https://api.unsplash.com/users/benkleaphoto/portfolio\",\n" +
                "                \"following\":\"https://api.unsplash.com/users/benkleaphoto/following\",\n" +
                "                \"followers\":\"https://api.unsplash.com/users/benkleaphoto/followers\"\n" +
                "            },\n" +
                "            \"profile_image\":{\n" +
                "                \"small\":\"https://images.unsplash.com/profile-1540252301988-030fd9b8036f?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=79c67731a6a44ac0fe3b2aebba4ef81a\",\n" +
                "                \"medium\":\"https://images.unsplash.com/profile-1540252301988-030fd9b8036f?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=292c344b0bca3f2394cac490bfc9578c\",\n" +
                "                \"large\":\"https://images.unsplash.com/profile-1540252301988-030fd9b8036f?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=a130da5571564fa96a28765bd317dd18\"\n" +
                "            },\n" +
                "            \"instagram_username\":\"benkleaphoto\",\n" +
                "            \"total_collections\":0,\n" +
                "            \"total_likes\":0,\n" +
                "            \"total_photos\":5,\n" +
                "            \"accepted_tos\":false\n" +
                "        }\n" +
                "    }\n" +
                "]";
        Gson gson = new Gson();
        List<UnsplashPicDto> unsplashPicListDto = gson.fromJson(testJson, new TypeToken<List<UnsplashPicDto>>() {
        }.getType());
        unsplashPicListDto.addAll(unsplashPicListDto);
        setData(unsplashPicListDto);
    }

    private void setData(List<UnsplashPicDto> unsplashPicListDto) {
        if (unsplashPicListDto == null || unsplashPicListDto.isEmpty()) return;
        for (UnsplashPicDto unsplashPicDto : unsplashPicListDto) {
            unsplashAdapter.addData(new UnsplashMultipleItem(UnsplashMultipleItem.TYPE_ONE, UnsplashMultipleItem.IMG_SPAN_SIZE, unsplashPicDto));
        }
    }
}
