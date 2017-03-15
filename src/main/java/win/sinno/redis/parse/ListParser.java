package win.sinno.redis.parse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import win.sinno.common.util.JsonUtil;
import win.sinno.redis.annotation.List;
import win.sinno.redis.annotation.ListColumn;
import win.sinno.redis.exception.InvalidParameterException;
import win.sinno.redis.exception.MissAnnotationException;
import win.sinno.redis.model.KeyValuePair;
import win.sinno.redis.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2016-10-11 11:51.
 */
public class ListParser {

    public static final Logger LOG = LoggerFactory.getLogger("redis_list_parser");

    /**
     * 将value解析转换成对象
     *
     * @param t
     * @param value
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(T t, String value) {

        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();

        if (ArrayUtils.isEmpty(fields)) {
            return t;
        }

        Map<String, String> params = JsonUtil.fromJson(value, Map.class);

        for (Field field : fields) {
            ListColumn listColumn = field.getAnnotation(ListColumn.class);

            if (listColumn != null) {
                String columnName = getColumnName(field, listColumn);

                if (params.containsKey(columnName)) {
                    String val = params.get(columnName);
                    ReflectUtil.setFieldValue4Str(t, field, val);
                }
            }
        }

        return t;
    }


    /**
     * 将对象转为key value pair
     *
     * @param obj
     * @param <T>
     * @return
     * @throws MissAnnotationException
     */
    public static <T> KeyValuePair obj2Set(T obj) throws MissAnnotationException {

        Class<?> clazz = obj.getClass();
        List listAnnotation = clazz.getAnnotation(List.class);

        if (null == listAnnotation) {
            throw new MissAnnotationException(clazz.getCanonicalName(), List.class.getCanonicalName());
        }

        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> map = new HashMap<String, String>();

        if (ArrayUtils.isNotEmpty(fields)) {
            for (Field field : fields) {
                ListColumn listColumn = field.getAnnotation(ListColumn.class);

                if (listColumn != null) {
                    String val = ReflectUtil.getFieldValue2Str(obj, field);

                    if (StringUtils.isNotEmpty(val)) {
                        map.put(getColumnName(field, listColumn), val);
                    }

                }
            }
        }

        String key = listAnnotation.key();
        String value = JsonUtil.toJson(map);

        KeyValuePair keyValuePair = new KeyValuePair();
        keyValuePair.setKey(key);
        keyValuePair.setValue(value);

        return keyValuePair;
    }

    /**
     * 获取注解列名
     * 1.ColumnName.name未设置则获取field的名称
     * 2.ColumnName.name设置则获取该值
     *
     * @param field
     * @param listColumn
     * @return
     */
    protected static String getColumnName(Field field, ListColumn listColumn) {
        String annoListColumn = listColumn.name();

        return StringUtils.isNotEmpty(annoListColumn) ? annoListColumn : field.getName();
    }

    public static <T> java.util.List<T> string2Obj(T t, java.util.List<String> valueList) {

        if (CollectionUtils.isEmpty(valueList)) {
            throw new InvalidParameterException("format obj can not null");
        }

        java.util.List<T> tList = new ArrayList<T>();
        Class<?> clazz = t.getClass();

        for (String value : valueList) {
            try {
                tList.add(string2Obj((T) clazz.newInstance(), value));
            } catch (InstantiationException e) {
                LOG.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return tList;
    }
}
