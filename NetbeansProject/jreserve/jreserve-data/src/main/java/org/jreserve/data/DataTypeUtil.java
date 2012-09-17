package org.jreserve.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "DataTypeUtil.DataType.incurred.name=Incurred",
    "DataTypeUtil.DataType.incurred.dbId=100",
    "DataTypeUtil.DataType.incurred.istriangle=true",
    "DataTypeUtil.DataType.paid.name=Paid",
    "DataTypeUtil.DataType.paid.dbId=200",
    "DataTypeUtil.DataType.paid.istriangle=true",
    "DataTypeUtil.DataType.reserve.name=Reserve",
    "DataTypeUtil.DataType.reserve.dbId=300",
    "DataTypeUtil.DataType.reserve.istriangle=true",
    "DataTypeUtil.DataType.claimcount.name=Claim count",
    "DataTypeUtil.DataType.claimcount.dbId=400",
    "DataTypeUtil.DataType.claimcount.istriangle=true",
    "DataTypeUtil.DataType.premium.name=Premium",
    "DataTypeUtil.DataType.premium.dbId=500",
    "DataTypeUtil.DataType.premium.istriangle=false",
    "DataTypeUtil.DataType.policies.name=Policies",
    "DataTypeUtil.DataType.policies.dbId=600",
    "DataTypeUtil.DataType.policies.istriangle=false",
    "DataTypeUtil.DataType.othertriangle.name=Other Triangle",
    "DataTypeUtil.DataType.othertriangle.dbId=1000",
    "DataTypeUtil.DataType.othertriangle.istriangle=true",
    "DataTypeUtil.DataType.othervector.name=Other Vector",
    "DataTypeUtil.DataType.othervector.dbId=1100",
    "DataTypeUtil.DataType.othervector.istriangle=false"
})
public class DataTypeUtil {
    
    private final static Logger logger = Logging.getLogger(DataTypeUtil.class.getName());
    
    private final static String PATH = "Data/data-type.xml";
    private static FileObject XML = null;
    private static DataTypeRoot ROOT = null;
    
    public synchronized static DataType parse(int dbId) {
        DataTypeRoot root = getRoot();
        DataType dt = root==null? null : root.getDataType(dbId);
        if(dt != null)
            return dt;
        throw new IllegalArgumentException("Unkwon DataType id: "+dbId);
    }
    
    private static DataTypeRoot getRoot() {
        if(ROOT == null)
            loadXML();
        return ROOT;
    }
    
    public synchronized static DataType createDataType(int dbId, String name, boolean isTriangle) {
        DataTypeRoot root = getRoot();
        if(root == null)
            return null;
        return root.createDataType(dbId, name, isTriangle);
    }
    
    public synchronized static void setName(DataType dt, String name) {
        DataTypeRoot root = getRoot();
        if(root != null)
            root.setName(dt, name);
    }
    
    public synchronized static List<DataType> getDataTypes() {
        DataTypeRoot root = getRoot();
        if(root == null)
            return new ArrayList<DataType>();
        return ROOT.getDataTypes();
    }
    
    private static void loadXML() {
        if(XML != null) {
            readFile();
        } else {
            if(initXmlFile()) {
                createDataTypRoot();
                save(ROOT);
            }
        }
    }
    
    private static void readFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(DataTypeRoot.class);
            Unmarshaller um = context.createUnmarshaller();
            ROOT = (DataTypeRoot) um.unmarshal(FileUtil.toFile(XML));
        } catch (JAXBException ex) {
            logger.error(ex, "Unable tu unmarshall xml document!", XML.getPath());
            Exceptions.printStackTrace(ex);
        }
    }
    
    private static void createDataTypRoot() {
        ROOT = new DataTypeRoot();
        
        int id = Integer.parseInt(Bundle.DataTypeUtil_DataType_incurred_dbId());
        String name = Bundle.DataTypeUtil_DataType_incurred_name();
        boolean isTriangle = Bundle.DataTypeUtil_DataType_incurred_istriangle().equalsIgnoreCase("true");
        ROOT.createDataType(id, name, isTriangle);
        
        id = Integer.parseInt(Bundle.DataTypeUtil_DataType_paid_dbId());
        name = Bundle.DataTypeUtil_DataType_paid_name();
        isTriangle = Bundle.DataTypeUtil_DataType_paid_istriangle().equalsIgnoreCase("true");
        ROOT.createDataType(id, name, isTriangle);
        
        id = Integer.parseInt(Bundle.DataTypeUtil_DataType_reserve_dbId());
        name = Bundle.DataTypeUtil_DataType_reserve_name();
        isTriangle = Bundle.DataTypeUtil_DataType_reserve_istriangle().equalsIgnoreCase("true");
        ROOT.createDataType(id, name, isTriangle);
        
        id = Integer.parseInt(Bundle.DataTypeUtil_DataType_claimcount_dbId());
        name = Bundle.DataTypeUtil_DataType_claimcount_name();
        isTriangle = Bundle.DataTypeUtil_DataType_claimcount_istriangle().equalsIgnoreCase("true");
        ROOT.createDataType(id, name, isTriangle);
        
        id = Integer.parseInt(Bundle.DataTypeUtil_DataType_premium_dbId());
        name = Bundle.DataTypeUtil_DataType_premium_name();
        isTriangle = Bundle.DataTypeUtil_DataType_premium_istriangle().equalsIgnoreCase("true");
        ROOT.createDataType(id, name, isTriangle);
        
        id = Integer.parseInt(Bundle.DataTypeUtil_DataType_policies_dbId());
        name = Bundle.DataTypeUtil_DataType_policies_name();
        isTriangle = Bundle.DataTypeUtil_DataType_policies_istriangle().equalsIgnoreCase("true");
        ROOT.createDataType(id, name, isTriangle);
           
        id = Integer.parseInt(Bundle.DataTypeUtil_DataType_othertriangle_dbId());
        name = Bundle.DataTypeUtil_DataType_othertriangle_name();
        isTriangle = Bundle.DataTypeUtil_DataType_othertriangle_istriangle().equalsIgnoreCase("true");
        ROOT.createDataType(id, name, isTriangle);
           
        id = Integer.parseInt(Bundle.DataTypeUtil_DataType_othervector_dbId());
        name = Bundle.DataTypeUtil_DataType_othervector_name();
        isTriangle = Bundle.DataTypeUtil_DataType_othervector_istriangle().equalsIgnoreCase("true");
        ROOT.createDataType(id, name, isTriangle);
    }
    
    private static boolean initXmlFile() {
        try {
            FileObject configHome = FileUtil.getConfigRoot();
            XML = FileUtil.createData(configHome, PATH);
            logger.info("DataType settings home set to '%s'.", XML.getPath());
            return true;
        } catch (IOException ex) {
            logger.error(ex, "Unable to create file '%s' in user dir!", PATH);
            Exceptions.printStackTrace(ex);
            return false;
        }
    }
    
    public synchronized static void save() {
        DataTypeRoot root = getRoot();
        if(root != null)
            save(root);
    }
    
    private static void save(DataTypeRoot root) {
        try {
            JAXBContext context = JAXBContext.newInstance(DataTypeRoot.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(root, FileUtil.toFile(XML));
            logger.debug("DataTypes saved: "+XML.getPath());
        } catch (JAXBException ex) {
            logger.error(ex, "Unable to save file '%s' in user dir!", PATH);
            Exceptions.printStackTrace(ex);
        }
    }
    
    @XmlRootElement(name="data-types")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class DataTypeRoot {
    
        @XmlElement(name="data-type")
        private List<DataType> dataTypes = new ArrayList<DataType>();
        
        private List<DataType> getDataTypes() {
            List<DataType> dts = new ArrayList<DataType>(dataTypes);
            Collections.sort(dts);
            return dts;
        }
        
        private void setName(DataType dt, String name) {
            dt = getDataType(dt.getDbId());
            if(dt == null)
                throw new IllegalArgumentException("DataType not found! "+dt);
            checkNewName(dt.getDbId(), name);
            dt.setName(name);
        }
        
        private void checkNewName(int dbId, String name) {
            for(DataType dt : dataTypes)
                if(dt.getDbId()!=dbId && dt.getName().equalsIgnoreCase(name))
                    throw new IllegalArgumentException("Name laredy used by: "+dt);
        }
        
        private DataType createDataType(int dbId, String name, boolean isTriangle) {
            checkNewDbId(dbId);
            checkNewName(name);
            DataType dt = new DataType(dbId, name, isTriangle);
            dataTypes.add(dt);
            return dt;
        }
        
        private void checkNewDbId(int dbId) {
            DataType dt = getDataType(dbId);
            if(dt == null)
                return;
            throw new IllegalArgumentException("DbId already used: "+dt);
        }
        
        private void checkNewName(String name) {
            for(DataType dt : dataTypes)
                if(dt.getName().equalsIgnoreCase(name))
                    throw new IllegalArgumentException("Name '"+name+"' already used in: "+dt+"!");
        }
        
        private DataType getDataType(int dbId) {
            for(DataType dt : dataTypes)
                if(dt.getDbId() == dbId)
                    return dt;
            return null;
        }
    }
}
