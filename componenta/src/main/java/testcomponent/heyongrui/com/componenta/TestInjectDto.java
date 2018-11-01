package testcomponent.heyongrui.com.componenta;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by lambert on 2018/10/22.
 */
//这里配置了Singleton，在ActivityModule也要设置相应的提供者。只设置Inject可直接使用
@Singleton
public class TestInjectDto {
    private String test;
    private int id;

    @Inject
    public TestInjectDto() {

    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
