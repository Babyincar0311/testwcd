package org.t2404e.wcd_test1.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

import org.t2404e.wcd_test1.dao.*;
import org.t2404e.wcd_test1.model.*;

@WebServlet("/player")
public class PlayerServlet extends HttpServlet {

    PlayerDAO playerDAO = new PlayerDAO();
    IndexerDAO indexerDAO = new IndexerDAO();
    PlayerIndexDAO playerIndexDAO = new PlayerIndexDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            String action = req.getParameter("action");

            if ("add".equals(action)) {
                req.setAttribute("indexers", indexerDAO.getAll());
                req.getRequestDispatcher("/player-form.jsp").forward(req, res);
                return;
            }

            if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("player", playerDAO.findById(id));
                req.setAttribute("indexers", indexerDAO.getAll());
                req.getRequestDispatcher("/player-form.jsp").forward(req, res);
                return;
            }

            if ("delete".equals(action)) {
                String idStr = req.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) {
                    playerDAO.delete(Integer.parseInt(idStr));
                }
                res.sendRedirect(req.getContextPath() + "/player");
                return;
            }

            req.setAttribute("players", playerDAO.getAll());
            req.getRequestDispatcher("/index.jsp").forward(req, res);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        try {
            String idStr = req.getParameter("playerId");
            String name = req.getParameter("name");
            String fullName = req.getParameter("fullName");
            String age = req.getParameter("age");
            int indexId = Integer.parseInt(req.getParameter("indexId"));

            // SỬA: Lấy giá trị indexValue ở ngoài để dùng cho cả Add và Edit
            float indexValue = Float.parseFloat(req.getParameter("indexValue"));

            // 1. Kiểm tra tên không được để trống
            if (name == null || name.trim().isEmpty()) {
                req.setAttribute("error", "Name is required");
                req.setAttribute("indexers", indexerDAO.getAll());
                req.getRequestDispatcher("/player-form.jsp").forward(req, res);
                return;
            }

            // 2. Kiểm tra dải giá trị chỉ số (Validation)
            Indexer idx = indexerDAO.findById(indexId);
            if (indexValue < idx.getValueMin() || indexValue > idx.getValueMax()) {
                req.setAttribute("error", "Index value must be between " + idx.getValueMin() + " and " + idx.getValueMax());
                req.setAttribute("indexers", indexerDAO.getAll());
                // Nếu đang Edit, cần nạp lại dữ liệu player cũ để hiển thị lại form
                if (idStr != null && !idStr.isEmpty()) {
                    req.setAttribute("player", playerDAO.findById(Integer.parseInt(idStr)));
                }
                req.getRequestDispatcher("/player-form.jsp").forward(req, res);
                return;
            }

            Player p = new Player();
            p.setName(name);
            p.setFullName(fullName);
            p.setAge(age);
            p.setIndexId(indexId);
            // SỬA: Gán giá trị vào object để PlayerDAO.update(p) có thể sử dụng
            p.setIndexValue(indexValue);

            if (idStr == null || idStr.isEmpty()) {
                // THÊM MỚI
                int playerId = playerDAO.insertAndReturnId(p);
                playerIndexDAO.insert(playerId, indexId, indexValue);
            } else {
                // CẬP NHẬT
                p.setPlayerId(Integer.parseInt(idStr));
                // PlayerDAO.update hiện tại phải xử lý update cả bảng player và player_index
                playerDAO.update(p);
            }

            res.sendRedirect(req.getContextPath() + "/player");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}