package com.hxct.mapping;

import com.hxct.entity.AcEntity;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author cw
 * 定义sql映射的接口，使用注解指明方法要执行的SQL
 */
public interface AcMapperI extends Mapper{

    //使用@Insert注解指明add方法要执行的SQL
    @Insert("insert into ac(ipaddr, snmpport,readco,writeco,name,model,sysoid) values(#{ipaddr}, #{snmpport}, #{readco}, #{writeco}, #{name}, #{model}, #{sysoid})")
    public int add(AcEntity data);
    
    //使用@Delete注解指明deleteById方法要执行的SQL
    @Delete("delete from ac where id=#{id}")
    public int deleteById(int id);
    
    //使用@Update注解指明update方法要执行的SQL
    @Update("update ac set ipaddr=#{ipaddr},snmpport=#{snmpport},readco=#{readco},writeco=#{writeco},name=#{name},model=#{model},sysoid=#{sysoid} where id=#{id}")
    public int update(AcEntity data);
    
    //使用@Select注解指明getById方法要执行的SQL
    @Select("select * from ac where id=#{id}")
    public AcEntity getById(int id);
    
    //使用@Select注解指明getAll方法要执行的SQL
    @Select("select ipaddr,snmpport,readco,writeco,name,model,sysoid from ac")
    public List<AcEntity> getAllData();
//    
//    @InsertBatch("")
//    public int batchInsert();
}