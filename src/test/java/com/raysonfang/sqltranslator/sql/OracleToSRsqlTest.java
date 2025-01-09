package com.raysonfang.sqltranslator.sql;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.raysonfang.sqltranslator.sql.dialect.starrocks.OracleToStarrocksOutputVisitor;
import com.raysonfang.sqltranslator.sql.dialect.starrocks.util.SqlStringUtil;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2025/1/8
 * Author: rockyyin
 * Description:  测试create table 语法测试
 *
 */
public class OracleToSRsqlTest extends TestCase {
    public void  testCreateSQL() {
        String sql1 = "CREATE TABLE my_table (\n" +
                "    ID NUMBER(10),\n" +
                "    clob_column CLOB,\n" +
                "    blob_column BLOB,\n" +
                "    some_other_column VARCHAR2(100),\n" +
                "    creation_date DATE DEFAULT SYSDATE,\n" +
                "    last_modified DATE,\n" +
                "    CONSTRAINT pk_my_table PRIMARY KEY (ID),\n" +
                "    CONSTRAINT uk_my_table UNIQUE (some_other_column)\n" +
                ")";

        String sql2 = "CREATE TABLE my_table (\n" +
                "    ID NUMBER(10),\n" +
                "    clob_column CLOB,\n" +
                "    blob_column BLOB,\n" +
                "    some_other_column VARCHAR2(100),\n" +
                "    creation_date DATE DEFAULT SYSDATE,\n" +
                "    last_modified DATE,\n" +
                "    CONSTRAINT pk_my_table PRIMARY KEY (ID),\n" +
                "    CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES departments(department_id), \n" +
                "    CONSTRAINT uk_my_table UNIQUE (some_other_column)\n" +
                ")";
        ArrayList<String> preAndSuffix = SqlStringUtil.suffixCreateSqlInterception(sql2, "CONSTRAINT");
        System.out.println(preAndSuffix.get(0));

//        CONSTRAINT
//        String createSqlNoKey = SqlStringUtil.suffixCreateSqlInterception(sql1, "CONSTRAINT");
//        System.out.println(createSqlNoKey);
        List<SQLStatement> stmtList = SQLUtils.parseStatements(preAndSuffix.get(0), DbType.oracle);
        StringBuilder out = new StringBuilder();
        OracleToStarrocksOutputVisitor visitor = new OracleToStarrocksOutputVisitor(out, false);
        for(SQLStatement sqlStatement : stmtList) {
            sqlStatement.accept(visitor);
        }

        System.out.println(out.toString() + "\n" +preAndSuffix.get(1));
    }

}
