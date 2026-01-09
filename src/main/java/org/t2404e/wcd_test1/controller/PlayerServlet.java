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
                req.getRequestDispatcher("player-form.jsp").forward(req, res);
                return;
            }

            if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("player", playerDAO.findById(id));
                req.setAttribute("indexers", indexerDAO.getAll());
                req.getRequestDispatcher("player-form.jsp").forward(req, res);
                return;
            }

            if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                playerDAO.delete(id);
                res.sendRedirect("player");
                return;
            }


            req.setAttribute("players", playerDAO.getAll());
            req.getRequestDispatcher("index.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        try {
            String name = req.getParameter("name");
            String fullName = req.getParameter("fullName");
            String age = req.getParameter("age");
            int indexId = Integer.parseInt(req.getParameter("indexId"));
            float indexValue = Float.parseFloat(req.getParameter("indexValue"));

            // Validate name
            if (name == null || name.trim().isEmpty()) {
                req.setAttribute("error", "Name is required");
                req.setAttribute("indexers", indexerDAO.getAll());
                req.getRequestDispatcher("player-form.jsp").forward(req, res);
                return;
            }

            // Validate index value range
            Indexer idx = indexerDAO.findById(indexId);
            if (indexValue < idx.getValueMin() || indexValue > idx.getValueMax()) {
                req.setAttribute(
                        "error",
                        "Index value must be between " + idx.getValueMin() + " and " + idx.getValueMax()
                );
                req.setAttribute("indexers", indexerDAO.getAll());
                req.getRequestDispatcher("player-form.jsp").forward(req, res);
                return;
            }

            Player p = new Player();
            p.setName(name);
            p.setFullName(fullName);
            p.setAge(age);
            p.setIndexId(indexId);

            int playerId = playerDAO.insertAndReturnId(p);
            playerIndexDAO.insert(playerId, indexId, indexValue);

            res.sendRedirect("player");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
