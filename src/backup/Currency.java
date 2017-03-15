package backup;

/*
 * The Currency class represents a single coin in the application. The Currency has all the data that
 * a currency needs.
 */

public class Currency {

	private String name;
	private int unit;
	private String country;
	private String code;
	private double rate;
	private double change;

	/*The single Constructor with full data needed. when a Currency object created, it must initializing
	 * all the data it needs.
	 */
	public Currency(String name, int unit, String country, String code,
			double rate, double change) {
		this.name = name;
		this.unit = unit;
		this.country = country;
		this.code = code;
		this.rate = rate;
		this.change = change;
	}

	//Getters for all variables.
	public String getName() {return name;}
	public int getUnit() {return unit;}
	public String getCountry() {return country;}
	public String getCode() {return code;}
	public double getRate() {return rate;}
	public double getChange() {return change;}

	@Override
	public String toString() {
		return "Currency [name=" + name + ", unit=" + unit + ", country=" +
				country + ", code=" + code + ", rate="+ rate + ", change=" + change + "]";
	}


	@Override
	//Override the equals method so we can check if one Currency equals to another.
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Currency other = (Currency) obj;
		if (Double.doubleToLongBits(change) != Double.doubleToLongBits(other.change) ||
				Double.doubleToLongBits(rate) != Double.doubleToLongBits(other.rate) ||
				unit != other.unit	) return false;
		if (code == null) {
			if (other.code != null) return false;
		} else if (!code.equals(other.code)) return false;
		if (country == null) {
			if (other.country != null) return false;
		} else if (!country.equals(other.country)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;

		return true;
	}
}