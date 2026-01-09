package org.t2404e.wcd_test1.dao;

import org.t2404e.wcd_test1.model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {

    public void insert(Player p) throws Exception {
        String sql = "INSERT INTO player(name, full_name, age, index_id) VALUES(?,?,?,?)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, p.getName());
        ps.setString(2, p.getFullName());
        ps.setString(3, p.getAge());
        ps.setInt(4, p.getIndexId());

        ps.executeUpdate();
    }

    public int insertAndReturnId(Player p) throws Exception {
        String sql = "INSERT INTO player(name, full_name, age, index_id) VALUES(?,?,?,?)";
        Connection conn = DBConnection.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
        );

        ps.setString(1, p.getName());
        ps.setString(2, p.getFullName());
        ps.setString(3, p.getAge());
        ps.setInt(4, p.getIndexId());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // player_id vừa insert
        }
        return 0;
    }

    public List<Player> getAll() throws Exception {
        List<Player> list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();

        String sql = """
            SELECT p.*, i.name AS index_name
            FROM player p
            JOIN indexer i ON p.index_id = i.index_id
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Player p = new Player();
            p.setPlayerId(rs.getInt("player_id"));
            p.setName(rs.getString("name"));
            p.setFullName(rs.getString("full_name"));
            p.setAge(rs.getString("age"));
            p.setIndexId(rs.getInt("index_id"));
            p.setIndexName(rs.getString("index_name"));
            list.add(p);
        }
        return list;
    }

   git
    public Player findById(int id) throws Exception {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM player WHERE player_id=?"
        );
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Player p = new Player();
            p.setPlayerId(id);
            p.setName(rs.getString("name"));
            p.setFullName(rs.getString("full_name"));
            p.setAge(rs.getString("age"));
            p.setIndexId(rs.getInt("index_id"));
            return p;
        }
        return null;
    }

    // ===================== UPDATE =====================
    public void update(Player p) throws Exception {
        String sql = "UPDATE player SET name=?, full_name=?, age=?, index_id=? WHERE player_id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, p.getName());
        ps.setString(2, p.getFullName());
        ps.setString(3, p.getAge());
        ps.setInt(4, p.getIndexId());
        ps.setInt(5, p.getPlayerId());

        ps.executeUpdate();
    }

    // ===================== DELETE =====================
    public void delete(int id) throws Exception {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM player WHERE player_id=?"
        );
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}
