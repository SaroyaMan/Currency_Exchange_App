public class Currency {
	
	private String name;
	private int unit;
	private String country;
	private String code;
	private double rate;
	private double change;
	
	public Currency(String name, int unit, String country, String code,
					double rate, double change) {
		this.name = name;
		this.unit = unit;
		this.country = country;
		this.code = code;
		this.rate = rate;
		this.change = change;
	}

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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Currency other = (Currency) obj;
		if (Double.doubleToLongBits(change) != Double.doubleToLongBits(other.change))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(rate) != Double.doubleToLongBits(other.rate))
			return false;
		if (unit != other.unit)
			return false;
		return true;
	}
}