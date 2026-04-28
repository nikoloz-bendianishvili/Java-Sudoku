public class Metropolis {

    private String metropolis;
    private String continent;
    private long population;

    public Metropolis(String metropolis, String continent, long population) {
        this.metropolis = metropolis;
        this.continent = continent;
        this.population = population;
    }

    public String getMetropolis() {
        return metropolis;
    }

    public String getContinent() {
        return continent;
    }

    public long getPopulation() {
        return population;
    }
}
