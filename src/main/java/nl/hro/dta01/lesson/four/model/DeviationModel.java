package nl.hro.dta01.lesson.four.model;

public class DeviationModel {

    private int itemIdA;
    private int itemIdB;
    private int raters;
    private double div;

    public DeviationModel(int itemIdA, int itemIdB, int raters, double div) {
        this.itemIdA = itemIdA;
        this.itemIdB = itemIdB;
        this.raters = raters;
        this.div = div;
    }

    public int getRaters() {
        return raters;
    }

    public void setRaters(int raters) {
        this.raters = raters;
    }

    public double getDiv() {
        return div;
    }

    public void setDiv(double div) {
        this.div = div;
    }

    public int getItemIdA() {
        return itemIdA;
    }

    public void setItemIdA(int itemIdA) {
        this.itemIdA = itemIdA;
    }

    public int getItemIdB() {
        return itemIdB;
    }

    public void setItemIdB(int itemIdB) {
        this.itemIdB = itemIdB;
    }

    @Override
    public String toString() {
        return "DeviationModel{" +
                "raters=" + raters +
                ", div=" + div +
                '}';
    }
}
