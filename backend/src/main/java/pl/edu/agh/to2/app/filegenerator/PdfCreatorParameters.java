package pl.edu.agh.to2.app.filegenerator;

import org.apache.pdfbox.pdmodel.PDPage;

/**
 * Class that holds parameters needed in many places for PdfCreatorService
 * let us avoid passing many parameters in methods
 * let us avoid creating many fields in PdfCreatorService
 */
public class PdfCreatorParameters {
    private PDPage currentPage;
    private double width;
    private double endPage;
    private double baseStartX;
    private double baseStartY;
    private double startX;
    private double startY;
    private int level;
    double nextSentenceStartY;

    public double getNextSentenceStartY() {
        return nextSentenceStartY;
    }

    public PdfCreatorParameters incrementStartX(double increment) {
        this.startX += increment;
        return this;
    }
    public void decrementStartX(double decrement) {
        this.startX -= decrement;
    }

    public void decrementStartY(double decrement) {
        this.startY -= decrement;
    }
    public void incrementStartY(double increment) {
        this.startY += increment;
    }

    public PdfCreatorParameters setNextSentenceStartY(double nextSentenceStartY) {
        this.nextSentenceStartY = nextSentenceStartY;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public PdfCreatorParameters setLevel(int level) {
        this.level = level;
        return this;
    }

    public double getStartY() {
        return startY;
    }

    public PdfCreatorParameters setStartY(double startY) {
        this.startY = startY;
        return this;
    }

    public double getStartX() {
        return startX;
    }

    public PdfCreatorParameters setStartX(double startX) {
        this.startX = startX;
        return this;
    }

    public double getBaseStartY() {
        return baseStartY;
    }

    public PdfCreatorParameters setBaseStartY(double baseStartY) {
        this.baseStartY = baseStartY;
        return this;
    }

    public double getBaseStartX() {
        return baseStartX;
    }

    public PdfCreatorParameters setBaseStartX(double baseStartX) {
        this.baseStartX = baseStartX;
        return this;
    }

    public double getEndPage() {
        return endPage;
    }

    public PdfCreatorParameters setEndPage(double endPage) {
        this.endPage = endPage;
        return this;
    }

    public double getWidth() {
        return width;
    }

    public PdfCreatorParameters setWidth(double width) {
        this.width = width;
        return this;
    }

    public PDPage getCurrentPage() {
        return currentPage;
    }

    public PdfCreatorParameters setCurrentPage(PDPage currentPage) {
        this.currentPage = currentPage;
        return this;
    }


}
