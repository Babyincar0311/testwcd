package org.t2404e.wcd_test1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PlayerIndexDAO {

    public void insert(int playerId, int indexId, float value) throws Exception {
        String sql = "INSERT INTO player_index(player_id, index_id, value) VALUES (?,?,?)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, playerId);
        ps.setInt(2, indexId);
        ps.setFloat(3, value);
        ps.executeUpdate();
    }
}
