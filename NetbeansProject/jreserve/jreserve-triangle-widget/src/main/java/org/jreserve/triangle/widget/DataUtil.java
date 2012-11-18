package org.jreserve.triangle.widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.data.Comment;
import org.jreserve.triangle.data.TriangleComment;
import org.jreserve.triangle.data.TriangleCorrection;

/**
 *
 * @author Peter Decsi
 */
public class DataUtil {

    public static WidgetData<Double> convertCorrection(TriangleCorrection correction) {
        Date accident = correction.getAccidentDate();
        Date development = correction.getDevelopmentDate();
        double c = correction.getCorrection();
        return new WidgetData<Double>(accident, development, c);
    }
    
    public static List<WidgetData<Double>> convertCorrections(List<TriangleCorrection> corrections) {
        List<WidgetData<Double>> result = new ArrayList<WidgetData<Double>>(corrections.size());
        for(TriangleCorrection correction : corrections)
            result.add(convertCorrection(correction));
        return result;
    }

    public static TriangleCorrection escapeCorrection(PersistentObject owner, WidgetData<Double> data) {
        Date accident = data.getAccident();
        Date development = data.getDevelopment();
        TriangleCorrection tc = new TriangleCorrection(owner, accident, development);
        tc.setCorrection(data.getValue());
        return tc;
    }

    public static List<TriangleCorrection> escapeCorrections(PersistentObject owner, List<WidgetData<Double>> datas) {
        List<TriangleCorrection> result = new ArrayList<TriangleCorrection>(datas.size());
        for(WidgetData<Double> data : datas)
            result.add(escapeCorrection(owner, data));
        return result;
    }
    
    public static WidgetData<Double> convertData(ClaimValue data) {
        Date accident = data.getAccidentDate();
        Date development = data.getDevelopmentDate();
        Double c = data.getClaimValue();
        return new WidgetData<Double>(accident, development, c);
    }
    
    public static List<WidgetData<Double>> convertDatas(List<ClaimValue> datas) {
        List<WidgetData<Double>> result = new ArrayList<WidgetData<Double>>(datas.size());
        for(ClaimValue data : datas)
            result.add(convertData(data));
        return result;
    }    
        
    public static List<ClaimValue> escapeDatas(ProjectDataType dataType, List<WidgetData<Double>> values) {
        List<ClaimValue> result = new ArrayList<ClaimValue>(values.size());
        for (WidgetData<Double> value : values)
            result.add(escapeData(dataType, value));
        return result;
    }
    
    public static ClaimValue escapeData(ProjectDataType dataType, WidgetData<Double> wd) {
        Date accident = wd.getAccident();
        Date development = wd.getDevelopment();
        Double value = wd.getValue();
        return new ClaimValue(dataType, accident, development, value);
    }
    
    public static List<WidgetData<Comment>> convertComments(List<TriangleComment> comments) {
        List<WidgetData<Comment>> result = new ArrayList<WidgetData<Comment>>(comments.size());
        for(TriangleComment comment : comments)
            result.add(convertComment(comment));
        return result;
    }
    
    public static WidgetData<Comment> convertComment(TriangleComment comment) {
        Date accident = comment.getAccidentDate();
        Date development = comment.getDevelopmentDate();
        return new WidgetData<Comment>(accident, development, comment);
    }
}
