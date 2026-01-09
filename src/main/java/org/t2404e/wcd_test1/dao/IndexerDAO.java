package org.t2404e.wcd_test1.dao;

import org.t2404e.wcd_test1.model.Indexer;
import java.sql.*;
import java.util.*;

public class IndexerDAO {

    public List<Indexer> getAll() throws Exception {
        List<Indexer> list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM indexer");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Indexer i = new Indexer();
            i.setIndexId(rs.getInt("index_id"));
            i.setName(rs.getString("name"));
            i.setValueMin(rs.getFloat("valueMin"));
            i.setValueMax(rs.getFloat("valueMax"));
            list.add(i);
        }
        return list;
    }

    public Indexer findById(int id) throws Exception {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps =
                conn.prepareStatement("SELECT * FROM indexer WHERE index_id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Indexer i = new Indexer();
            i.setIndexId(id);
            i.setName(rs.getString("name"));
            i.setValueMin(rs.getFloat("valueMin"));
            i.setValueMax(rs.getFloat("valueMax"));
            return i;
        }
        return null;
    }
}
