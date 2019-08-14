package com.tj24.library_base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Description:greendao测试实体
 * @Createdtime:2019/3/3 0:15
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
@Entity
public class Cat {
    @Id
    private String id;
    private String name;
    private int age;
    @Generated(hash = 1160128998)
    public Cat(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    @Generated(hash = 205319056)
    public Cat() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    
}
