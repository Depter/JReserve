package org.decsi.jreserve.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Triangle extends Commentable {
    
    private int id;
    private String name;
    private LoB lob;
    private List<ClaimType> claimType = new ArrayList<>();
    private DataType dataType;
    private MonthDate startDate;
    private MonthDate endDate;
    
    private int monthPerAccident;
    private int monthPerDevelopment;
    private int accidentCount;
    private int developmentCount;
    
    private List<DoubleTriangleCell> cells = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LoB getLob() {
        return lob;
    }

    public List<ClaimType> getClaimTypes() {
        return claimType;
    }

    public DataType getDataType() {
        return dataType;
    }



    public MonthDate getStartDate() {
        return startDate;
    }

    public MonthDate getEndDate() {
        return endDate;
    }
    
    public int getAccidentCount() {
        return accidentCount;
    }

    public int getDevelopmentCount() {
        return developmentCount;
    }

    public int getMonthPerAccident() {
        return monthPerAccident;
    }

    public int getMonthPerDevelopment() {
        return monthPerDevelopment;
    }

    public DoubleTriangleCell getCells(int accident, int development) {
        for(DoubleTriangleCell cell : cells)
            if(cell.getAccident()==accident && cell.getDevelopment()==development)
                return cell;
        return null;
    }
    
    public double[][] getData() {
        double[][] data = initData();
        for(DoubleTriangleCell cell : cells)
            data[cell.getAccident()][cell.getDevelopment()] = cell.getValue();
        return data;
    }
    
    private double[][] initData() {
        double[][] data = new double[accidentCount][];
        for(int a=0; a<accidentCount; a++)
            data[a] = initDevelopment();
        return data;
    }
    
    private double[] initDevelopment() {
        double[] development = new double[developmentCount];
        for(int d=0; d<developmentCount; d++)
            development[d] = Double.NaN;
        return development;
    }
    
    @Override
    public int getCommentTypeId() {
        return Comment.TRIANGLE;
    }
}
