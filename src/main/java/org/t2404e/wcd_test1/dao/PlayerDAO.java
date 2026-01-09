package org.t2404e.wcd_test1.dao;

import org.t2404e.wcd_test1.model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {

    // ===================== INSERT =====================
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

    // INSERT + LẤY player_id
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
            return rs.getInt(1);
        }
        return 0;
    }

    // ===================== GET ALL  =====================
    public List<Player> getAll() throws Exception {
        List<Player> list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();

        String sql = """
            SELECT p.*,
                   i.name AS index_name,
                   pi.value AS index_value
            FROM player p
            JOIN indexer i ON p.index_id = i.index_id
            LEFT JOIN player_index pi ON p.player_id = pi.player_id
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
            p.setIndexValue(rs.getFloat("index_value"));

            list.add(p);
        }
        return list;
    }

    // ===================== FIND BY ID =====================
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
        String sqlPlayer = "UPDATE player SET name=?, full_name=?, age=?, index_id=? WHERE player_id=?";
        String sqlIndex = "UPDATE player_index SET value=?, index_id=? WHERE player_id=?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu giao dịch
            try {
                // 1. Cập nhật bảng player
                PreparedStatement ps1 = conn.prepareStatement(sqlPlayer);
                ps1.setString(1, p.getName());
                ps1.setString(2, p.getFullName());
                ps1.setString(3, p.getAge());
                ps1.setInt(4, p.getIndexId());
                ps1.setInt(5, p.getPlayerId());
                ps1.executeUpdate();

                // 2. Cập nhật bảng player_index (giá trị index_value)
                PreparedStatement ps2 = conn.prepareStatement(sqlIndex);
                ps2.setFloat(1, p.getIndexValue());
                ps2.setInt(2, p.getIndexId());
                ps2.setInt(3, p.getPlayerId());
                ps2.executeUpdate();

                conn.commit(); // Hoàn tất giao dịch
            } catch (Exception e) {
                conn.rollback(); // Quay lại nếu có lỗi
                throw e;
            }
        }
    }

    // ===================== DELETE =====================
    public void delete(int id) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu giao dịch
            try {
                // 1. Xóa ở bảng player_index trước (image_0deaa5.png)
                PreparedStatement ps1 = conn.prepareStatement("DELETE FROM player_index WHERE player_id = ?");
                ps1.setInt(1, id);
                ps1.executeUpdate();

                // 2. Sau đó xóa ở bảng player
                PreparedStatement ps2 = conn.prepareStatement("DELETE FROM player WHERE player_id = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
