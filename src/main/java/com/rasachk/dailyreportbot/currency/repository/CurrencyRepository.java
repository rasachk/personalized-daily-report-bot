package com.rasachk.dailyreportbot.currency.repository;

import com.rasachk.dailyreportbot.currency.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    List<Currency> findByIsActiveTrueAndIsDeletedFalse();

}
