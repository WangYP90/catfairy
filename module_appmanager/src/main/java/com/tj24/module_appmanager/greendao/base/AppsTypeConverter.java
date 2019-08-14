package com.tj24.module_appmanager.greendao.base;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: 将APP的所属分类 转化为集合
 * @Createdtime:2019/3/21 23:02
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class AppsTypeConverter implements PropertyConverter<List<String>,String> {

    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        else {
            List<String> list = Arrays.asList(databaseValue.split(","));
            return list;
        }
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        if(entityProperty==null){
            return null;
        }
        else{
            StringBuilder sb= new StringBuilder();
            for(String link:entityProperty){
                sb.append(link);
                sb.append(",");
            }
            return sb.toString();
        }
    }
}
