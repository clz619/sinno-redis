package win.sinno.redis.parse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import win.sinno.common.util.JsonUtil;
import win.sinno.redis.annotation.Set;
import win.sinno.redis.annotation.SetColumn;
import win.sinno.redis.exception.InvalidParameterException;
import win.sinno.redis.exception.MissAnnotationException;
import win.sinno.redis.model.KeyValuePair;
import win.sinno.redis.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author : admin@chenlizhong.cn
 * @version : 1.0
 * @since : 2016-10-11 11:51.
 */
public class SetParser {

    public static final Logger LOG = LoggerFactory.getLogger("redis_set_parser");

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
            SetColumn setColumn = field.getAnnotation(SetColumn.class);

            if (setColumn != null) {
                String columnName = getColumnName(field, setColumn);

                if (params.containsKey(columnName)) {
                    String val = params.get(columnName);
                    ReflectUtil.setFieldValue4Str(t, field, val);
                }
            }
        }

        return t;
    }


    /**
     * 将对象转为set
     *
     * @param obj
     * @param <T>
     * @return
     * @throws MissAnnotationException
     */
    public static <T> KeyValuePair obj2Set(T obj) throws MissAnnotationException {

        Class<?> clazz = obj.getClass();
        Set setAnnotation = clazz.getAnnotation(Set.class);

        if (null == setAnnotation) {
            throw new MissAnnotationException(clazz.getCanonicalName(), Set.class.getCanonicalName());
        }

        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> map = new HashMap<String, String>();

        if (ArrayUtils.isNotEmpty(fields)) {
            for (Field field : fields) {
                SetColumn setColumn = field.getAnnotation(SetColumn.class);

                if (setColumn != null) {
                    String val = ReflectUtil.getFieldValue2Str(obj, field);

                    if (StringUtils.isNotEmpty(val)) {
                        map.put(getColumnName(field, setColumn), val);
                    }

                }
            }
        }

        String key = setAnnotation.key();
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
     * @param setColumn
     * @return
     */
    public static String getColumnName(Field field, SetColumn setColumn) {
        String annoSetColumn = setColumn.name();

        return StringUtils.isNotEmpty(annoSetColumn) ? annoSetColumn : field.getName();
    }

    public static <T> java.util.Set<T> string2Obj(T t, java.util.Set<String> valueSet) {

        if (CollectionUtils.isEmpty(valueSet)) {
            throw new InvalidParameterException("format obj can not null");
        }

        java.util.Set<T> tSet = new HashSet<T>();
        Class<?> clazz = t.getClass();

        for (String value : valueSet) {
            try {
                tSet.add(string2Obj((T) clazz.newInstance(), value));
            } catch (InstantiationException e) {
                LOG.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return tSet;
    }
}
