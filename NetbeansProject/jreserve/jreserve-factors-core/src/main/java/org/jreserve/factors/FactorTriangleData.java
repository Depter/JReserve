package org.jreserve.factors;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.rutil.RCode;
import org.jreserve.triangle.AbstractTriangularDataModification;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.entities.TriangleComment;
import org.jreserve.triangle.util.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FactorTriangleData extends AbstractTriangularDataModification {

    public final static String LAYER_ID = "FACTOR_TRIANGLE";
    private int accidentCount = 0;
    private int developmentCount = 0;
    private double[][] factors = null;
    private List<TriangleComment> comments = new ArrayList<TriangleComment>();
    
    public FactorTriangleData(TriangularData source) {
        super(source);
    }
    
    @Override
    public int getAccidentCount() {
        return accidentCount;
    }

    @Override
    public int getDevelopmentCount() {
        return developmentCount;
    }

    @Override
    public int getDevelopmentCount(int accident) {
        if(accident<0 || developmentCount <= accident)
            return 0;
        return factors[accident].length;
    }

    @Override
    public double getValue(int accident, int development) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double[][] toArray() {
        if(factors == null)
            return new double[0][];
        return factors;
    }

    @Override
    public List<TriangleComment> getComments(int accident, int development) {
        return TriangleUtil.filterValues(comments, accident, development);
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        return LAYER_ID;
    }

    @Override
    protected void recalculateCorrection() {
        accidentCount = calculateAccidentCount();
        developmentCount = calculateDevelopmentCount();
        calculateFactors();
    }
    
    private int calculateAccidentCount() {
        int accident = (source==null? 0 : source.getAccidentCount());
        while(accident > 0 && source.getDevelopmentCount(accident) < 2)
            accident--;
        return accident;
    }
    
    private int calculateDevelopmentCount() {
        int development = 0;
        for(int a=0; a<accidentCount; a++) {
            int dev = source.getDevelopmentCount(a) - 1;
            development = development<dev? dev : development;
        }
        return development;    
    }
    
    private void calculateFactors() {
        double[][] input = getInput();
        TriangleUtil.cummulate(input);
        calculateFactors(input);
    }
    
    private double[][] getInput() {
        double[][] input = source==null? null : source.toArray();
        if(input == null)
            return new double[0][];
        return input;
    }
    
    private void calculateFactors(double[][] input) {
        factors = new double[accidentCount][];
        for(int accident=0; accidentCount<accidentCount; accident++)
            factors[accident] = calculateFactors(input[accident]);
    }
    
    private double[] calculateFactors(double[] input) {
        int size = input.length - 1;
        double[] result = new double[size];
        for(int d=0; d<size; d++)
            result[d] = input[d+1] / input[d];
        return result;
    }

    @Override
    protected void modifyRTriangle(String triangleName, RCode rCode) {
        //TODO modify
    }
}
