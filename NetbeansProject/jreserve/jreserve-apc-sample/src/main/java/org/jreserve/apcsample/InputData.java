package org.jreserve.apcsample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.ClaimValue;
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
    
    private final static DataDummy[] BI_NUMBER = {
        new DataDummy("1-1-1997", "1-1-1997", 653),
        new DataDummy("1-1-1997", "1-1-1998", 56),
        new DataDummy("1-1-1997", "1-1-1999", 7),
        new DataDummy("1-1-1997", "1-1-2000", 2),
        new DataDummy("1-1-1997", "1-1-2001", 3),
        new DataDummy("1-1-1997", "1-1-2002", 8),
        new DataDummy("1-1-1997", "1-1-2003", 0),
        new DataDummy("1-1-1997", "1-1-2004", 0),
        new DataDummy("1-1-1998", "1-1-1998", 538),
        new DataDummy("1-1-1998", "1-1-1999", 46),
        new DataDummy("1-1-1998", "1-1-2000", 12),
        new DataDummy("1-1-1998", "1-1-2001", 2),
        new DataDummy("1-1-1998", "1-1-2002", 6),
        new DataDummy("1-1-1998", "1-1-2003", 1),
        new DataDummy("1-1-1998", "1-1-2004", 1),
        new DataDummy("1-1-1999", "1-1-1999", 613),
        new DataDummy("1-1-1999", "1-1-2000", 43),
        new DataDummy("1-1-1999", "1-1-2001", 9),
        new DataDummy("1-1-1999", "1-1-2002", 11),
        new DataDummy("1-1-1999", "1-1-2003", 0),
        new DataDummy("1-1-1999", "1-1-2004", 1),
        new DataDummy("1-1-2000", "1-1-2000", 676),
        new DataDummy("1-1-2000", "1-1-2001", 86),
        new DataDummy("1-1-2000", "1-1-2002", 31),
        new DataDummy("1-1-2000", "1-1-2003", 3),
        new DataDummy("1-1-2000", "1-1-2004", 0),
        new DataDummy("1-1-2001", "1-1-2001", 767),
        new DataDummy("1-1-2001", "1-1-2002", 92),
        new DataDummy("1-1-2001", "1-1-2003", 9),
        new DataDummy("1-1-2001", "1-1-2004", 2),
        new DataDummy("1-1-2002", "1-1-2002", 760),
        new DataDummy("1-1-2002", "1-1-2003", 73),
        new DataDummy("1-1-2002", "1-1-2004", 5),
        new DataDummy("1-1-2003", "1-1-2003", 789),
        new DataDummy("1-1-2003", "1-1-2004", 67),
        new DataDummy("1-1-2004", "1-1-2004", 896)
    };
    
    private final static DataDummy[] BI_INCURRED = {
        new DataDummy("1-1-1997", "1-1-1997", 3013647.785),
        new DataDummy("1-1-1997", "1-1-1998", 825877.977),
        new DataDummy("1-1-1997", "1-1-1999", 102279.323),
        new DataDummy("1-1-1997", "1-1-2000", 1319.636),
        new DataDummy("1-1-1997", "1-1-2001", 8629.74),
        new DataDummy("1-1-1997", "1-1-2002", 284435.078),
        new DataDummy("1-1-1997", "1-1-2003", -127450.51),
        new DataDummy("1-1-1997", "1-1-2004", -119869.495),
        new DataDummy("1-1-1998", "1-1-1998", 1677038.902),
        new DataDummy("1-1-1998", "1-1-1999", 239628.502),
        new DataDummy("1-1-1998", "1-1-2000", 114674.833),
        new DataDummy("1-1-1998", "1-1-2001", 280201.957),
        new DataDummy("1-1-1998", "1-1-2002", 517838.726),
        new DataDummy("1-1-1998", "1-1-2003", -114008.662),
        new DataDummy("1-1-1998", "1-1-2004", -208186.881),
        new DataDummy("1-1-1999", "1-1-1999", 2119736.737),
        new DataDummy("1-1-1999", "1-1-2000", 365194.396),
        new DataDummy("1-1-1999", "1-1-2001", 240915.168),
        new DataDummy("1-1-1999", "1-1-2002", 603489.258),
        new DataDummy("1-1-1999", "1-1-2003", 1837.526),
        new DataDummy("1-1-1999", "1-1-2004", -273750.986),
        new DataDummy("1-1-2000", "1-1-2000", 1947059.996),
        new DataDummy("1-1-2000", "1-1-2001", 1205269.597),
        new DataDummy("1-1-2000", "1-1-2002", 774418.246),
        new DataDummy("1-1-2000", "1-1-2003", 39256.063),
        new DataDummy("1-1-2000", "1-1-2004", 189945.536),
        new DataDummy("1-1-2001", "1-1-2001", 3059368.599),
        new DataDummy("1-1-2001", "1-1-2002", 1160423.973),
        new DataDummy("1-1-2001", "1-1-2003", 526466.426),
        new DataDummy("1-1-2001", "1-1-2004", 352815.527),
        new DataDummy("1-1-2002", "1-1-2002", 4320019.156),
        new DataDummy("1-1-2002", "1-1-2003", -105700.75),
        new DataDummy("1-1-2002", "1-1-2004", 1211410.147),
        new DataDummy("1-1-2003", "1-1-2003", 2713203.465),
        new DataDummy("1-1-2003", "1-1-2004", 1083041.519),
        new DataDummy("1-1-2004", "1-1-2004", 3461298.109)
    };
    
    private final static DataDummy[] BI_PAID = {
        new DataDummy("1-1-1997", "1-1-1997", 261239.82),
        new DataDummy("1-1-1997", "1-1-1998", 353105.6),
        new DataDummy("1-1-1997", "1-1-1999", -255098.299),
        new DataDummy("1-1-1997", "1-1-2000", 166978.063),
        new DataDummy("1-1-1997", "1-1-2001", 20001.823),
        new DataDummy("1-1-1997", "1-1-2002", -408826.43),
        new DataDummy("1-1-1997", "1-1-2003", -7865.59),
        new DataDummy("1-1-1997", "1-1-2004", 209330.293),
        new DataDummy("1-1-1998", "1-1-1998", 202302.664),
        new DataDummy("1-1-1998", "1-1-1999", 270922.416),
        new DataDummy("1-1-1998", "1-1-2000", -165883.729),
        new DataDummy("1-1-1998", "1-1-2001", 28879.889),
        new DataDummy("1-1-1998", "1-1-2002", -67702.216),
        new DataDummy("1-1-1998", "1-1-2003", -212597.082),
        new DataDummy("1-1-1998", "1-1-2004", 122696.09),
        new DataDummy("1-1-1999", "1-1-1999", 237619.476),
        new DataDummy("1-1-1999", "1-1-2000", 331237.172),
        new DataDummy("1-1-1999", "1-1-2001", -175643.18),
        new DataDummy("1-1-1999", "1-1-2002", -122714.418),
        new DataDummy("1-1-1999", "1-1-2003", -21321.695),
        new DataDummy("1-1-1999", "1-1-2004", 36424.717),
        new DataDummy("1-1-2000", "1-1-2000", 237012.962),
        new DataDummy("1-1-2000", "1-1-2001", 319593.296),
        new DataDummy("1-1-2000", "1-1-2002", -127632.811),
        new DataDummy("1-1-2000", "1-1-2003", 67181.332),
        new DataDummy("1-1-2000", "1-1-2004", -90037.194),
        new DataDummy("1-1-2001", "1-1-2001", 388655.21),
        new DataDummy("1-1-2001", "1-1-2002", 239500.333),
        new DataDummy("1-1-2001", "1-1-2003", -99559.803),
        new DataDummy("1-1-2001", "1-1-2004", 30314.083),
        new DataDummy("1-1-2002", "1-1-2002", 259707.779),
        new DataDummy("1-1-2002", "1-1-2003", 310072.677),
        new DataDummy("1-1-2002", "1-1-2004", -36616.158),
        new DataDummy("1-1-2003", "1-1-2003", 236365.493),
        new DataDummy("1-1-2003", "1-1-2004", 506677.467),
        new DataDummy("1-1-2004", "1-1-2004", 248164.549)
    };
    
    private final static DataDummy[] MD_NUMBER = {
        new DataDummy("1-1-1997", "1-1-1997", 26373),
        new DataDummy("1-1-1997", "1-1-1998", 1173),
        new DataDummy("1-1-1997", "1-1-1999", 14),
        new DataDummy("1-1-1997", "1-1-2000", 4),
        new DataDummy("1-1-1997", "1-1-2001", 2),
        new DataDummy("1-1-1997", "1-1-2002", 11),
        new DataDummy("1-1-1997", "1-1-2003", 0),
        new DataDummy("1-1-1997", "1-1-2004", 0),
        new DataDummy("1-1-1998", "1-1-1998", 27623),
        new DataDummy("1-1-1998", "1-1-1999", 1078),
        new DataDummy("1-1-1998", "1-1-2000", 19),
        new DataDummy("1-1-1998", "1-1-2001", 11),
        new DataDummy("1-1-1998", "1-1-2002", 8),
        new DataDummy("1-1-1998", "1-1-2003", 0),
        new DataDummy("1-1-1998", "1-1-2004", 0),
        new DataDummy("1-1-1999", "1-1-1999", 30908),
        new DataDummy("1-1-1999", "1-1-2000", 1299),
        new DataDummy("1-1-1999", "1-1-2001", 27),
        new DataDummy("1-1-1999", "1-1-2002", 17),
        new DataDummy("1-1-1999", "1-1-2003", 2),
        new DataDummy("1-1-1999", "1-1-2004", 1),
        new DataDummy("1-1-2000", "1-1-2000", 31182),
        new DataDummy("1-1-2000", "1-1-2001", 1392),
        new DataDummy("1-1-2000", "1-1-2002", 46),
        new DataDummy("1-1-2000", "1-1-2003", 2),
        new DataDummy("1-1-2000", "1-1-2004", 1),
        new DataDummy("1-1-2001", "1-1-2001", 32855),
        new DataDummy("1-1-2001", "1-1-2002", 1754),
        new DataDummy("1-1-2001", "1-1-2003", 46),
        new DataDummy("1-1-2001", "1-1-2004", 15),
        new DataDummy("1-1-2002", "1-1-2002", 37661),
        new DataDummy("1-1-2002", "1-1-2003", 1634),
        new DataDummy("1-1-2002", "1-1-2004", 54),
        new DataDummy("1-1-2003", "1-1-2003", 38160),
        new DataDummy("1-1-2003", "1-1-2004", 1696),
        new DataDummy("1-1-2004", "1-1-2004", 40194)
    };
    
    private final static DataDummy[] MD_INCURRED = {
        new DataDummy("1-1-1997", "1-1-1997", 4873558.967),
        new DataDummy("1-1-1997", "1-1-1998", 5726627.703),
        new DataDummy("1-1-1997", "1-1-1999", 5643477.71),
        new DataDummy("1-1-1997", "1-1-2000", 5703210.185),
        new DataDummy("1-1-1997", "1-1-2001", 5787091.086),
        new DataDummy("1-1-1997", "1-1-2002", 5858937.234),
        new DataDummy("1-1-1997", "1-1-2003", 5862561.124),
        new DataDummy("1-1-1997", "1-1-2004", 5791897.954),
        new DataDummy("1-1-1998", "1-1-1998", 5130849.158),
        new DataDummy("1-1-1998", "1-1-1999", 6098121.531),
        new DataDummy("1-1-1998", "1-1-2000", 6177774.474),
        new DataDummy("1-1-1998", "1-1-2001", 6165459.838),
        new DataDummy("1-1-1998", "1-1-2002", 6292000.607),
        new DataDummy("1-1-1998", "1-1-2003", 6304102.196),
        new DataDummy("1-1-1998", "1-1-2004", 6304337.616),
        new DataDummy("1-1-1999", "1-1-1999", 5945610.919),
        new DataDummy("1-1-1999", "1-1-2000", 7525655.574),
        new DataDummy("1-1-1999", "1-1-2001", 7557814.619),
        new DataDummy("1-1-1999", "1-1-2002", 7377334.785),
        new DataDummy("1-1-1999", "1-1-2003", 7442120.115),
        new DataDummy("1-1-1999", "1-1-2004", 7418866.015),
        new DataDummy("1-1-2000", "1-1-2000", 6632221.178),
        new DataDummy("1-1-2000", "1-1-2001", 7861102.002),
        new DataDummy("1-1-2000", "1-1-2002", 7532936.665),
        new DataDummy("1-1-2000", "1-1-2003", 7528468.098),
        new DataDummy("1-1-2000", "1-1-2004", 7525867.879),
        new DataDummy("1-1-2001", "1-1-2001", 7020974.106),
        new DataDummy("1-1-2001", "1-1-2002", 8688585.802),
        new DataDummy("1-1-2001", "1-1-2003", 8712863.977),
        new DataDummy("1-1-2001", "1-1-2004", 8674967.328),
        new DataDummy("1-1-2002", "1-1-2002", 8275452.681),
        new DataDummy("1-1-2002", "1-1-2003", 9867326.013),
        new DataDummy("1-1-2002", "1-1-2004", 9932303.813),
        new DataDummy("1-1-2003", "1-1-2003", 9000367.513),
        new DataDummy("1-1-2003", "1-1-2004", 10239887.721),
        new DataDummy("1-1-2004", "1-1-2004", 9511539.178)
    };
    
    private final static DataDummy[] MD_PAID = {
        new DataDummy("1-1-1997", "1-1-1997", 4426764.786),
        new DataDummy("1-1-1997", "1-1-1998", 992329.57),
        new DataDummy("1-1-1997", "1-1-1999", 88951.978),
        new DataDummy("1-1-1997", "1-1-2000", 13240.372),
        new DataDummy("1-1-1997", "1-1-2001", 38621.509),
        new DataDummy("1-1-1997", "1-1-2002", 26720.378),
        new DataDummy("1-1-1997", "1-1-2003", 36818.038),
        new DataDummy("1-1-1997", "1-1-2004", 10750.132),
        new DataDummy("1-1-1998", "1-1-1998", 4388958.408),
        new DataDummy("1-1-1998", "1-1-1999", 984169.331),
        new DataDummy("1-1-1998", "1-1-2000", 60161.739),
        new DataDummy("1-1-1998", "1-1-2001", 35004.177),
        new DataDummy("1-1-1998", "1-1-2002", 75767.782),
        new DataDummy("1-1-1998", "1-1-2003", 23890.016),
        new DataDummy("1-1-1998", "1-1-2004", 572.011),
        new DataDummy("1-1-1999", "1-1-1999", 5280130.25),
        new DataDummy("1-1-1999", "1-1-2000", 1239396.19),
        new DataDummy("1-1-1999", "1-1-2001", 76122.459),
        new DataDummy("1-1-1999", "1-1-2002", 110189.338),
        new DataDummy("1-1-1999", "1-1-2003", 112894.825),
        new DataDummy("1-1-1999", "1-1-2004", 11750.766),
        new DataDummy("1-1-2000", "1-1-2000", 5445384.037),
        new DataDummy("1-1-2000", "1-1-2001", 1164233.654),
        new DataDummy("1-1-2000", "1-1-2002", 171582.851),
        new DataDummy("1-1-2000", "1-1-2003", 16426.833),
        new DataDummy("1-1-2000", "1-1-2004", 6451.274),
        new DataDummy("1-1-2001", "1-1-2001", 5612137.899),
        new DataDummy("1-1-2001", "1-1-2002", 1837950.37),
        new DataDummy("1-1-2001", "1-1-2003", 155862.933),
        new DataDummy("1-1-2001", "1-1-2004", 127146.473),
        new DataDummy("1-1-2002", "1-1-2002", 6593299.049),
        new DataDummy("1-1-2002", "1-1-2003", 1592418.03),
        new DataDummy("1-1-2002", "1-1-2004", 74189.356),
        new DataDummy("1-1-2003", "1-1-2003", 6603090.84),
        new DataDummy("1-1-2003", "1-1-2004", 1659747.752),
        new DataDummy("1-1-2004", "1-1-2004", 7194586.604)
    };
    
    private final static SimpleDateFormat DF = new SimpleDateFormat("dd-MM-yyyy");
    
    private final static Date START = getDate("1-1-1997");
    private final static int PERIODS = 8;
    private final static int MONTHS = 12;
    
    final static TriangleGeometry TRIANGLE_GEOMETRY = new TriangleGeometry(START, PERIODS, MONTHS);
    final static VectorGeometry VECTOR_GEOMETRY = new VectorGeometry(START, PERIODS, MONTHS);
    
    static List<ClaimValue> getData(ProjectDataType dt) {
        DataDummy[] dummies = getDummies(dt);
        return getData(dt, dummies);
    }
    
    private static DataDummy[] getDummies(ProjectDataType dt) {
        String name = dt.getClaimType().getName();
        if(SampleBuilder.BODILY_INJURY.equals(name))
            return getBiDummies(dt.getDbId());
        return getMdDummies(dt.getDbId());
    }
    
    private static DataDummy[] getBiDummies(int dataType) {
        switch(dataType) {
            case SampleBuilder.INCURRED:
                return BI_INCURRED;
            case SampleBuilder.PAID:
                return BI_PAID;
            case SampleBuilder.NUMBER:
                return BI_NUMBER;
            default:
                return BURNING_COST;
        }
    }
    
    private static DataDummy[] getMdDummies(int dataType) {
        switch(dataType) {
            case SampleBuilder.INCURRED:
                return MD_INCURRED;
            case SampleBuilder.PAID:
                return MD_PAID;
            case SampleBuilder.NUMBER:
                return MD_NUMBER;
            default:
                return BURNING_COST;
        }
    }
    
    private static List<ClaimValue> getData(ProjectDataType dt, DataDummy[] dummies) {
        List<ClaimValue> datas = new ArrayList<ClaimValue>(dummies.length);
        for(DataDummy dummy : dummies)
            datas.add(createData(dt, dummy));
        return datas;
    }
    
    private static ClaimValue createData(ProjectDataType dt, DataDummy dummy) {
        Date accident = getDate(dummy.accidnet);
        Date development = getDate(dummy.development);
        return new ClaimValue(dt, accident, development, dummy.value);
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
