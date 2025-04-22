/* Author: Ayah Alshami
 * CSI 2300
 * April 18 2025
 * defines specific behavior for siamese cat
 * has 3 stages
  */

package TasKitty;

public class SiameseCat extends Pet {
    public SiameseCat(String name) {
        super(name, 3); 
    }
    
    // returns siamese rather than pet when called
    @Override
    public String getType() {
        return "siamese";
    }

    // overrides with unique stage count
    @Override
    public String getStage() {
        if (getLevel() == 1) return "egg";
        else if (getLevel() == 2) return "baby";
        else return "teen"; 
    }
}