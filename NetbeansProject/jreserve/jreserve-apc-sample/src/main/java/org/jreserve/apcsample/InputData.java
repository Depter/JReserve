package org.jreserve.apcsample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jreserve.data.Data;
import org.jreserve.data.DataTable;
import org.jreserve.data.ProjectDataType;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.entities.VectorGeometry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class InputData {

    private final static DataDummy[] BURNING_COST = {
        new DataDummy("1-1-1997", "1-1-1997", 345872),
        new DataDummy("1-1-1998", "1-1-1998", 395282),
        new DataDummy("1-1-1999", "1-1-1999", 444693),
        new DataDummy("1-1-2000", "1-1-2000", 494103),
        new DataDummy("1-1-2001", "1-1-2001", 544932),
        new DataDummy("1-1-2002", "1-1-2002", 620275),
        new DataDummy("1-1-2003", "1-1-2003", 668107),
        new DataDummy("1-1-2004", "1-1-2004", 718563)
    };
    
    private final static DataDummy[] BI_INCURRED = {
        new DataDummy("1-1-1997", "1-1-1997", 3013647.785),
        new DataDummy("1-1-1997", "1-1-1997", 825877.977),
        new DataDummy("1-1-1997", "1-1-1997", 102279.323),
        new DataDummy("1-1-1997", "1-1-1997", 1319.636),
        new DataDummy("1-1-1997", "1-1-1997", 8629.74),
        new DataDummy("1-1-1997", "1-1-1997", 284435.078),
        new DataDummy("1-1-1997", "1-1-1997", -127450.51),
        new DataDummy("1-1-1997", "1-1-1997", -119869.495),
        new DataDummy("1-1-1998", "1-1-1998", 1677038.902),
        new DataDummy("1-1-1998", "1-1-1998", 239628.502),
        new DataDummy("1-1-1998", "1-1-1998", 114674.833),
        new DataDummy("1-1-1998", "1-1-1998", 280201.957),
        new DataDummy("1-1-1998", "1-1-1998", 517838.726),
        new DataDummy("1-1-1998", "1-1-1998", -114008.662),
        new DataDummy("1-1-1998", "1-1-1998", -208186.881),
        new DataDummy("1-1-1999", "1-1-1999", 2119736.737),
        new DataDummy("1-1-1999", "1-1-1999", 365194.396),
        new DataDummy("1-1-1999", "1-1-1999", 240915.168),
        new DataDummy("1-1-1999", "1-1-1999", 603489.258),
        new DataDummy("1-1-1999", "1-1-1999", 1837.526),
        new DataDummy("1-1-1999", "1-1-1999", -273750.986),
        new DataDummy("1-1-2000", "1-1-2000", 1947059.996),
        new DataDummy("1-1-2000", "1-1-2000", 1205269.597),
        new DataDummy("1-1-2000", "1-1-2000", 774418.246),
        new DataDummy("1-1-2000", "1-1-2000", 39256.063),
        new DataDummy("1-1-2000", "1-1-2000", 189945.536),
        new DataDummy("1-1-2001", "1-1-2001", 3059368.599),
        new DataDummy("1-1-2001", "1-1-2001", 1160423.973),
        new DataDummy("1-1-2001", "1-1-2001", 526466.426),
        new DataDummy("1-1-2001", "1-1-2001", 352815.527),
        new DataDummy("1-1-2002", "1-1-2002", 4320019.156),
        new DataDummy("1-1-2002", "1-1-2002", -105700.75),
        new DataDummy("1-1-2002", "1-1-2002", 1211410.147),
        new DataDummy("1-1-2003", "1-1-2003", 2713203.465),
        new DataDummy("1-1-2003", "1-1-2003", 1083041.519),
        new DataDummy("1-1-2004", "1-1-2004", 3461298.109)
    };
    
    private final static DataDummy[] BI_PAID = {
        new DataDummy("1-1-1997", "1-1-1997", 261239.82),
        new DataDummy("1-1-1997", "1-1-1997", 353105.6),
        new DataDummy("1-1-1997", "1-1-1997", -255098.299),
        new DataDummy("1-1-1997", "1-1-1997", 166978.063),
        new DataDummy("1-1-1997", "1-1-1997", 20001.823),
        new DataDummy("1-1-1997", "1-1-1997", -408826.43),
        new DataDummy("1-1-1997", "1-1-1997", -7865.59),
        new DataDummy("1-1-1997", "1-1-1997", 209330.293),
        new DataDummy("1-1-1998", "1-1-1998", 202302.664),
        new DataDummy("1-1-1998", "1-1-1998", 270922.416),
        new DataDummy("1-1-1998", "1-1-1998", -165883.729),
        new DataDummy("1-1-1998", "1-1-1998", 28879.889),
        new DataDummy("1-1-1998", "1-1-1998", -67702.216),
        new DataDummy("1-1-1998", "1-1-1998", -212597.082),
        new DataDummy("1-1-1998", "1-1-1998", 122696.09),
        new DataDummy("1-1-1999", "1-1-1999", 237619.476),
        new DataDummy("1-1-1999", "1-1-1999", 331237.172),
        new DataDummy("1-1-1999", "1-1-1999", -175643.18),
        new DataDummy("1-1-1999", "1-1-1999", -122714.418),
        new DataDummy("1-1-1999", "1-1-1999", -21321.695),
        new DataDummy("1-1-1999", "1-1-1999", 36424.717),
        new DataDummy("1-1-2000", "1-1-2000", 237012.962),
        new DataDummy("1-1-2000", "1-1-2000", 319593.296),
        new DataDummy("1-1-2000", "1-1-2000", -127632.811),
        new DataDummy("1-1-2000", "1-1-2000", 67181.332),
        new DataDummy("1-1-2000", "1-1-2000", -90037.194),
        new DataDummy("1-1-2001", "1-1-2001", 388655.21),
        new DataDummy("1-1-2001", "1-1-2001", 239500.333),
        new DataDummy("1-1-2001", "1-1-2001", -99559.803),
        new DataDummy("1-1-2001", "1-1-2001", 30314.083),
        new DataDummy("1-1-2002", "1-1-2002", 259707.779),
        new DataDummy("1-1-2002", "1-1-2002", 310072.677),
        new DataDummy("1-1-2002", "1-1-2002", -36616.158),
        new DataDummy("1-1-2003", "1-1-2003", 236365.493),
        new DataDummy("1-1-2003", "1-1-2003", 506677.467),
        new DataDummy("1-1-2004", "1-1-2004", 248164.549)
    };
    
    private final static SimpleDateFormat DF = new SimpleDateFormat("dd-MM-yyyy");
    
    private final static Date START = getDate("1-1-1997");
    private final static int PERIODS = 8;
    private final static int MONTHS = 12;
    
    final static TriangleGeometry TRIANGLE_GEOMETRY = new TriangleGeometry(START, PERIODS, MONTHS);
    final static VectorGeometry VECTOR_GEOMETRY = new VectorGeometry(START, PERIODS, MONTHS);
    
    static List<Data<ProjectDataType, Double>> getData(ProjectDataType dt) {
        DataDummy[] dummies = getDummies(dt);
        return getData(dt, dummies);
    }
    
    private static DataDummy[] getDummies(ProjectDataType dt) {
        String name = dt.getClaimType().getName();
        if(SampleBuilder.BODILY_INJURY.equals(name))
            return getBiDummies(dt.getDbId());
        return new DataDummy[0];
    }
    
    private static DataDummy[] getBiDummies(int dataType) {
        switch(dataType) {
            case SampleBuilder.INCURRED:
                return BI_INCURRED;
            case SampleBuilder.PAID:
                return BI_PAID;
            default:
                return BURNING_COST;
        }
    }
    
    private static List<Data<ProjectDataType, Double>> getData(ProjectDataType dt, DataDummy[] dummies) {
        List<Data<ProjectDataType, Double>> datas = new ArrayList<Data<ProjectDataType, Double>>(dummies.length);
        for(DataDummy dummy : dummies)
            datas.add(createData(dt, dummy));
        return datas;
    }
    
    private static Data<ProjectDataType, Double> createData(ProjectDataType dt, DataDummy dummy) {
        Date accident = getDate(dummy.accidnet);
        Date development = getDate(dummy.development);
        return new Data<ProjectDataType, Double>(dt, accident, development, dummy.value);
    }
    
    private static Date getDate(String date) {
        try {
            return DF.parse(date);
        } catch (ParseException ex) {
            throw new IllegalArgumentException(date, ex);
        }
    }
    
    private static class DataDummy {
        private String accidnet;
        private String development;
        private double value;
        
        private DataDummy(String accident, String development, double value) {
            this.accidnet = accident;
            this.development = development;
            this.value = value;
        }
    }
}
