package moe.pine.meteorshower.health.repositories

import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass

open class TestBase {
    companion object {
        protected lateinit var sqlSessionFactory: SqlSessionFactory

        @BeforeClass
        @JvmStatic
        fun setUpBeforeClass() {
            val config = Resources.getResourceAsStream("mybatis-config-test.xml")
            sqlSessionFactory =
                SqlSessionFactoryBuilder()
                    .build(config)
                    .apply {
                        val pkg = TestBase::class.java.packageName
                        configuration.addMappers(pkg)
                    }
        }
    }

    protected lateinit var sqlSession: SqlSession

    @Before
    open fun setUp() {
        sqlSession = sqlSessionFactory.openSession(true)
    }

    @After
    fun tearDown() {
        sqlSession.close()
    }
}