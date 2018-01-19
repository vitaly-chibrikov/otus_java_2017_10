package ru.otus.mybatis;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import ru.otus.user.UsersDataSet;

/**
 * Created by tully.
 */
public class MainXMLExample {
    public static void main(String[] args) throws IOException {
        Reader reader = Resources.getResourceAsReader("config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            session.insert("ru.otus.mybatis.insert", new UsersDataSet(1,"deadbeaf"));
            session.commit();

            UsersDataSet dataSet = session.selectOne("ru.otus.mybatis.select", "deadbeaf");
            System.out.println(dataSet);
        }
    }
}
