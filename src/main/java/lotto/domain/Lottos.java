package lotto.domain;

import lotto.dto.WinningLottoDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lottos {
    private List<Lotto> lottos;

    public Lottos(List<Lotto> lottos) {
        this(new Amount(0), lottos);
    }

    public Lottos(Amount amount) {
        this(amount, new ArrayList<>());
    }

    public Lottos(Amount amount, List<Lotto> manualLottos) {
        this.lottos = new ArrayList<>(manualLottos);
        for (int game = 0; game < amount.purchasableLottoCount(); game++) {
            this.lottos.add(new Lotto());
        }
    }

    public Long totalWinningAmount(WinningLottoDTO winningLottoDTO) {
        Amount amount = new Amount();
        for (Lotto lotto : this.lottos) {
            amount.add(lotto.winningInfo(winningLottoDTO).getWinningAmount());
        }
        return amount.amount();
    }

    public BigDecimal rateOfReturn(WinningLottoDTO winningLottoDTO) {
        Amount purchaseAmount = new Amount(this.lottos.size() * Lotto.LOTTO_PRICE);
        Amount winningAmount = new Amount(totalWinningAmount(winningLottoDTO));
        return winningAmount.divide(purchaseAmount.amount(), 2);
    }

    public List<Lotto> getLottos() {
        return Collections.unmodifiableList(this.lottos);
    }

    public int size() {
        return this.lottos.size();
    }

    public boolean isEmpty() {
        return lottos.size() == 0;
    }

    public int winningCorrectCount(WinningLottoDTO winningLottoDTO, Winning winning) {
        return (int) this.lottos.stream()
                .filter(lotto -> Winning.isMatched(lotto.winningInfo(winningLottoDTO), winning))
                .count();
    }
}
