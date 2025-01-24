package com.suchtool.nicecommon.configuration;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.suchtool.nicecommon.core.provider.NiceCommonMyBatisFillUpdateSQLProvider;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Intercepts({@Signature(
		type = StatementHandler.class,
		method = "prepare",
		args = {Connection.class, Integer.class}
)})
public class NiceCommonMyBatisFillSQLInterceptor implements Interceptor {
	// 定义正则表达式用于提取 SET 部分的字段名
	private static final Pattern SET_PATTERN = Pattern.compile("UPDATE\\s+[^\\s]+\\s+(SET)");

	@Autowired
	private NiceCommonMyBatisFillUpdateSQLProvider niceCommonMybatisFillUpdateSQLProvider;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		if (SqlCommandType.UPDATE == mappedStatement.getSqlCommandType()
				&& StatementType.CALLABLE != mappedStatement.getStatementType()) {

			BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
			// 获取已经构造好的SQL
			String sql = boundSql.getSql();
			// 获取映射的参数
			List<ParameterMapping> mappings = new ArrayList(boundSql.getParameterMappings());
			// 假如参数中不包含要构造的参数，手动写入
			Map<String, String> fieldsMap = niceCommonMybatisFillUpdateSQLProvider.provideParam();
			for (Entry<String, String> map : fieldsMap.entrySet()) {
				String fieldKey = map.getKey();

				Matcher setMatcher = SET_PATTERN.matcher(sql);
				// 找到UPDATE才处理
				if (setMatcher.find()) {
					Pattern fieldPattern = Pattern.compile("UPDATE\\s+[^\\s]+\\s+(SET)\\s+(.|\n)+?" + fieldKey + "=(.|\n)+?WHERE(.|\n)+?");
					Matcher fieldMatcher = fieldPattern.matcher(sql);
					if (!fieldMatcher.find()) {
						// 原SQL没有此字段，则处理
						int indexOfSet = setMatcher.start(1);;
						String sqlPart = fieldKey + "='" + map.getValue() + "', ";
						sql = sql.substring(0, indexOfSet) + " SET " + sqlPart + sql.substring(indexOfSet + 4);
					}
				}
			}
			metaObject.setValue("delegate.boundSql.sql", sql);
			metaObject.setValue("delegate.boundSql.parameterMappings", mappings);
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return target instanceof StatementHandler ? Plugin.wrap(target, this) : target;
	}
}