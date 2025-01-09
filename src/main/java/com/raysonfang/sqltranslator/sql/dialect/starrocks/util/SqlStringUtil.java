package com.raysonfang.sqltranslator.sql.dialect.starrocks.util;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Date: 2025/1/9
 * Author: rockyyin
 * Description: 针对sql字符串转换的处理工具类
 */
public class SqlStringUtil {

    // 这边SR都是用默认的桶数量来处理
    public static String primaryKey = "PRIMARY KEY ({0})\n" +
            "DISTRIBUTED BY HASH ({1})";
    /**
     * 处理createSQL 主要是将sql中的主键和其他索引截取，改造为SR的语法
     * @param createSql
     * @return
     */
    public static ArrayList<String> suffixCreateSqlInterception(String createSql, String specialKey) {
        //将create sql中CONSTRAINT关键字阶截断，第一部分直接返回，第二部分需要拼接SR的主键表的语法
        ArrayList<String> CreateSqlPreAndSuffix = new ArrayList<>();
        if (specialKey == null) {
            return null;
        } else {
            int index = createSql.indexOf(specialKey);
            String intercepted = createSql.substring(0, index);
            int lastIndexOf = intercepted.lastIndexOf(",");
            StringBuilder sb = new StringBuilder(intercepted);
            sb.deleteCharAt(lastIndexOf);
            CreateSqlPreAndSuffix.add(sb.toString() + "\r)\n");
            String suffixStr = createSql.substring(index);
            //先从 PRIMARY key 关键字获取列名称
            int primary_key = suffixStr.indexOf("PRIMARY KEY");
            String PKAndUk = suffixStr.substring(primary_key);
            int leftParenthesis = PKAndUk.indexOf("(");
            int rightParenthesis = PKAndUk.indexOf(")");
            String PkeyColumn = PKAndUk.substring(leftParenthesis+1, rightParenthesis);
            //先从 unique关键字获取列名称
            int unique = suffixStr.indexOf("UNIQUE");
            String ukStr = suffixStr.substring(unique);
            int uniqueKeyLeft = ukStr.indexOf("(");
            int uniqueKeyRight = ukStr.indexOf(")");
            String UKeyColumn = ukStr.substring(uniqueKeyLeft+1, uniqueKeyRight);
            String formatPK = MessageFormat.format(primaryKey, PkeyColumn+","+UKeyColumn, PkeyColumn+","+UKeyColumn);

            CreateSqlPreAndSuffix.add(formatPK);
            return  CreateSqlPreAndSuffix;
        }
    }

    /**
     * 转换 insertAll 的语法 单个insert语句
     * -- 多条插入
     * INSERT ALL
     *     INTO employees (employee_id, first_name, last_name) VALUES (2, 'Jane', 'Smith')
     *     INTO employees (employee_id, first_name, last_name) VALUES (3, 'Bob', 'Johnson')
     * SELECT * FROM dual;
     * 需要转换到单条insert
     * @param insertSql
     * @return
     */
    public static String insertAllToSingleInsert(String insertSql) {

        return "";
    }





}
