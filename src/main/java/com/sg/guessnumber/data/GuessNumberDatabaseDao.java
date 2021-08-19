/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessnumber.data;

import com.sg.guessnumber.models.GuessNumber;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Profile("database")
public class GuessNumberDatabaseDao implements GuessNumberDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GuessNumberDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GuessNumber add(GuessNumber guessNumber) {

        final String sql = "INSERT INTO guessnumber(answer) VALUES(?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql, 
                Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, guessNumber.getAnswer());
            return statement;

        }, keyHolder);

        guessNumber.setId(keyHolder.getKey().intValue());

        return guessNumber;
    }

    @Override
    public List<GuessNumber> getAll() {
        final String sql = "SELECT id, answer, guess, finished FROM guessNumber;";
        return jdbcTemplate.query(sql, new GuessNumberMapper());
    }

    @Override
    public GuessNumber findById(int id) {

        final String sql = "SELECT id, answer, guess, finished "
                + "FROM guessNumber WHERE id = ?;";

        return jdbcTemplate.queryForObject(sql, new GuessNumberMapper(), id);
    }

    @Override
    public boolean update(GuessNumber guessNumber) {

        final String sql = "UPDATE guessNumber SET "
                + "guess = ?, "
                + "WHERE id = ?;";

        return jdbcTemplate.update(sql,
                guessNumber.getGuess(),
                guessNumber.getId()) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        final String sql = "DELETE FROM guessNumber WHERE id = ?;";
        return jdbcTemplate.update(sql, id) > 0;
    }

    private static final class GuessNumberMapper implements RowMapper<GuessNumber> {

        @Override
        public GuessNumber mapRow(ResultSet rs, int index) throws SQLException {
            GuessNumber td = new GuessNumber();
            td.setId(rs.getInt("id"));
            td.setAnswer(rs.getInt("answer"));
            td.setGuess(rs.getInt("guess"));
            td.setFinished(rs.getBoolean("finished"));
            return td;
        }
    }
}