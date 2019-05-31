package com.javaweb.MichaelKai.interceptor;

import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.utils.DateUtil;
import com.javaweb.MichaelKai.common.utils.ReflectUtil;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: project_base
 * @description: sql修改拦截器
 * @author: YuKai Fan
 * @create: 2019-05-31 09:44
 **/
@Component
//标识拦截StatementHandler
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class})})
public class SqlAppendInterceptor implements Interceptor {

    private static final Pattern INSERT_PARSE_PTN = Pattern.compile("(insert into .+\\()(.+)(\\)\\s*values)(.+)",  Pattern.CASE_INSENSITIVE);

    private static final Pattern INSERT_VALUE_SELECT_PTN = Pattern.compile("(select)(.+)(from)(.+)", Pattern.CASE_INSENSITIVE);


    /**
     * 拦截后要执行的方法
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
        //获取RoutingStatementHandler中的属性delegate的值
        PreparedStatementHandler delegate = (PreparedStatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
        //获取拦截请求后的sql
        BoundSql boundSql = delegate.getBoundSql();
        String sql = boundSql.getSql();

        MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(delegate, "mappedStatement");
        String sqlId = mappedStatement.getId();
        //判断拦截的sql是否是日志或者日志分析有关的sql
        if (sqlId.contains("LogMapper") || sqlId.contains("LogAnalysisMapper")) {
            return invocation.proceed();
        }

        SqlCommandType sqlType = null;
        String lowcaseSql = boundSql.getSql().toLowerCase().trim();
        //判断拦截的sql类型 (插入或更新操作)
        if (lowcaseSql.startsWith("insert")) {
            sqlType = SqlCommandType.INSERT;
        } else if (lowcaseSql.startsWith("update")) {
            sqlType = SqlCommandType.UPDATE;
        }

        if (sqlType != null) {
            //修改sql
            changeBoundSql(boundSql, sqlType);
        }

        return invocation.proceed();
    }

    /**
     * 修改拦截的sql
     * @param boundSql
     * @param sqlType
     */
    private void changeBoundSql(BoundSql boundSql, SqlCommandType sqlType) {
        User user = ShiroKit.getUser();
        String userId = user.getId();
        userId = StringUtils.isEmpty(userId) ? "-1" : userId;
        String time = DateUtil.dateToString(new Date(), Constant.DATE_FORMAT_CREATE_UPDATE);

        String sql = format(boundSql.getSql());
        if (sqlType == SqlCommandType.INSERT) {
            //修改insert操作sql
            sql = sqlWithInsert(sql, userId, time);
        } else if (sqlType == SqlCommandType.UPDATE) {
            //修改update操作sql
            sql = sqlWithUpdate(sql, userId, time);
        }

        ReflectUtil.setFieldValue(boundSql, "sql", sql);
    }

    /**
     * 要修改的update操作字段
     * @param sql
     * @param userId
     * @param time
     * @return
     */
    private String sqlWithUpdate(String sql, String userId, String time) {
        String changeSql = sql;
        if (!changeSql.contains("update_user_id")) {
            String[] splitByWhere = changeSql.toLowerCase().split("where");
            if (splitByWhere.length == 2) {
                changeSql = splitByWhere[0] + ",update_user_id='" + userId + "'"
                        + " where " + splitByWhere[1];
            }
        }
        if (!changeSql.contains("update_time")) {
            String[] splitByWhere = changeSql.toLowerCase().split("where");
            if (splitByWhere.length == 2) {
                changeSql = splitByWhere[0] + ",update_time='" + time + "'"
                        + " where " + splitByWhere[1];
            }
        }
        return changeSql;
    }

    /**
     * 要修改的insert操作字段
     * @param sql
     * @param userId
     * @param time
     * @return
     */
    private String sqlWithInsert(String sql, String userId, String time) {
        String changeSql = sql + " "; //追加空格确保以）分隔后的数组长度
        if (!changeSql.contains("create_user_id")) {
            changeSql = changeSqlWithInsert(changeSql, "create_user_id", userId);
        }
        if (!changeSql.contains("create_time")) {
            changeSql = changeSqlWithInsert(changeSql, "create_time", time);
        }
        if (!changeSql.contains("update_user_id")) {
            changeSql = changeSqlWithInsert(changeSql, "update_user_id", userId);
        }
        if (!changeSql.contains("update_time")) {
            changeSql = changeSqlWithInsert(changeSql, "update_time", time);
        }
        return changeSql;
    }

    /**
     * 修改insert操作的sql
     * @param sql
     * @param field
     * @param value
     * @return
     */
    private String changeSqlWithInsert(String sql, String field, String value) {
        Matcher matcher = INSERT_PARSE_PTN.matcher(sql);
        if (matcher.find()) {
            StringBuilder sbSql = new StringBuilder(matcher.group(1));
            sbSql.append(matcher.group(2)).append(",").append(field).append(matcher.group(3));

            String strValues = matcher.group(4);

            if (strValues.contains("select")) {
                Matcher matcherSelect = INSERT_VALUE_SELECT_PTN.matcher(strValues);
                if (matcherSelect.find()) {
                    sbSql.append(" select ").append(matcherSelect.group(2)).append(",'").append(value)
                            .append("'").append(" from ").append(matcherSelect.group(4));
                }
            } else {
                strValues = strValues.replaceAll("\\)\\s?,\\s?\\(", "),(");
                if (strValues.contains("),(")) {
                    String[] values = strValues.split("\\).\\(");

                    for (int i = 0, j = values.length; i < j; i++) {
                        if (i == 0) {
                            sbSql.append(values[i]).append(",'").append(value).append("'),");
                        } else if (i == j - 1) {
                            sbSql.append("(").append(values[i].substring(0, values[i].lastIndexOf(")")))
                                    .append(",'").append(value).append("')");
                        } else {
                            sbSql.append("(").append(values[i]).append(",'").append(value).append("'),");
                        }
                    }
                } else {
                    sbSql.append(strValues.substring(0, strValues.lastIndexOf(")"))).append(",'")
                            .append(value).append("')");
                }
            }

            return sbSql.toString();
        }
        return sql;
    }

    /**
     * 去除sql中多余的空格和换行符
     * @param sql
     * @return
     */
    private String format(String sql) {
        if (StringUtils.isEmpty(sql)) {
            return sql;
        }
        return sql.replaceAll("\\s{1,}", " ").toLowerCase();
    }

    /**
     * 拦截器对应的封装原始对象的方法,拦截之前先执行此方法
     * @param o
     * @return
     */
    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}