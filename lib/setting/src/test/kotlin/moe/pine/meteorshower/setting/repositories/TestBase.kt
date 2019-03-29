package moe.pine.meteorshower.setting.repositories

import org.apache.ibatis.io.Resources
import org.apache.ibatis.jdbc.ScriptRunner
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
        val session = sqlSessionFactory.openSession(true)
        val runner = ScriptRunner(session.connection)
        val dropScript = Resources.getResourceAsReader("drop_tables.sql")
        val createScript = Resources.getResourceAsReader("create_tables.sql")
        runner.setStopOnError(true)
        runner.runScript(dropScript)
        runner.runScript(createScript)
        runner.closeConnection()

        sqlSession = sqlSessionFactory.openSession(true)
    }

    @After
    fun tearDown() {
        sqlSession.close()
    }
}