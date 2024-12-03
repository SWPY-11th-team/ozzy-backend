package com.example.ozzy.other;

import com.example.ozzy.OzzyApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = OzzyApplication.class)
class OracleTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testOracleConnection() {
        String sql = "SELECT 1 FROM DUAL";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        assertThat(result).isEqualTo(1);
    }

}
