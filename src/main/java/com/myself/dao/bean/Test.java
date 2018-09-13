package com.myself.dao.bean;

import com.myself.dao.anno.Column;
import com.myself.dao.anno.Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test {

    public static void main(String[] args) {

        Filter f1 = new Filter();
        f1.setId(10);

        Filter f2 = new Filter();
        f2.setUsername("小龙");

        Filter f3 = new Filter();
        f3.setEmail("123123123@qq.com,llll@qq.com");

        String q1 = query(f1);
        String q2 = query(f2);
        String q3 = query(f3);


        System.out.println(q1);
        System.out.println(q2);
        System.out.println(q3);

    }


    private static String query(Object filter){

        StringBuilder sb = new StringBuilder();
        //1、获取到class
        Class c = filter.getClass();
        //2、获取table的名字
        boolean isexist = c.isAnnotationPresent(Table.class);
        if (!isexist){
            return null;
        }
        Table table = (Table) c.getAnnotation(Table.class);
        String tableName = table.value();

        sb.append("select * from ").append(tableName).append(" where 1=1");

        //3、便利所有字段
        Field[] fields = c.getDeclaredFields();
        for (Field field:fields){
            //4、处理每个字段对应的sql
            //4.1、拿到字段名
            boolean fExist = field.isAnnotationPresent(Column.class);
            if (!fExist){
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            String columnName = column.value();
            //4.2、拿到字段值
            String fieldName = field.getName();
            String getMethodName = "get"+fieldName.substring(0,1).toUpperCase()+
                    fieldName.substring(1);
            Object fieldValue = null;
            try {
                Method getMehtod = c.getMethod(getMethodName);
                fieldValue = getMehtod.invoke(filter);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            //4.3、拼装sql
            if (fieldValue==null || (fieldValue instanceof Integer && (Integer)fieldValue==0)){
                continue;
            }
            sb.append(" and ").append(fieldName);
            if (fieldValue instanceof String){
                if (((String)fieldValue).contains(",")){
                    String[] values = ((String)fieldValue).split(",");
                    sb.append(" in( ");
                    for (String value:values){
                        sb.append("'").append(value).append("'").append(",");
                    }
                    sb.deleteCharAt(sb.length()-1);
                    sb.append(")");
                }else{
                    sb.append(" = ").append("'").append(fieldValue).append("'");
                }
            }else if (fieldValue instanceof Integer){
                sb.append(" = ").append(fieldValue);
            }

        }

        return sb.toString();
    }

}
