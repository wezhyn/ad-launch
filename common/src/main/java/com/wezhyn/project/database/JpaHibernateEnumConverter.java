package com.wezhyn.project.database;


import com.wezhyn.project.NumberEnum;
import com.wezhyn.project.StringEnum;
import com.wezhyn.project.annotation.StrategyEnum;
import com.wezhyn.project.database.impl.EnumStringMapperFactory;
import com.wezhyn.project.database.impl.NumberEnumMapperFactory;
import com.wezhyn.project.database.impl.OrdinalEnumMapperFactory;
import com.wezhyn.project.database.impl.StringEnumMapperFactory;
import lombok.AllArgsConstructor;
import org.hibernate.AssertionFailure;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Properties;

/**
 * @author wezhyn
 * @since 12.11.2019
 */
@SuppressWarnings("unchecked")
public class JpaHibernateEnumConverter<T extends Enum<T>> implements DynamicParameterizedType, UserType {

    private static final EnumType DEFAULT_ENUM_TYPE=EnumType.TO_STRING;

    private static final ResultSetToPrimitive<String> DEFAULT_RESULT_SET_2_STRING=(rs, names)->{
        return rs.getString(names[0]);
    };
    private static final ResultSetToPrimitive<Integer> DEFAULT_RESULT_SET_2_INT=(rs, names)->{
        return rs.getInt(names[0]);
    };
    private static final StringEnumMapperFactory STRING_ENUM_MAPPER_FACTORY=new StringEnumMapperFactory();
    private static final EnumStringMapperFactory ENUM_STRING_MAPPER_FACTORY=new EnumStringMapperFactory();
    private static final NumberEnumMapperFactory NUMBER_ENUM_MAPPER_FACTORY=new NumberEnumMapperFactory();
    private static final OrdinalEnumMapperFactory ORDINAL_ENUM_MAPPER_FACTORY=new OrdinalEnumMapperFactory();

//    todo: 代添加缓存


    /**
     * 枚举类型
     */
    private Class<Enum<?>> enumClass;
    private int sqlType;
    private EnumValueMapperSupport<?, ?> enumMapper;


    @Override
    public void setParameterValues(Properties parameters) {
        ParameterType reader=(ParameterType) parameters.get(DynamicParameterizedType.PARAMETER_TYPE);
        EnumType enumType;
        if (reader!=null) {
            enumClass=reader.getReturnedClass().asSubclass(Enum.class);
//            获取转变策略
            enumType=getEnumType(reader);
        } else {
            throw new RuntimeException("再说吧");
        }
        switch (enumType) {
            case STRING: {
                sqlType=Types.VARCHAR;
                enumMapper=new EnumValueMapperSupport<>(sqlType,
                        STRING_ENUM_MAPPER_FACTORY.getTypeEnumMapper(),
                        STRING_ENUM_MAPPER_FACTORY.getPrimitiveEnumMapper(enumClass),
                        DEFAULT_RESULT_SET_2_STRING
                );
                return;
            }
            case NUMBER: {
                sqlType=Types.INTEGER;
                enumMapper=new EnumValueMapperSupport<NumberEnum, Integer>(sqlType,
                        NUMBER_ENUM_MAPPER_FACTORY.getTypeEnumMapper(),
                        NUMBER_ENUM_MAPPER_FACTORY.getPrimitiveEnumMapper(enumClass),
                        DEFAULT_RESULT_SET_2_INT);
                return;
            }
            case TO_STRING: {
                sqlType=Types.VARCHAR;
                enumMapper=new EnumValueMapperSupport<>(sqlType,
                        ENUM_STRING_MAPPER_FACTORY.getTypeEnumMapper(),
                        ENUM_STRING_MAPPER_FACTORY.getPrimitiveEnumMapper(enumClass),
                        DEFAULT_RESULT_SET_2_STRING);
                return;
            }
            default: {
                throw new RuntimeException("待开发");
            }
        }
    }

    private EnumType getEnumType(ParameterType reader) {
//        标注的注解上获取 StrategyEnum
        StrategyEnum enumAnn=getAnnotation(reader.getAnnotationsMethod(), StrategyEnum.class);
        if (enumAnn!=null) {
            return enumAnn.value();
        }
//        无标记，从类型上获取
        boolean isStringEnum=StringEnum.class.isAssignableFrom(enumClass);
        boolean isNumberEnum=NumberEnum.class.isAssignableFrom(enumClass);
        return isStringEnum ? EnumType.STRING : (isNumberEnum ? EnumType.NUMBER : DEFAULT_ENUM_TYPE);
    }


    @SuppressWarnings("unchecked")
    private <T extends Annotation> T getAnnotation(Annotation[] annotations, Class<T> anClass) {
        for (Annotation annotation : annotations) {
            if (anClass.isInstance(annotation)) {
                return (T) annotation;
            }
        }
        return null;
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{sqlType};
    }

    @Override
    public Class returnedClass() {
        return enumClass;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return Objects.hashCode(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        if (enumMapper==null) {
            throw new AssertionFailure("EnumType (" + enumClass.getName() + ") not properly, fully configured");
        }
        return enumMapper.getValue(rs, names);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (enumMapper==null) {
            throw new AssertionFailure("EnumType (" + enumClass.getName() + ") not properly, fully configured");
        }

        enumMapper.setValue(st, value, index);
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }


    private interface EnumValueMapper<T> {
        /**
         * 获取枚举
         *
         * @param rs    RS
         * @param names Names
         * @return 枚举
         * @throws SQLException exception
         */
        T getValue(ResultSet rs, String[] names) throws SQLException;

        /**
         * 持久化枚举
         *
         * @param st    语句
         * @param value 值
         * @param index idnex
         * @throws SQLException exception
         */
        void setValue(PreparedStatement st, Object value, int index) throws SQLException;
    }

    private interface ResultSetToPrimitive<R> {
        /**
         * 获取 ResultSet 对应列的值
         *
         * @param rs    set
         * @param names 列名
         * @return int 类型 or String 类型
         * @throws SQLException exception
         */
        R apply(ResultSet rs, String[] names) throws SQLException;
    }


    @AllArgsConstructor
    public static class EnumValueMapperSupport<T, R> implements EnumValueMapper<T> {

        private int sqlType;
        private TypeEnumMapper<T, R> enum2PrimitiveMapper;
        private PrimitiveEnumMapper<T, R> primitive2EnumMapper;
        private ResultSetToPrimitive<R> resultSetToPrimitive;

        protected R extractJdbcValue(T value) {
            return enum2PrimitiveMapper.apply(value);
        }


        @Override
        public T getValue(ResultSet rs, String[] names) throws SQLException {
            R value=resultSetToPrimitive.apply(rs, names);
            return value==null ? null : primitive2EnumMapper.apply(value);
        }


        @Override
        public void setValue(PreparedStatement st, Object value, int index) throws SQLException {
            T v=(T) value;
            final Object jdbcValue=value==null ? null : extractJdbcValue(v);
            if (jdbcValue==null) {
                st.setNull(index, sqlType);
                return;
            }
            st.setObject(index, jdbcValue, sqlType);
        }
    }

}
