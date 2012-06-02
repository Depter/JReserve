package org.decsi.jreserve.data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimTypeBuilder extends AbstractNamedDataBuilder<ClaimType> {

    @Override
    public ClaimType build() {
        super.checkState();
        ClaimType claimType = new ClaimType(id, shortName, name);
        clear();
        return claimType;
    }

}
