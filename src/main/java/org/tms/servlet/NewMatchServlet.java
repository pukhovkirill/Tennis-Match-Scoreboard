package org.tms.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tms.service.OngoingMatchesService;

import java.io.IOException;

@MultipartConfig
public class NewMatchServlet extends HttpServlet {

    private OngoingMatchesService ongoingMatchesService;

    @Override
    public void init() throws ServletException {
        this.ongoingMatchesService = new OngoingMatchesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/newMatch.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String player1 = req.getParameter("player1");
        String player2 = req.getParameter("player2");

        if(player1.equals(player2)){
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Player1 and player2 must be different people!");
            return;
        }

        var uuid = ongoingMatchesService.createNewMatch(player1, player2).getUuid();

        resp.sendRedirect(String.format("/match-score?uuid=%s", uuid.toString()));
    }
}
