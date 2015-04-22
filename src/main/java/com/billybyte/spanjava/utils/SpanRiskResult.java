package com.billybyte.spanjava.utils;

import java.math.BigDecimal;

public class SpanRiskResult {

	private final Integer worstScenario;
	private final BigDecimal scanRisk;
	private final BigDecimal timeRisk;
	private final BigDecimal futPriceRisk;
	
	public SpanRiskResult(Integer worstScenario, BigDecimal scanRisk,
			BigDecimal timeRisk, BigDecimal futPriceRisk) {
		super();
		this.worstScenario = worstScenario;
		this.scanRisk = scanRisk;
		this.timeRisk = timeRisk;
		this.futPriceRisk = futPriceRisk;
	}

	public Integer getWorstScenario() {
		return worstScenario;
	}

	public BigDecimal getScanRisk() {
		return scanRisk;
	}

	public BigDecimal getTimeRisk() {
		return timeRisk;
	}

	public BigDecimal getFutPriceRisk() {
		return futPriceRisk;
	}
	
	public String toString() {
		return worstScenario+","+scanRisk+","+timeRisk+","+futPriceRisk;
	}
	
}
