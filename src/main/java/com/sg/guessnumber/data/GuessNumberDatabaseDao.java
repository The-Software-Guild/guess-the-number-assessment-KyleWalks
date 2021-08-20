/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.guessnumber.data;

import com.sg.guessnumber.models.GuessNumber;
import com.sg.guessnumber.models.Round;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"database", "prod"})
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
    public Round add(Round round) {
        final String sql = "INSERT INTO round(guess,result,roundTime,guessnumber_id) VALUES(?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql, 
                Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, round.getGuess());
            statement.setString(2, round.getResult());
            statement.setTimestamp(3, Timestamp.valueOf(round.getRoundTime()));
            statement.setInt(4, round.getGuessNumberId());
            return statement;

        }, keyHolder);
              
        round.setId(keyHolder.getKey().intValue());

        return round;
    }

    @Override
    public List<GuessNumber> getAllGames() {
        final String sql = "SELECT id, answer, finished FROM guessNumber;";
        return jdbcTemplate.query(sql, new GuessNumberMapper());
    }
    
    @Override
    public List<Round> getAllRounds(int id) {
        final String sql = "SELECT R.id, R.guess, R.result, R.roundTime, R.guessnumber_id "
                + "FROM round R "
                + "JOIN guessnumber GN ON R.guessnumber_id = GN.id "
                + "WHERE R.guessnumber_id = " + id + ";";
        return jdbcTemplate.query(sql, new RoundMapper());
    }

    @Override
    public GuessNumber findById(int id) {

        final String sql = "SELECT id, answer, finished "
                + "FROM guessNumber WHERE id = ?;";

        return jdbcTemplate.queryForObject(sql, new GuessNumberMapper(), id);
    }

    @Override
    public boolean update(GuessNumber guessNumber) {

        final String sql = "UPDATE guessNumber SET "
                + "finished = ? "
                + "WHERE id = ?;";

        return jdbcTemplate.update(sql,
                guessNumber.isFinished() ? 1 : 0,
                guessNumber.getId()) > 0;
    }

    @Override
    public boolean deleteGameById(int id) {
        deleteRoundsByGame(findById(id));
        final String sql = "DELETE FROM guessNumber WHERE id = ?;";
        return jdbcTemplate.update(sql, id) > 0;
    }
    
    @Override
    public boolean deleteRoundsByGame(GuessNumber gn) {
        final String sql = "DELETE FROM round WHERE guessnumber_id = ?;";
        return jdbcTemplate.update(sql, gn.getId()) > 0;
    }

    private static final class GuessNumberMapper implements RowMapper<GuessNumber> {

        @Override
        public GuessNumber mapRow(ResultSet rs, int index) throws SQLException {
            GuessNumber gn = new GuessNumber();
            gn.setId(rs.getInt("id"));
            gn.setFinished(rs.getBoolean("finished"));
            gn.setAnswer(rs.getInt("answer"));
            
            return gn;
        }
    }
    
    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round rd = new Round();
            rd.setId(rs.getInt("id"));
            rd.setGuess(rs.getInt("guess"));
            rd.setResult(rs.getString("result"));
            rd.setRoundTime(rs.getTimestamp("roundTime").toLocalDateTime());
            rd.setGuessNumberId(rs.getInt("guessnumber_id"));
            return rd;
        }
    }
}