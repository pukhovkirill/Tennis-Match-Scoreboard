package org.tms.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tms.service.FinishedMatchesPersistenceService;
import org.tms.service.MatchScoreCalculationService;
import org.tms.service.OngoingMatchesService;

import java.io.IOException;
import java.util.UUID;

@MultipartConfig
public class MatchScoreServlet extends HttpServlet {
    private OngoingMatchesService ongoingMatchesService;
    private FinishedMatchesPersistenceService persistenceService;
    private MatchScoreCalculationService calculationService;

    @Override
    public void init() throws ServletException {
        this.ongoingMatchesService = new OngoingMatchesService();
        this.persistenceService = new FinishedMatchesPersistenceService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var uuidString = req.getParameter("uuid");
        var uuid = UUID.fromString(uuidString);

        var optionalMatchScore = ongoingMatchesService.getMatch(uuid);

        if(optionalMatchScore.isEmpty()){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Match %s not found", uuidString));
            return;
        }

        var matchScore = optionalMatchScore.get();

        calculationService = new MatchScoreCalculationService(matchScore);

        req.setAttribute("match", matchScore);
        req.getRequestDispatcher("/views/matchScore.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var uuidString = req.getParameter("uuid");
        var optionalMatchScore = ongoingMatchesService.getMatch(UUID.fromString(uuidString));

        if(optionalMatchScore.isEmpty()){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Match %s not found", uuidString));
            return;
        }

        var matchScore = optionalMatchScore.get();

        var playerId = Long.valueOf(req.getParameter("winner"));
        calculationService.awardPoint(playerId);

        if(matchScore.getMatch().getWinner() != null){
            ongoingMatchesService.finishMatch(matchScore.getUuid());
            persistenceService.persist(matchScore.getMatch());
            req.setAttribute("match", matchScore);
            req.getRequestDispatcher("/views/finishedMatch.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(String.format("/match-score?uuid=%s", uuidString));
    }
}
