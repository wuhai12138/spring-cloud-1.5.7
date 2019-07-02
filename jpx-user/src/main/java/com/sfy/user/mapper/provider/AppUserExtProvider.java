package com.sfy.user.mapper.provider;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 金鹏祥 on 2019/5/17.
 */
@Slf4j
public class AppUserExtProvider {



    public String getSqlByParams(Map<String, Object> conditions, int isQuery) throws Exception {

        Map<String, Object> search = (HashMap<String, Object>) conditions.get("search");
        String from = " from user u inner join t_user_info t on u.id = t.user_id ";

        StringBuilder where = new StringBuilder();
        where.append("where 1=1 ");
        if (search.containsKey("id")) {
            where.append(" and u.id = " + (Integer) search.get("id"));
        }
        if (search.containsKey("username")) {
            where.append(" and u.username like '%" + search.get("username") + "%'");
        }
        if (search.containsKey("email")) {
            where.append(" and u.email like '%" + search.get("email") + "%' ");
        }
        if (search.containsKey("enabled")) {
            where.append(" and u.enabled = " + (Byte) search.get("enabled"));
        }
        if (search.containsKey("userNickname")) {
            where.append(" and t.user_nickname like '%" + search.get("userNickname") + "%'");
        }
        if (search.containsKey("userCode")) {
            where.append(" and t.user_code = " + search.get("userCode"));
        }
        if (search.containsKey("registerType")) {
            where.append(" and t.register_type = " + search.get("registerType"));
        }
        if (search.containsKey("regBeginTime")) {
            where.append(" and t.register_time >= " + (Long) search.get("regBeginTime"));
        }
        if (search.containsKey("regEndTime")) {
            where.append(" and t.register_time <=" + (Long) search.get("regEndTime"));
        }

        if (isQuery == 0) {
            return "select count(u.id)" + from + where.toString();
        }

        int offset = (Integer) conditions.get("offset");
        int limit = (Integer) conditions.get("limit");
        String orderBy = conditions.get("orderBy").toString();
        String fields = " u.id,u.username,u.first_name,u.last_name,u.email,u.image_url,u.enabled,u.password_revised," +
                "t.user_code,t.user_nickname,t.user_icon,t.user_address,t.register_type,t.register_mobile,t.mobile_version," +
                "t.app_version,t.register_time,t.is_login,t.login_time,t.device_id,t.create_time,t.update_time ";
        StringBuilder query = new StringBuilder();
        query.append(" select ");
        query.append(fields);
        query.append(from);
        query.append(where);
        if (!Strings.isNullOrEmpty(orderBy)) {
            query.append(" order by " + orderBy);
        }
        if (limit > 0) {
            query.append(String.format(" limit %d,%d ", offset, limit));
        }

        return query.toString();
    }

    /**
     * 获取APP用户数量
     *
     * @param conditions
     * @return
     */
    public String count(Map<String, Object> conditions) {

        try {
            String countSql = getSqlByParams(conditions, 0);
            return countSql;
        } catch (Exception ex) {
            log.error("AppUserExtProvider.count 错误：" + ex.getMessage());
        }
        return "";
    }

    /**
     * 获取APP用户列表
     *
     * @param conditions
     * @return
     */
    public String query(Map<String, Object> conditions) {

        try {
            String querySql = getSqlByParams(conditions, 1);
            return querySql;
        } catch (Exception ex) {
            log.error("AppUserExtProvider.query 错误：" + ex.getMessage());
        }
        return "";
    }
}
