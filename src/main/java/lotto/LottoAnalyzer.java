package lotto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LottoAnalyzer {

    private LottoGame lottoGame;

    public LottoAnalyzer(LottoGame lottoGame) {
        this.lottoGame = lottoGame;
    }

    public List<LottoRank> gradeTicket(int round, List<LottoTicket> lottoTickets) {

        WinningLotto winningLotto = getWinningLotto(round);

        return lottoTickets.stream()
                .map(lottoTicket -> LottoRank.valueOf(winningLotto.getContainNumberCount(lottoTicket), winningLotto.isBonusMatch(lottoTicket)))
                .filter(Objects::nonNull)
                .filter(lottoRank -> !lottoRank.equals(LottoRank.BOOM))
                .collect(Collectors.toList());
    }

    private WinningLotto getWinningLotto(int round) {
        WinningLotto winningLotto = lottoGame.get(round);

        if (Objects.isNull(winningLotto)) {
            throw new IllegalArgumentException("존재하지 않는 회차입니다.");
        }
        return winningLotto;
    }

    public double calculateRevenueRate(int round, List<LottoTicket> lottoTickets) {
        BigDecimal sum = gradeTicket(round, lottoTickets)
                .stream()
                .map(LottoRank::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.divide(BigDecimal.valueOf(lottoTickets.size() * 1000.0)).doubleValue();
    }
}
