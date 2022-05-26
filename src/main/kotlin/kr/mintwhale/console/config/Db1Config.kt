package kr.mintwhale.console.config

import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.SqlSessionTemplate
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@MapperScan(value = ["kr.mintwhale.console.mapper.dao"], sqlSessionFactoryRef = "db1SqlSessionFactory")
class Db1Config {

    @Bean(name = ["db1DataSource"])
    @Primary
    @ConfigurationProperties(prefix = "spring.db1.datasource")
    fun db1DataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean(name = ["db1SqlSessionFactory"])
    @Primary
    @Throws(Exception::class)
    fun db1SqlSessionFactory(@Qualifier("db1DataSource") db1DataSource: DataSource, applicationContext: ApplicationContext): SqlSessionFactory {
        val sqlSessionFactoryBean = SqlSessionFactoryBean()
        sqlSessionFactoryBean.setDataSource(db1DataSource)
        sqlSessionFactoryBean.setMapperLocations(*applicationContext.getResources("classpath:mapper/dao/*.xml"))
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"))
        sqlSessionFactoryBean.setTypeAliasesPackage("kr.mintwhale.console.data.model")
        return sqlSessionFactoryBean.`object`!!
    }

    @Bean(name = ["db1SqlSessionTemplate"])
    @Primary
    @Throws(Exception::class)
    fun db1SqlSessionTemplate(db1SqlSessionFactory: SqlSessionFactory): SqlSessionTemplate {
        return SqlSessionTemplate(db1SqlSessionFactory)
    }

    @Bean
    fun transactionManager(): DataSourceTransactionManager {
        return DataSourceTransactionManager(db1DataSource())
    }

}